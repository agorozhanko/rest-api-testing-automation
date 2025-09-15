package example;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.constant.ResponseConstant;
import lib.constant.URL;
import lib.utility.APIUtility;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class CreationSimpleRequestsTest {

    @Test()
    public void testRestAssured() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "John");

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get(URL.API_HELLO)
                .jsonPath();

        String name = response.get("answer2");

        if (name == null) {
            System.out.println("The key 'answer2' is absent");
        } else {
            System.out.println(name);
        }
    }

    @Test()
    public void testRestAssuredParams() {
        Map<String, Object> body = new HashMap<>();
        body.put("param1", "value1");
        body.put("param2", "value2");

        Response response = RestAssured
                .given()
                .body(body)
                .post(URL.API_CHECK_TYPE)
                .andReturn();
        response.print();
    }

    @Test()
    public void testRestAssuredStatusCode200() {
        Response response = RestAssured
                .get(URL.API_CHECK_TYPE)
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }

    @Test()
    public void testRestAssuredStatusCode303() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get(URL.API_GET_303)
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }

    @Test()
    public void testRestAssuredStatusCode400() {
        Response response = RestAssured
                .get(URL.API_SOMETHING)
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }

    @Test()
    public void testRestAssuredStatusCode500() {
        Response response = RestAssured
                .get(URL.API_GET_500)
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }

    @Test()
    public void testRestAssuredShowHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("myHeader1", "myValue1");
        headers.put("myHeader2", "myValue12");

        Response response = RestAssured
                .given()
                .headers(headers)
                .when()
                .get(URL.API_SHOW_ALL_HEADERS)
                .andReturn();

        response.prettyPrint();

        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);
    }

    @Test()
    public void testRestAssuredCookies() {
        Map<String, String> data = new HashMap<>();
        data.put(ResponseConstant.LOGIN, ResponseConstant.SECRET_LOGIN);
        data.put(ResponseConstant.PASSWORD, ResponseConstant.SECRET_PASS);

        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post(URL.API_GET_AUTH_COOKIE)
                .andReturn();

        System.out.println("\nPretty text:");
        response.prettyPrint();

        System.out.println("\nHeaders:");
        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);

        System.out.println("\nCookies:");
        Map<String, String> responseCookies = response.getCookies();
        System.out.println(responseCookies);

        String responseCookie = APIUtility.getAuthCookie(response);
        System.out.println(responseCookie);
    }

    @Test()
    public void testRestAssuredWrongCookies() {
        Map<String, String> data = new HashMap<>();
        data.put(ResponseConstant.LOGIN, "secret_login2");
        data.put(ResponseConstant.PASSWORD, "secret_pass2");

        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post(URL.API_GET_AUTH_COOKIE)
                .andReturn();

        System.out.println("\nPretty text:");
        response.prettyPrint();

        System.out.println("\nHeaders:");
        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);

        System.out.println("\nCookies:");
        Map<String, String> responseCookies = response.getCookies();
        System.out.println(responseCookies);

//        String responseCookie = APIUtility.getAuthCookie(response);
//        System.out.println(responseCookie);
    }

    @Test()
    public void testRestAssuredCookiesForCheck() {
        Map<String, String> data = new HashMap<>();
        data.put(ResponseConstant.LOGIN, ResponseConstant.SECRET_LOGIN);
        data.put(ResponseConstant.PASSWORD, ResponseConstant.SECRET_PASS);

        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post(URL.API_GET_AUTH_COOKIE)
                .andReturn();

        String responseCookie = APIUtility.getAuthCookie(response);

        Map<String, String> cookies = new HashMap<>();
        if (responseCookie != null) {
            cookies.put(ResponseConstant.AUTH_COOKIE, responseCookie);
        }

        Response responseForCheck = RestAssured
                .given()
                .body(data)
                .cookies(cookies)
                .when()
                .post(URL.API_CHECK_AUTH_COOKIE)
                .andReturn();

        responseForCheck.print();
    }

}
