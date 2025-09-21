package example;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.core.ApiCoreRequests;
import lib.assertions.Assertions;
import lib.constant.Constant;
import lib.constant.ResponseConstant;
import lib.constant.URL;
import lib.utility.APIUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Authorisation cases")
@Feature("Authorisation")
public class UserAuthTest {
    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach
    public void loginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put(ResponseConstant.EMAIL, Constant.EMAIL_VINKOTOV);
        authData.put(ResponseConstant.PASSWORD, Constant.TEST_PASSWORD);

        Response responseGetAuth = apiCoreRequests.makePostRequest(URL.API_USER_LOGIN, authData);

        cookie = APIUtility.getCookie(responseGetAuth, ResponseConstant.AUTH_SID);
        header = APIUtility.getHeader(responseGetAuth, ResponseConstant.X_CSRF_TOKEN);
        userIdOnAuth = APIUtility.getUserIdFromJson(responseGetAuth);
    }

    @Test
    @Tag("Lessons")
    @Severity(SeverityLevel.MINOR)
    @Description("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    @Issue("Ex14: Формирование фреймворка")
    public void testAuthUser() {
        Response responseCheckAuth = apiCoreRequests.makeGetRequest(URL.API_USER_AUTH, header, cookie);

        Assertions.assertJsonByName(responseCheckAuth, ResponseConstant.USER_ID, userIdOnAuth);
    }

    @Description("This test check authorization status w/o sending auth cookie or token")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {ResponseConstant.COOKIE, ResponseConstant.HEADERS})
    @Tag("Lessons")
    @Severity(SeverityLevel.MINOR)
    @Issue("Ex14: Формирование фреймворка")
    public void testNegativeAuthUser(String condition) {
        if (condition.equals(ResponseConstant.COOKIE)) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(URL.API_USER_AUTH, cookie);
            Assertions.assertJsonByName(responseForCheck, ResponseConstant.USER_ID, 0);
        } else if (condition.equals(ResponseConstant.HEADERS)) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(URL.API_USER_AUTH, header);
            Assertions.assertJsonByName(responseForCheck, ResponseConstant.USER_ID, 0);
        } else {
            throw new IllegalArgumentException(String.format("Condition value is known: '%s'", condition));
        }
    }
}
