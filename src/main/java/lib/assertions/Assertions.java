package lib.assertions;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

public class Assertions extends org.junit.jupiter.api.Assertions {
    public static void assertJsonByName(Response response, String name, int expectedValue) {
        response.then().assertThat().body("$", hasKey(name));

        int value = response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertJsonByName(Response response, String name, String expectedValue) {
        response.then().assertThat().body("$", hasKey(name));

        String value = response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    @Step("Check error. Text must be: {1}.")
    public static void assertJsonError(Response response, String expectedValue) {
        assertJsonByName(response, "error", expectedValue);
    }

    public static void assertResponseTextEquals(Response response, String expectedAnswer) {
        assertEquals(
                expectedAnswer,
                response.asString(),
                "Response text is not as expected"
        );
    }

    public static void assertResponseCodeEquals(Response response, int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                response.statusCode(),
                "Response status code is not as expected"
        );
    }

    @Step("Check response code is 200.")
    public static void assertResponseCode200(Response response) {
        assertResponseCodeEquals(response, 200);
    }

    @Step("Check response code is 400.")
    public static void assertResponseCode400(Response response) {
        assertResponseCodeEquals(response, 400);
    }

    @Step("Response has field: {1}.")
    public static void assertJsonHasField(Response response, String expectedFieldName) {
        response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    @Step("Response has fields: {1}.")
    public static void assertJsonHasFields(Response response, String... expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames)
            assertJsonHasField(response, expectedFieldName);
    }

    @Step("Response has not field: {1}.")
    public static void assertJsonHasNotField(Response response, String unexpectedFieldName) {
        response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    @Step("Response has not fields: {1}.")
    public static void assertJsonHasNotFields(Response response, String... unexpectedFieldNames) {
        for (String unexpectedFieldName : unexpectedFieldNames)
            assertJsonHasNotField(response, unexpectedFieldName);
    }

}
