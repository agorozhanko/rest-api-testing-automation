package lib.assertions;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.Matchers.hasKey;

public class BaseAssertions {
    public static void assertJsonByName(Response response, String name, int expectedValue) {
        response.then().assertThat().body("$", hasKey(name));

        int value = response.jsonPath().getInt(name);
        Assertions.assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }
}
