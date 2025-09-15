package lib.utility;

import lib.constant.Constant;
import lib.constant.ResponseConstant;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {
    public static String getRandomEmail() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return String.format("learnqa%s@example.com", timestamp);
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
