package lib.utility;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import lib.constant.ResponseConstant;
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
        return getHeader(response, ResponseConstant.LOCATION);
    }

    public static String getHeaderXSecretHomeworkHeader(Response response) {
        return getHeader(response, ResponseConstant.X_SECRET_HOMEWORK_HEADER);
    }

    public static String getCookie(Response response, String name) {
        Map<String, String> cookies = response.getCookies();

        Assertions.assertTrue(cookies.containsKey(name), String.format("Response doesn't have cookie with name: '%s'", name));
        return cookies.get(name);
    }

    public static String getAuthCookie(Response response) {
        return getCookie(response, ResponseConstant.AUTH_COOKIE);
    }

    public static String getCookieHomeWork(Response response) {
        return getCookie(response, ResponseConstant.HOME_WORK);
    }

    public static int getIntFromJson(Response response, String name) {
        response.then().assertThat().body("$", hasKey(name));
        return response.jsonPath().getInt(name);
    }

    public static int getUserIdFromJson(Response response) {
        return getIntFromJson(response, ResponseConstant.USER_ID);
    }

    public static String getStringFromJson(Response response, String name) {
        response.then().assertThat().body("$", hasKey(name));
        return response.jsonPath().getString(name);
    }

    public static String getPlatformFromJson(Response response) {
        return getStringFromJson(response, ResponseConstant.PLATFORM);
    }

    public static String getBrowserFromJson(Response response) {
        return getStringFromJson(response, ResponseConstant.BROWSER);
    }

    public static String getDeviceFromJson(Response response) {
        return getStringFromJson(response, ResponseConstant.DEVICE);
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
            } else {
                throw new IllegalStateException("Something went wrong. Unexpected Status Code");
            }
        }
        return redirectCount;
    }
}
