package homework.edition;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.assertions.Assertions;
import lib.constant.Constant;
import lib.constant.ResponseConstant;
import lib.constant.URL;
import lib.utility.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Edition cases")
@Feature("Edition unauthorized user")
public class UnauthorizedUserEditTest extends BaseTest {

    private Map<String, String> userData;
    private String userId;

    @BeforeEach
    public void createUser() {
        //GENERATE USER
        userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.makePostRequest(URL.API_USER, userData).jsonPath();
        userId = responseCreateAuth.getString(ResponseConstant.ID);
    }

    @Test
    @Tag("Edition")
    @Description("Edit user data without authorization.")
    @DisplayName("Edit unauthorized user")
    @Issue("Ex17: Негативные тесты на PUT")
    public void testEditUnauthorizedUser() {
        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put(ResponseConstant.FIRST_NAME, newName);

        Response responseEditUser =
                apiCoreRequests.makePutRequest(URL.API_USER + userId, editData);

        //GET
        Response responseUserData =
                apiCoreRequests.makeGetRequest(URL.API_USER + userId);

        Assertions.assertJsonByName(responseUserData, ResponseConstant.USERNAME, userData.get(ResponseConstant.USERNAME));
        Assertions.assertJsonHasNotFields(responseUserData, Constant.unexpectedFields);
    }
}
