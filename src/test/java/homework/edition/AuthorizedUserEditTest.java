package homework.edition;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.assertions.Assertions;
import lib.constant.Constant;
import lib.constant.ResponseConstant;
import lib.constant.URL;
import lib.utility.APIUtility;
import lib.utility.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Edition cases")
@Feature("Edition authorized user")
public class AuthorizedUserEditTest extends BaseTest {
    private String userId;
    private String userName;
    private String userEmail;
    private Response responseGetAuth;

    @BeforeEach
    public void createUser() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.makePostRequest(URL.API_USER, userData).jsonPath();
        userId = responseCreateAuth.getString(ResponseConstant.ID);
        userName = userData.get(ResponseConstant.USERNAME);
        userEmail = userData.get(ResponseConstant.EMAIL);

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put(ResponseConstant.EMAIL, userEmail);
        authData.put(ResponseConstant.PASSWORD, userData.get(ResponseConstant.PASSWORD));

        responseGetAuth = apiCoreRequests.makePostRequest(URL.API_USER_LOGIN, authData);
    }

    @Test
    @Description("- generate user" +
            "- login " +
            "- edit user" +
            "- check user data after edition")
    @DisplayName("Edit just created user")
    public void testEditJustCreatedUser() {
        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put(ResponseConstant.FIRST_NAME, newName);

        Response responseEditUser =
                apiCoreRequests.makePutRequest(URL.API_USER + userId,
                        editData,
                        APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN),
                        APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID));

        //GET
        Response responseUserData =
                apiCoreRequests.makeGetRequest(URL.API_USER + userId,
                        APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN),
                        APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID));

        Assertions.assertJsonByName(responseUserData, ResponseConstant.FIRST_NAME, newName);
    }

    @Test
    @Description("Change user data while logged in as another user")
    @DisplayName("Edit another user")
    public void testEditAnotherUser() {
        Map<String, String> userDataForEdition = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.makePostRequest(URL.API_USER, userDataForEdition).jsonPath();
        String userIdForEdition = responseCreateAuth.getString(ResponseConstant.ID);

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put(ResponseConstant.FIRST_NAME, newName);

        Response responseEditUser =
                apiCoreRequests.makePutRequest(URL.API_USER + userIdForEdition,
                        editData,
                        APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN),
                        APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID));

        Assertions.assertJsonError(responseEditUser, "This user can only edit their own data.");
    }

    @Test
    @Description("Edit 'firstName' of a user, while logged in by the same user, to a very short value of one character")
    @DisplayName("Edit user name with small value")
    public void testEditUserNameWithSmallValue() {
        //EDIT
        String newName = DataGenerator.generateRandomString(1);
        Map<String, String> editData = new HashMap<>();
        editData.put(ResponseConstant.FIRST_NAME, newName);

        Response responseEditUser =
                apiCoreRequests.makePutRequest(URL.API_USER + userId,
                        editData,
                        APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN),
                        APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID));

        Assertions.assertJsonError(responseEditUser, "The value for field `firstName` is too short");

        //GET
        Response responseUserData =
                apiCoreRequests.makeGetRequest(URL.API_USER + userId,
                        APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN),
                        APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID));

        Assertions.assertJsonByName(responseUserData, ResponseConstant.FIRST_NAME, userName);
    }

    @Test
    @Description("Edit a user's email while logged in as the same user to a new email without the '@' symbol")
    @DisplayName("Edit user email with invalid value")
    public void testEditEmailUserWithInvalidValue() {
        //EDIT
        String newEmail = Constant.INVALID_EMAIL;
        Map<String, String> editData = new HashMap<>();
        editData.put(ResponseConstant.EMAIL, newEmail);

        Response responseEditUser =
                apiCoreRequests.makePutRequest(URL.API_USER + userId,
                        editData,
                        APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN),
                        APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID));

        Assertions.assertJsonError(responseEditUser, "Invalid email format");

        //GET
        Response responseUserData =
                apiCoreRequests.makeGetRequest(URL.API_USER + userId,
                        APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN),
                        APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID));

        Assertions.assertJsonByName(responseUserData, ResponseConstant.EMAIL, userEmail);
    }
}
