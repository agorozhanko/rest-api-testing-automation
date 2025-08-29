package homework;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

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
}
