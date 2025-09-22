package lib.assertions;

import io.qameta.allure.Step;

public class StringAssertions {

    @Step("Check phrase Length More Than 15 symbols.")
    public static boolean phraseLengthMoreThan15symbols(String phrase) {
        if (phrase != null) {
            return phrase.length() > 15;
        } else {
            throw new NullPointerException("Phrase is NULL!");
        }
    }
}
