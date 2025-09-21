package homework;

import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.constant.Constant;
import lib.constant.ResponseConstant;
import lib.constant.URL;
import lib.utility.APIUtility;
import lib.utility.JobUtility;
import lib.utility.PasswordUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreationSimpleRequestsTest {

    @Test()
    @Tag("SimpleRequests")
    @Severity(SeverityLevel.MINOR)
    @Issue("Ex5: Парсинг JSON")
    public void testJsonParsing() {
        JsonPath response = RestAssured
                .get(URL.API_GET_JSON_HOMEWORK)
                .jsonPath();

        List<Object> messagesList = response.getList(ResponseConstant.MESSAGES);
        System.out.println(messagesList.get(1));
    }

    @Test()
    @Tag("SimpleRequests")
    @Issue("Ex6: Редирект")
    public void testRedirect() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get(URL.API_LONG_REDIRECT)
                .andReturn();

        Header location = response.getHeaders().get(ResponseConstant.LOCATION);
        System.out.println(location);
    }

    @Test()
    @Tag("SimpleRequests")
    @Issue("Ex7: Долгий редирект")
    public void testLongRedirect() {
        String url = URL.API_LONG_REDIRECT;
        int redirectLimit = 35;
        int redirectCount = APIUtility.getRedirectCount(url, redirectLimit);

        System.out.println(redirectCount);
    }

    @Test()
    @Tag("SimpleRequests")
    @Issue("Ex8: Токены")
    public void testTokens() {
        JsonPath createTaskResponse = RestAssured
                .get(URL.API_LONGTIME_JOB)
                .jsonPath();

        String token = JobUtility.getToken(createTaskResponse);
        int seconds = JobUtility.workTimeCompletion(createTaskResponse);

        Map<String, String> params = new HashMap<>();
        params.put(ResponseConstant.TOKEN, token);

        JsonPath jobProgressResponse = RestAssured
                .given()
                .queryParams(params)
                .when()
                .get(URL.API_LONGTIME_JOB)
                .jsonPath();

        String responseError = JobUtility.getError(jobProgressResponse);

        JobUtility.checkError(responseError);
        String responseStatus = JobUtility.getStatus(jobProgressResponse);
        JobUtility.waitForJobCompletion(responseStatus, seconds);

        JsonPath jobReadyResponse = RestAssured
                .given()
                .queryParams(params)
                .when()
                .get(URL.API_LONGTIME_JOB)
                .jsonPath();

        String jobReadyResponseResult = JobUtility.getResult(jobReadyResponse);
        String jobReadyResponseStatus = JobUtility.getStatus(jobReadyResponse);

        Assertions.assertEquals(Constant.READY_RESPONSE_RESULT, jobReadyResponseResult);
        Assertions.assertEquals(Constant.JOB_IS_READY, jobReadyResponseStatus);
    }

    @Test
    @Tag("SimpleRequests")
    @Issue("Ex9: Подбор пароля")
    public void testFindPassword() {
        String login = "super_admin";

        List<String> passwordsList = Arrays.asList(
                "123456", "password", "123456789", "12345", "12345678", "qwerty", "1234567", "111111",
                "123123", "abc123", "password1", "1234", "qwertyuiop", "123", "iloveyou", "admin", "welcome",
                "monkey", "login", "princess", "qwerty123", "solo", "starwars", "passw0rd", "trustno1"
        );

        String correctPassword = PasswordUtility.passwordSelection(login, passwordsList);
        System.out.println("Найден пароль: " + correctPassword);
    }

}
