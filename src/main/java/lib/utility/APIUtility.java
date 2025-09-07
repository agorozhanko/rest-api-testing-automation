package lib.utility;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import lib.exception.TooManyAttemptsException;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;

public class APIUtility {
    public static int getStatusCode(Response response) {
        return response.getStatusCode();
    }

    public static String getHeader(Response response, String name) {
        Headers headers = response.getHeaders();

        Assertions.assertTrue(headers.hasHeaderWithName(name), String.format("Response doesn't have header with name: '%s'", name));
        return headers.getValue(name);
    }

    public static String getHeaderLocation(Response response) {
        return getHeader(response, lib.constant.Response.LOCATION);
    }

    public static String getCookie(Response response, String name) {
        Map<String, String> cookies = response.getCookies();

        Assertions.assertTrue(cookies.containsKey(name), String.format("Response doesn't have cookie with name: '%s'", name));
        return cookies.get(name);
    }

    public static String getAuthCookie(Response response) {
        return getCookie(response, lib.constant.Response.AUTH_COOKIE);
    }

    public static int getIntFromJson(Response response, String name) {
        response.then().assertThat().body("$", hasKey(name));
        return response.jsonPath().getInt(name);
    }

    public static int getUserIdFromJson(Response response) {
        return getIntFromJson(response, "user_id");
    }

    public static int getRedirectCount(String url, int redirectLimit) {
        Response response;
        String location;
        int redirectCount = 0;
        while (true) {
            response = RestAssured.given().redirects().follow(false).when().get(url).andReturn();
            int statusCode = getStatusCode(response);

            if (statusCode == 200) {
                break;
            } else if (statusCode >= 300 && statusCode < 400) {
                location = getHeaderLocation(response);
                url = location;
                redirectCount++;
                if (redirectCount > redirectLimit) {
                    throw new TooManyAttemptsException("Redirects limit exceeded!");
                }
            }
        }
        return redirectCount;
    }
}
