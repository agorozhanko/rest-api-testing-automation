package lib.enumeration;

public enum Device {
    ANDROID("Android"),
    IOS("iOS"),
    IPHONE("iPhone"),
    NO("No"),
    UNKNOWN("Unknown");

    private String text;

    Device(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Device{" +
                "text='" + text + '\'' +
                '}';
    }
}
