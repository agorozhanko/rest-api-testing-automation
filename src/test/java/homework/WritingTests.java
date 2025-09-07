package homework;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.constant.URL;
import lib.utility.APIUtility;
import lib.utility.StringUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class WritingTests {

    @ParameterizedTest
    @ValueSource(strings = {"Phrase with more than 15 symbols", "                ", "!@#$%^&*()_+[]||"})
    public void testShortPhraseMoreThan15symbols(String phrase) {
        Assertions.assertTrue(StringUtility.phraseLengthMoreThan15symbols(phrase),
                " Phrase length must be more than 15 symbols");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Phrase", "               ", "!@#$%^&*"})
    public void testShortPhraseLessThan15symbols(String phrase) {
        Assertions.assertFalse(StringUtility.phraseLengthMoreThan15symbols(phrase),
                " Phrase length must be less than 15 symbols");
    }

    @Test()
    public void testShortPhraseNull() {
        Assertions.assertThrows(NullPointerException.class, () -> StringUtility.phraseLengthMoreThan15symbols(null));
    }

    @Test
    public void testCookieValue() {
        Response response = RestAssured
                .get(URL.API_HOMEWORK_COOKIE)
                .andReturn();

        String actualCookieValue = APIUtility.getCookieHomeWork(response);
        Assertions.assertEquals("hw_value", actualCookieValue,
                "Значение cookie 'HomeWork' не совпадает с ожидаемым");
    }

    @Test
    public void testResponseHeader() {
        Response response = RestAssured
                .get(URL.API_HOMEWORK_HEADER)
                .andReturn();

        String actualHeaderValue = APIUtility.getHeaderXSecretHomeworkHeader(response);
        Assertions.assertEquals("Some secret value", actualHeaderValue,
                "Значение заголовка 'x-secret-homework-header' не совпадает с ожидаемым");
    }

}
