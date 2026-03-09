package Lib.ykLoggerLib;

public enum LogLevel {

    INFO(new AnsiColor(0,250,255).getAnsiColor()),
    WARN(new AnsiColor(255, 200, 0).getAnsiColor()),
    ERROR(new AnsiColor(255,0,0).getAnsiColor()),
    DEBUG(new AnsiColor(0,255,60).getAnsiColor());

    public final String color;

    LogLevel(String color) {
        this.color = color;
    }

}
