package Lib.ykLoggerLib;
import static Lib.ykLoggerLib.AnsiColor.*;

public enum LogLevel {

    INFO(getAnsiColor(0,200,200)),
    WARN(getAnsiColor(255, 190, 0)),
    ERROR(getAnsiColor(255,0,0)),
    DEBUG(getAnsiColor(0,255,50));

    public final String color;

    LogLevel(String color) {
        this.color = color;
    }

}
