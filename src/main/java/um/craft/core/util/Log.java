package um.craft.core.util;

public final class Log {

    public static final String RESET = "\u001B[0m";

    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";

    public static final String BRIGHT_BLACK = "\u001B[90m";

    private Log() {
    }

    public static void debug(String message) {
        print(BRIGHT_BLACK, "DEBUG", message);
    }

    public static void info(String message) {
        print(CYAN, "INFO", message);
    }

    public static void noti(String message) {
        print(PURPLE, "NOTI", message);
    }

    public static void log(String message) {
        print(BLUE, "LOG", message);
    }

    public static void success(String message) {
        print(GREEN, "SUCCESS", message);
    }

    public static void warn(String message) {
        print(YELLOW, "WARN", message);
    }

    public static void error(String message) {
        print(RED, "ERROR", message);
    }

    public static void error(String message, Throwable throwable) {
        print(RED, "ERROR", message);
        throwable.printStackTrace(System.out);
    }

    private static void print(String color, String tag, String message) {
        System.out.println(color + "[" + tag + "]" + RESET + " " + message);
    }
}