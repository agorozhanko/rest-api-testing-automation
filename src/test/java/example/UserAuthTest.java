package example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.assertions.Assertions;
import lib.constant.Constant;
import lib.constant.ResponseConstant;
import lib.constant.URL;
import lib.utility.APIUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserAuthTest {
    String cookie;
    String header;
    int userIdOnAuth;

    @BeforeEach
    public void loginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put(ResponseConstant.EMAIL, Constant.EMAIL_VINKOTOV);
        authData.put(ResponseConstant.PASSWORD, Constant.TEST_PASSWORD);

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post(URL.API_USER_LOGIN)
                .andReturn();

        cookie = APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID);
        header = APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN);
        userIdOnAuth = APIUtility.getUserIdFromJson(responseGetAuth);
    }

    @Test
    public void testAuthUser() {
        Response responseCheckAuth = RestAssured
                .given()
                .header(ResponseConstant.X_CSRF_TOKEN, header)
                .cookie(ResponseConstant.AUTH_SID, cookie)
                .get(URL.API_USER_AUTH)
                .andReturn();

        Assertions.assertJsonByName(responseCheckAuth, ResponseConstant.USER_ID, userIdOnAuth);
    }

    @ParameterizedTest
    @ValueSource(strings = {ResponseConstant.COOKIE, ResponseConstant.HEADERS})
    public void testNegativeAuthUser(String condition) {
        RequestSpecification spec = RestAssured.given();
        spec.baseUri(URL.API_USER_AUTH);

        if (condition.equals(ResponseConstant.COOKIE)) {
            spec.cookie(ResponseConstant.AUTH_SID, cookie);
        } else if (condition.equals(ResponseConstant.HEADERS)) {
            spec.header(ResponseConstant.X_CSRF_TOKEN, header);
        } else {
            throw new IllegalArgumentException(String.format("Condition value is known: '%s'", condition));
        }

        Response responseForCheck = spec.get().andReturn();
        Assertions.assertJsonByName(responseForCheck, ResponseConstant.USER_ID, 0);
    }
}
