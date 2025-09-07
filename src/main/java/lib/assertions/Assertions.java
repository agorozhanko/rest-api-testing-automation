package lib.assertions;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;

public class Assertions {
    public static void assertJsonByName(Response response, String name, int expectedValue) {
        response.then().assertThat().body("$", hasKey(name));

        int value = response.jsonPath().getInt(name);
        org.junit.jupiter.api.Assertions.assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

}
