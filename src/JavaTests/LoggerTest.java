package JavaTests;

import Lib.ykLoggerLib.AnsiColor;
import Lib.ykLoggerLib.LogLevel;
import Lib.ykLoggerLib.Logger;
import org.junit.jupiter.api.Test;

public class LoggerTest {

    public String test = "Test logger";
    public AnsiColor testColor = new AnsiColor(100, 255, 100);

    @Test
    public void WarnTest(){
        Logger.log(LogLevel.WARN, test);
    }
    @Test
    public void ErrorTest(){
        Logger.log(LogLevel.ERROR, test);
    }
    @Test
    public void DebugTest(){
        Logger.log(LogLevel.DEBUG, test);
    }
    @Test
    public void InfoTest(){
        Logger.log(LogLevel.INFO, test);
    }

    @Test
    public void WarnColorLogTest(){
        Logger.log(LogLevel.WARN, testColor, test);
    }

    @Test
    public void ErrorColorLogTest(){
        Logger.log(LogLevel.ERROR, testColor, test);
    }

    @Test
    public void DebugColorLogTest(){
        Logger.log(LogLevel.DEBUG, testColor, test);
    }

    @Test
    public void InfoColorLogTest(){
        Logger.log(LogLevel.INFO, testColor, test);
    }

    @Test
    public void ColorLogTest(){
        Logger.log(testColor, test);
    }
}
