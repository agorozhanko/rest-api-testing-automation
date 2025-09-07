package example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.assertions.BaseAssertions;
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
        authData.put(lib.constant.Response.EMAIL, "vinkotov@example.com");
        authData.put(lib.constant.Response.PASSWORD, "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post(URL.API_USER_LOGIN)
                .andReturn();

        cookie = APIUtility.getCookie(responseGetAuth, lib.constant.Response.AUTH_SID);
        header = APIUtility.getHeader(responseGetAuth, lib.constant.Response.X_CSRF_TOKEN);
        userIdOnAuth = APIUtility.getUserIdFromJson(responseGetAuth);
    }

    @Test
    public void testAuthUser() {
        Response responseCheckAuth = RestAssured
                .given()
                .header(lib.constant.Response.X_CSRF_TOKEN, header)
                .cookie(lib.constant.Response.AUTH_SID, cookie)
                .get(URL.API_USER_AUTH)
                .andReturn();

        BaseAssertions.assertJsonByName(responseCheckAuth, lib.constant.Response.USER_ID, userIdOnAuth);
    }

    @ParameterizedTest
    @ValueSource(strings = {lib.constant.Response.COOKIE, lib.constant.Response.HEADERS})
    public void testNegativeAuthUser(String condition) {
        RequestSpecification spec = RestAssured.given();
        spec.baseUri(URL.API_USER_AUTH);

        if (condition.equals(lib.constant.Response.COOKIE)) {
            spec.cookie(lib.constant.Response.AUTH_SID, cookie);
        } else if (condition.equals(lib.constant.Response.HEADERS)) {
            spec.header(lib.constant.Response.X_CSRF_TOKEN, header);
        } else {
            throw new IllegalArgumentException(String.format("Condition value is known: '%s'", condition));
        }

        Response responseForCheck = spec.get().andReturn();
        BaseAssertions.assertJsonByName(responseForCheck, lib.constant.Response.USER_ID, 0);
    }
}
