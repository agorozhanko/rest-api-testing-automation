package homework;

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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Deletion cases")
@Feature("Deletion user")
public class UserDeleteTest extends BaseTest {
    private static final Map<String, String> authDataUser2 = new HashMap<>();
    private static Map<String, String> userDataForDeletion = new HashMap<>();
    private static String userIdForDeletion;

    @BeforeAll
    static void prepareUserData() {
        String user2email = "vinkotov@example.com";
        String user2password = "1234";
        authDataUser2.put(ResponseConstant.EMAIL, user2email);
        authDataUser2.put(ResponseConstant.PASSWORD, user2password);

        userDataForDeletion = DataGenerator.getRegistrationData();
    }

    @AfterAll
    static void deletionUser() {
        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put(ResponseConstant.EMAIL, userDataForDeletion.get(ResponseConstant.EMAIL));
        authData.put(ResponseConstant.PASSWORD, userDataForDeletion.get(ResponseConstant.PASSWORD));

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL.API_USER_LOGIN, authData);

        //DELETE
        Response responseDeleteUser =
                apiCoreRequests.makeDeleteRequest(URL.API_USER + userIdForDeletion,
                        APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN),
                        APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID));
    }

    @Test
    @Description("Try to delete user by ID 2. Make sure that the system will not allow you to delete this user.")
    @DisplayName("Delete undeletable user")
    public void deleteUserWithId2() {
        Response responseGetAuth = apiCoreRequests.makePostRequest(URL.API_USER_LOGIN, authDataUser2);

        Response responseDeleteUser =
                apiCoreRequests.makeDeleteRequest(URL.API_USER + Constant.USER_ID_2,
                        APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN),
                        APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID));

        Assertions.assertJsonError(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description("Create a user. Log in as them. Delete them, then try to get their data by ID " +
            "and make sure that the user has actually been deleted.")
    @DisplayName("Delete user")
    public void deleteUser() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.makePostRequest(URL.API_USER, userData).jsonPath();
        String userId = responseCreateAuth.getString(ResponseConstant.ID);

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put(ResponseConstant.EMAIL, userData.get(ResponseConstant.EMAIL));
        authData.put(ResponseConstant.PASSWORD, userData.get(ResponseConstant.PASSWORD));

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL.API_USER_LOGIN, authData);

        //DELETE
        Response responseDeleteUser =
                apiCoreRequests.makeDeleteRequest(URL.API_USER + userId,
                        APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN),
                        APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID));
        Assertions.assertJsonByName(responseDeleteUser, "success", "!");

        //GET
        Response responseUserData =
                apiCoreRequests.makeGetRequest(URL.API_USER + userId,
                        APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN),
                        APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID));
        Assertions.assertResponseTextEquals(responseUserData, "User not found");
    }

    @Test
    @Description("Try to delete a user while being logged in as another user.")
    @DisplayName("Delete another user")
    public void deleteAnotherUser() {
        //GENERATE USER
        JsonPath responseCreateAuth = apiCoreRequests.makePostRequest(URL.API_USER, userDataForDeletion).jsonPath();
        userIdForDeletion = responseCreateAuth.getString(ResponseConstant.ID);

        //LOGIN
        Response responseGetAuth = apiCoreRequests.makePostRequest(URL.API_USER_LOGIN, authDataUser2);

        //DELETE
        Response responseDeleteUser =
                apiCoreRequests.makeDeleteRequest(URL.API_USER + userIdForDeletion,
                        APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN),
                        APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID));
        responseDeleteUser.prettyPrint();
        Assertions.assertJsonError(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }
}
