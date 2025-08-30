package utility;

import constant.Constant;
import exception.TooManyAttemptsException;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIUtility {
    public static int getStatusCode(Response response) {
        return response.getStatusCode();
    }

    public static String getLocation(Response response) {
        String location = response.getHeader(Constant.LOCATION);
        if (location == null) {
            throw new NullPointerException("Header 'Location' is NULL");
        }
        return location;
    }

    public static int getRedirectCount(String url, int redirectLimit) {
        Response response;
        String location;
        int redirectCount = 0;
        while (true) {
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();
            int statusCode = getStatusCode(response);

            if (statusCode == 200) {
                break;
            } else if (statusCode >= 300 && statusCode < 400) {
                location = getLocation(response);
                url = location;
                redirectCount++;
                if (redirectCount > redirectLimit) {
                    throw new TooManyAttemptsException("Redirects limit exceeded!");
                }
            }
        }
        return redirectCount;
    }

    public static String getAuthCookie(Response response) {
        return response.getCookie(Constant.AUTH_COOKIE);
    }
}
