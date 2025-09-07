package lib.utility;

public class StringUtility {
    public static boolean phraseLengthMoreThan15symbols(String phrase) {
        if (phrase != null) {
            return phrase.length() > 15;
        } else {
            throw new NullPointerException("Phrase is NULL!");
        }
    }
}
