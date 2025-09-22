package lib.core;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import lib.constant.ResponseConstant;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET-request {0}.")
    public Response makeGetRequest(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token and auth cookie.")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header(ResponseConstant.X_CSRF_TOKEN, token))
                .cookie(ResponseConstant.AUTH_SID, cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with auth cookie only.")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie(ResponseConstant.AUTH_SID, cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token only.")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header(ResponseConstant.X_CSRF_TOKEN, token))
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request.")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a PUT-request with body, token and auth cookie.")
    public Response makePutRequest(String url, Map<String, String> editData, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header(ResponseConstant.X_CSRF_TOKEN, token))
                .cookie(ResponseConstant.AUTH_SID, cookie)
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make a PUT-request with body.")
    public Response makePutRequest(String url, Map<String, String> editData) {
        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make a PUT-request with token and auth cookie.")
    public Response makeDeleteRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header(ResponseConstant.X_CSRF_TOKEN, token))
                .cookie(ResponseConstant.AUTH_SID, cookie)
                .delete(url)
                .andReturn();
    }
}
