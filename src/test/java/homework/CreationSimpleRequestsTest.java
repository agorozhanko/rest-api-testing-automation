package homework;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import utility.APIUtility;

import java.util.List;

public class CreationSimpleRequestsTest {

    @Test()
    public void testJsonParsing() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        List<Object> messagesList = response.getList("messages");
        System.out.println(messagesList.get(1));
    }

    @Test()
    public void testRedirect() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        Header location = response.getHeaders().get("Location");
        System.out.println(location);
    }

    @Test()
    public void testLongRedirect() {
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int redirectLimit = 35;
        int redirectCount = APIUtility.getRedirectCount(url, redirectLimit);

        System.out.println(redirectCount);

    }

}
