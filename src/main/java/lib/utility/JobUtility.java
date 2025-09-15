package lib.utility;

import io.restassured.path.json.JsonPath;
import lib.constant.Constant;
import lib.constant.ResponseConstant;

import java.util.concurrent.TimeUnit;

public class JobUtility {

    public static void waitForJobCompletion(String responseStatus, int seconds) {
        if (responseStatus != null) {
            if (responseStatus.equals(Constant.JOB_IS_NOT_READY)) {
                try {
                    TimeUnit.SECONDS.sleep(seconds);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String getResult(JsonPath response) {
        return response.get(ResponseConstant.RESULT);
    }

    public static String getStatus(JsonPath response) {
        return response.get(ResponseConstant.STATUS);
    }

    public static void checkError(String responseError) {
        if (responseError != null) {
            if (responseError.equals(Constant.NO_JOB_LINKED_TO_THIS_TOKEN)) {
                throw new IllegalArgumentException(Constant.NO_JOB_LINKED_TO_THIS_TOKEN);
            }
        }
    }

    public static String getError(JsonPath response) {
        return response.get(ResponseConstant.ERROR);
    }

    public static Integer workTimeCompletion(JsonPath response) {
        return response.get(ResponseConstant.SECONDS);
    }

    public static String getToken(JsonPath response) {
        return response.get(ResponseConstant.TOKEN);
    }
}
