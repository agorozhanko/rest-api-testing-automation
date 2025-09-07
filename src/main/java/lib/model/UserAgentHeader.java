package lib.model;

public class UserAgentHeader {
    private String userAgent;
    private String expectedPlatform;
    private String expectedBrowser;
    private String expectedDevice;

    public UserAgentHeader(String userAgent, String expectedPlatform, String expectedBrowser, String expectedDevice) {
        this.userAgent = userAgent;
        this.expectedPlatform = expectedPlatform;
        this.expectedBrowser = expectedBrowser;
        this.expectedDevice = expectedDevice;
    }

    public UserAgentHeader(String expectedPlatform, String expectedBrowser, String expectedDevice) {
        this.expectedPlatform = expectedPlatform;
        this.expectedBrowser = expectedBrowser;
        this.expectedDevice = expectedDevice;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getExpectedPlatform() {
        return expectedPlatform;
    }

    public String getExpectedBrowser() {
        return expectedBrowser;
    }

    public String getExpectedDevice() {
        return expectedDevice;
    }
}
