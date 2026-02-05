package Lib.ykLoggerLib;

public class AnsiColor {

    public static String getAnsiColor(int r, int g, int b){
        return "\u001B[38;2;" + r + ";" + g + ";" + b + "m";
    }

}
