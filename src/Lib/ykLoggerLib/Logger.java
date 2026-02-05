package Lib.ykLoggerLib;
import static Lib.ykLoggerLib.LogLevel.*;

public class Logger {

    private static final String RESET = "\u001B[0m";

    public static void log(LogLevel level, String message) {
        System.out.println(level.color + "[" + level + "] " + RESET + message);
    }

    public static void main(String[] args) {
        log(INFO, "Test");
        log(WARN, "test warning");
        log(ERROR, "test error");
        log(DEBUG, "test debug");
    }
}
