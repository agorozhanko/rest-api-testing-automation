package lib.enumeration;

public enum Platform {
    WEB("Web"),
    MOBILE("Mobile"),
    GOOGLEBOT("Googlebot");

    private String text;

    Platform(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Platform{" +
                "text='" + text + '\'' +
                '}';
    }
}
