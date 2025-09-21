package homework;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.assertions.Assertions;
import lib.constant.Constant;
import lib.constant.ResponseConstant;
import lib.constant.URL;
import lib.utility.APIUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Get user cases")
@Feature("Get User")
public class UserGetTest extends BaseTest {

    private final String[] unexpectedFields = {ResponseConstant.FIRST_NAME, ResponseConstant.LAST_NAME, ResponseConstant.EMAIL};

    @Test
    @DisplayName("Get user data no auth")
    @Description("Unauthorized request for data")
    public void testGetUserDataNoAuth() {
        Response responseUserData = apiCoreRequests.makeGetRequest(URL.HTTPS_PLAYGROUND_LEARNQA_RU_API_USER_2);

        Assertions.assertJsonHasField(responseUserData, ResponseConstant.USERNAME);
        Assertions.assertJsonHasNotFields(responseUserData, unexpectedFields);
    }

    @Test
    @DisplayName("Get user details from the same user")
    @Description("Authorization by user with ID = 2 and a request to obtain data from the same user")
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put(ResponseConstant.EMAIL, Constant.EMAIL_VINKOTOV);
        authData.put(ResponseConstant.PASSWORD, Constant.TEST_PASSWORD);

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL.API_USER_LOGIN, authData);

        String header = APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN);
        String cookie = APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID);

        Response responseUserData =
                apiCoreRequests.makeGetRequest(URL.HTTPS_PLAYGROUND_LEARNQA_RU_API_USER_2, header, cookie);

        String[] expectedFields = {ResponseConstant.USERNAME, ResponseConstant.FIRST_NAME,
                ResponseConstant.LAST_NAME, ResponseConstant.EMAIL};

        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @Test
    @DisplayName("Get user details from the another user")
    @Description("Authorization by user with ID = 2 and a request to obtain data from user with ID = 1")
    public void testGetUserDetailsAuthAsAnotherUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put(ResponseConstant.EMAIL, Constant.EMAIL_VINKOTOV);
        authData.put(ResponseConstant.PASSWORD, Constant.TEST_PASSWORD);

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL.API_USER_LOGIN, authData);

        String header = APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN);
        String cookie = APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID);

        Response responseUserData =
                apiCoreRequests.makeGetRequest(String.format(URL.API_USER + 1), header, cookie);

        Assertions.assertJsonHasField(responseUserData, ResponseConstant.USERNAME);
        Assertions.assertJsonHasNotFields(responseUserData, unexpectedFields);
    }
}
