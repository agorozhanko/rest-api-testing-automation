package homework;

import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.assertions.StringAssertions;
import lib.constant.URL;
import lib.utility.APIUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class WritingTests {

    @ParameterizedTest
    @ValueSource(strings = {"Phrase with more than 15 symbols", "                ", "!@#$%^&*()_+[]||"})
    @Tag("Phrase")
    @Severity(SeverityLevel.MINOR)
    @Issue("Ex10: Тест на короткую фразу")
    public void testShortPhraseMoreThan15symbols(String phrase) {
        Assertions.assertTrue(StringAssertions.phraseLengthMoreThan15symbols(phrase),
                " Phrase length must be more than 15 symbols");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Phrase", "               ", "!@#$%^&*"})
    @Tag("Phrase")
    @Severity(SeverityLevel.MINOR)
    @Issue("Ex10: Тест на короткую фразу")
    public void testShortPhraseLessThan15symbols(String phrase) {
        Assertions.assertFalse(StringAssertions.phraseLengthMoreThan15symbols(phrase),
                " Phrase length must be less than 15 symbols");
    }

    @Test()
    @Tag("Phrase")
    @Severity(SeverityLevel.MINOR)
    @Issue("Ex10: Тест на короткую фразу")
    public void testShortPhraseNull() {
        Assertions.assertThrows(NullPointerException.class, () -> StringAssertions.phraseLengthMoreThan15symbols(null));
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Issue("Ex11: Тест запроса на метод cookie")
    public void testCookieValue() {
        Response response = RestAssured
                .get(URL.API_HOMEWORK_COOKIE)
                .andReturn();

        String actualCookieValue = APIUtility.getCookieHomeWork(response);
        Assertions.assertEquals("hw_value", actualCookieValue,
                "Значение cookie 'HomeWork' не совпадает с ожидаемым");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Issue("Ex12: Тест запроса на метод header")
    public void testResponseHeader() {
        Response response = RestAssured
                .get(URL.API_HOMEWORK_HEADER)
                .andReturn();

        String actualHeaderValue = APIUtility.getHeaderXSecretHomeworkHeader(response);
        Assertions.assertEquals("Some secret value", actualHeaderValue,
                "Значение заголовка 'x-secret-homework-header' не совпадает с ожидаемым");
    }

}
