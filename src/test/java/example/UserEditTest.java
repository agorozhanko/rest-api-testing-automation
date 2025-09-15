package example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.assertions.Assertions;
import lib.constant.ResponseConstant;
import lib.constant.URL;
import lib.utility.APIUtility;
import lib.utility.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest {

    @Test
    public void testEditJustCreatedTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post(URL.API_USER)
                .jsonPath();

        String userId = responseCreateAuth.getString(ResponseConstant.ID);

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put(ResponseConstant.EMAIL, userData.get(ResponseConstant.EMAIL));
        authData.put(ResponseConstant.PASSWORD, userData.get(ResponseConstant.PASSWORD));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post(URL.API_USER_LOGIN)
                .andReturn();

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put(ResponseConstant.FIRST_NAME, newName);

        Response responseEditUser = RestAssured
                .given()
                .header(ResponseConstant.X_CSRF_TOKEN, APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN))
                .cookies(ResponseConstant.AUTH_SID, APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID))
                .body(editData)
                .put(URL.API_USER + userId)
                .andReturn();

        //GET
        Response responseUserData = RestAssured
                .given()
                .header(ResponseConstant.X_CSRF_TOKEN, APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN))
                .cookie(ResponseConstant.AUTH_SID, APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID))
                .get(URL.API_USER + userId)
                .andReturn();
        Assertions.assertJsonByName(responseUserData, ResponseConstant.FIRST_NAME, newName);
    }
}
