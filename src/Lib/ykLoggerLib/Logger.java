package Lib.ykLoggerLib;

public class Logger {

    private static final String RESET = "\u001B[0m";

    public static void log(String message){
        System.out.println(message);
    }

    public static void log(LogLevel level, String message) {
        System.out.println(level.color + "[" + level + "] " + RESET + message);
    }

    public static void log(AnsiColor color, String message) {
        System.out.println(color.getAnsiColor() + message + RESET);
    }

    public static void log(LogLevel level, AnsiColor color, String message) {
        System.out.println(level.color + "[" + level + "] " + color.getAnsiColor() + message + RESET);
    }
}
