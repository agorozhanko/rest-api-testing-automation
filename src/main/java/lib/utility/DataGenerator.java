package lib.utility;

import io.qameta.allure.Step;
import lib.constant.Constant;
import lib.constant.ResponseConstant;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DataGenerator {

    public static String getRandomEmail() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return String.format("learnqa%s@example.com", timestamp);
    }

    @Step("Generate random string with length {0}")
    public static String generateRandomString(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();

        if (length < 0) {
            throw new IllegalArgumentException("String length must be > 0");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }


    public static Map<String, String> getRegistrationData() {
        Map<String, String> data = new HashMap<>();
        data.put(ResponseConstant.EMAIL, DataGenerator.getRandomEmail());
        data.put(ResponseConstant.PASSWORD, "123");
        data.put(ResponseConstant.USERNAME, Constant.LEARNQA);
        data.put(ResponseConstant.FIRST_NAME, Constant.LEARNQA);
        data.put(ResponseConstant.LAST_NAME, Constant.LEARNQA);

        return data;
    }

    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();
        Map<String, String> userData = new HashMap<>();
        String[] keys = {ResponseConstant.EMAIL, ResponseConstant.PASSWORD, ResponseConstant.USERNAME,
                ResponseConstant.FIRST_NAME, ResponseConstant.LAST_NAME};
        for (String key : keys) {
            if (nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }
}
