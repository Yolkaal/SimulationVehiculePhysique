package JavaTests;

import Lib.ykEngineArchitectureLib.SimulationEngine;
import org.junit.jupiter.api.Test;

public class EngineTest {

    @Test
    public void launchEngineTest() throws InterruptedException {
        SimulationEngine.getInstance().Run();
    }

}
