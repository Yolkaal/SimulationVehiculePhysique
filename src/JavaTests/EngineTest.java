package JavaTests;

import Lib.ykEngineArchitectureLib.SimulationEngine;
import org.junit.Test;


public class EngineTest {

    @Test
    public void RunEngineTest() throws InterruptedException {
        SimulationEngine moteur = SimulationEngine.getInstance();
        moteur.Run();
        if (moteur.LAUNCH_TIMESTAMP - System.currentTimeMillis() > 50000){
            assertFalse(moteur.isEngineRunning());
        }
    }
}
