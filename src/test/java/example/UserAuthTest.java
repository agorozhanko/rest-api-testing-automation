package example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.constant.URL;
import lib.utility.APIUtility;
import org.junit.jupiter.api.Assertions;
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
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post(URL.API_USER_LOGIN)
                .andReturn();

        cookie = APIUtility.getCookie(responseGetAuth, "auth_sid");
        header = APIUtility.getHeader(responseGetAuth, "x-csrf-token");
        userIdOnAuth = APIUtility.getUserIdFromJson(responseGetAuth);
    }

    @Test
    public void testAuthUser() {
        JsonPath responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get(URL.API_USER_AUTH)
                .jsonPath();

        int userIdOnCheck = responseCheckAuth.getInt("user_id");
        Assertions.assertTrue(userIdOnCheck > 0, String.format("Unexpected user id: '%s'", userIdOnCheck));
        Assertions.assertEquals(userIdOnAuth, userIdOnCheck,
                "User id from auth request is not equal to user_id from check request");
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition) {
        RequestSpecification spec = RestAssured.given();
        spec.baseUri(URL.API_USER_AUTH);

        if (condition.equals("cookie")) {
            spec.cookie("auth_sid", cookie);
        } else if (condition.equals("headers")) {
            spec.header("x-csrf-token", header);
        } else {
            throw new IllegalArgumentException(String.format("Condition value is known: '%s'", condition));
        }

        JsonPath responseForCheck = spec.get().jsonPath();
        Assertions.assertEquals(0, responseForCheck.getInt("user_id"),
                "user_id should be 0 for unauth request");
    }
}
