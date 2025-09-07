package lib.utility;

import lib.model.UserAgentHeader;

import java.util.List;

public class UserAgentUtility {
    public static void generateErrorText(UserAgentHeader expectedUserAgentHeader, UserAgentHeader actualUserAgentHeader, List<String> errors) {
        boolean hasError = false;
        StringBuilder errorMessage = new StringBuilder("User Agent: " + expectedUserAgentHeader.getUserAgent() + "\n");

        if (!expectedUserAgentHeader.getExpectedDevice().equals(actualUserAgentHeader.getExpectedDevice())) {
            hasError = true;
            errorMessage.append(String.format("  device: expected '%s', actual '%s'\n",
                    expectedUserAgentHeader.getExpectedDevice(), actualUserAgentHeader.getExpectedDevice()));
        }
        if (!expectedUserAgentHeader.getExpectedBrowser().equals(actualUserAgentHeader.getExpectedBrowser())) {
            hasError = true;
            errorMessage.append(String.format("  browser: expected '%s', actual '%s'\n",
                    expectedUserAgentHeader.getExpectedBrowser(), actualUserAgentHeader.getExpectedBrowser()));
        }
        if (!expectedUserAgentHeader.getExpectedPlatform().equals(actualUserAgentHeader.getExpectedPlatform())) {
            hasError = true;
            errorMessage.append(String.format("  platform: expected '%s', actual '%s'\n",
                    expectedUserAgentHeader.getExpectedPlatform(), actualUserAgentHeader.getExpectedPlatform()));
        }

        if (hasError) {
            errors.add(errorMessage.toString());
        }
    }

    public static void printError(List<String> errors) {
        if (errors.isEmpty()) {
            System.out.println("All User Agents are correct.");
        } else {
            System.out.println("Errors found in User Agent:");
            errors.forEach(System.out::println);
        }
    }
}
