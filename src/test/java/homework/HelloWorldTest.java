package homework;

import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.constant.URL;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    @Test()
    @Tag("hello world")
    @Severity(SeverityLevel.TRIVIAL)
    public void testHelloWorld() {
        System.out.println("Hello from Andrei Gorozhanko");
    }

    @Test
    @Tag("hello world")
    @Severity(SeverityLevel.TRIVIAL)
    @Issue("Ex4: GET-запрос")
    public void testGetText() {
        Response response = RestAssured
                .get(URL.API_GET_TEXT)
                .andReturn();
        response.prettyPrint();
    }
}
