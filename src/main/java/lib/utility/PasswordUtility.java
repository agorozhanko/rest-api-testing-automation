package lib.utility;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.constant.Constant;
import lib.constant.ResponseConstant;
import lib.constant.URL;
import lib.exception.PasswordNotFoundException;

import java.util.List;

public class PasswordUtility {
    public static String passwordSelection(String login, List<String> passwordsList) {

        for (String password : passwordsList) {
            Response getSecretPasswordResponse = RestAssured
                    .given()
                    .param(ResponseConstant.LOGIN, login)
                    .param(ResponseConstant.PASSWORD, password)
                    .when()
                    .post(URL.API_GET_SECRET_PASSWORD_HOMEWORK)
                    .andReturn();

            String authCookie = APIUtility.getAuthCookie(getSecretPasswordResponse);

            Response checkAuthCookieResponse = RestAssured
                    .given()
                    .cookie(ResponseConstant.AUTH_COOKIE, authCookie)
                    .when()
                    .post(URL.API_CHECK_AUTH_COOKIE)
                    .andReturn();

            String authStatus = checkAuthCookieResponse.asString();

            if (authStatus.equals(Constant.YOU_ARE_AUTHORIZED)) {
                return password;
            }
        }
        throw new PasswordNotFoundException();
    }
}
