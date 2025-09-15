package example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.assertions.Assertions;
import lib.constant.Constant;
import lib.constant.ResponseConstant;
import lib.constant.URL;
import lib.utility.APIUtility;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest {

    @Test
    public void testGetUserDataNoAuth() {
        Response responseUserData = RestAssured
                .get(URL.HTTPS_PLAYGROUND_LEARNQA_RU_API_USER_2)
                .andReturn();
        Assertions.assertJsonHasField(responseUserData, ResponseConstant.USERNAME);
        Assertions.assertJsonHasNotField(responseUserData, ResponseConstant.FIRST_NAME);
        Assertions.assertJsonHasNotField(responseUserData, ResponseConstant.LAST_NAME);
        Assertions.assertJsonHasNotField(responseUserData, ResponseConstant.EMAIL);
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put(ResponseConstant.EMAIL, Constant.EMAIL_VINKOTOV);
        authData.put(ResponseConstant.PASSWORD, Constant.TEST_PASSWORD);

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post(URL.API_USER_LOGIN)
                .andReturn();

        String header = APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN);
        String cookie = APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID);


        Response responseUserData = RestAssured
                .given()
                .header(ResponseConstant.X_CSRF_TOKEN, header)
                .cookie(ResponseConstant.AUTH_SID, cookie)
                .get(URL.HTTPS_PLAYGROUND_LEARNQA_RU_API_USER_2)
                .andReturn();

        String[] expectedFields = {ResponseConstant.USERNAME, ResponseConstant.FIRST_NAME,
                ResponseConstant.LAST_NAME, ResponseConstant.EMAIL};

        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }
}
