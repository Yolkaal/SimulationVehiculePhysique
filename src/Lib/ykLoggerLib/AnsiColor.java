package Lib.ykLoggerLib;

public class AnsiColor {

    int red;
    int green;
    int blue;
    String ansiCode;

    public AnsiColor(int r, int g, int b){
        red = r;
        green = g;
        blue = b;
        ansiCode = "\u001B[38;2;" + red + ";" + green + ";" + blue + "m";
    }

    public String getAnsiColor(){
        return ansiCode;
    }
}
