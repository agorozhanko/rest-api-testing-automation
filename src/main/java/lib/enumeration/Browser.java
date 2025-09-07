package lib.enumeration;

public enum Browser {
    CHROME("Chrome"),
    UNKNOWN("Unknown"),
    NO("No");
    private final String text;

    Browser(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Browser{" +
                "text='" + text + '\'' +
                '}';
    }
}
