package homework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.constant.URL;
import lib.enumeration.Browser;
import lib.enumeration.Device;
import lib.enumeration.Platform;
import lib.model.UserAgentHeader;
import lib.utility.APIUtility;
import lib.utility.UserAgentUtility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class UserAgentTest {
    private static final List<String> errors = new ArrayList<>();

    @ParameterizedTest
    @MethodSource("userAgentProvider")
    public void testUserAgent(UserAgentHeader expectedUserAgentHeader) {
        Response response = RestAssured
                .given()
                .header(lib.constant.Response.USER_AGENT, expectedUserAgentHeader.getUserAgent())
                .get(URL.API_USER_AGENT_CHECK)
                .andReturn();

        String actualPlatform = APIUtility.getPlatformFromJson(response);
        String actualBrowser = APIUtility.getBrowserFromJson(response);
        String actualDevice = APIUtility.getDeviceFromJson(response);

        UserAgentHeader actualUserAgentHeader = new UserAgentHeader(actualPlatform, actualBrowser, actualDevice);

        UserAgentUtility.generateErrorText(expectedUserAgentHeader, actualUserAgentHeader, errors);

//        Assertions.assertEquals(expectedUserAgentHeader.getExpectedPlatform(), actualPlatform, "Platform не совпадает");
//        Assertions.assertEquals(expectedUserAgentHeader.getExpectedBrowser(), actualBrowser, "Browser не совпадает");
//        Assertions.assertEquals(expectedUserAgentHeader.getExpectedDevice(), actualDevice, "Device не совпадает");
    }

    public static Stream<UserAgentHeader> userAgentProvider() {
        return Stream.of(
                new UserAgentHeader(
                        "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
                        Platform.MOBILE.getText(), Browser.NO.getText(), Device.ANDROID.getText()
                ),
                new UserAgentHeader(
                        "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
                        Platform.MOBILE.getText(), Browser.CHROME.getText(), Device.IOS.getText()
                ),
                new UserAgentHeader(
                        "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                        Platform.GOOGLEBOT.getText(), Browser.UNKNOWN.getText(), Device.UNKNOWN.getText()
                ),
                new UserAgentHeader(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                        Platform.WEB.getText(), Browser.CHROME.getText(), Device.NO.getText()
                ),
                new UserAgentHeader(
                        "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
                        Platform.MOBILE.getText(), Browser.NO.getText(), Device.IPHONE.getText()
                )
        );
    }

    @AfterAll
    public static void printErrors() {
        UserAgentUtility.printError(errors);
    }

}
