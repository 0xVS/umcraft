package um.craft.core.util;

public enum ValueType {
    NUMBER("([+-]?\\d*\\.?\\d+)"),
    STRING("\"([^\"]*)\""),
    BOOLEAN("(true|false)");

    private final String pattern;

    ValueType(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
