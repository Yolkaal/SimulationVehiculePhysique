package Test;

import Lib.ykLoggerLib.LogLevel;
import Lib.ykLoggerLib.Logger;

public class LoggerTest {

    public String test = "Test logger";

    @test
    public void WarnTest(){
        Logger.log(LogLevel.WARN, test);
    }



}
