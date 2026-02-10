package Lib.ykEngineArchitectureLib;

import Lib.ykLoggerLib.LogLevel;
import Lib.ykLoggerLib.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class SimulationEngine {

    private boolean engineRunning;
    public long LAUNCH_TIMESTAMP;
    private long LAST_FRAME_TIMESTAMP;
    private long CURRENT_FRAME_TIMESTAMP;

    private static SimulationEngine instance;

    private final List<Updatable> SIM_OBJECT_REGISTRY;

    private SimulationEngine() {
        SIM_OBJECT_REGISTRY = new ArrayList<>();
    }

    public static synchronized SimulationEngine getInstance() {
        if (instance == null) {
            instance = new SimulationEngine();
        }
        return instance;
    }

    public void Register(Updatable object) {
        SIM_OBJECT_REGISTRY.add(object);
    }

    public void Unregister(Updatable object) {
        SIM_OBJECT_REGISTRY.remove(object);
    }

    public void Awake() {
        LAUNCH_TIMESTAMP = System.currentTimeMillis();
        for (Updatable u : SIM_OBJECT_REGISTRY) {
            u.Awake();
        }
        LAST_FRAME_TIMESTAMP = System.currentTimeMillis();
    }

    public void Start() {
        for (Updatable u : SIM_OBJECT_REGISTRY) {
            u.Start();
            u.getInfo();
        }
    }

    public void Run() throws InterruptedException {
        engineRunning = true;
        Awake();
        Start();
        LAST_FRAME_TIMESTAMP = System.currentTimeMillis();
        while (engineRunning) {
            Update();
            CURRENT_FRAME_TIMESTAMP = System.currentTimeMillis();
            Logger.log(LogLevel.INFO, "FRAMETIME : " + getFrameTime());
            Logger.log(LogLevel.INFO, "FRAMERATE : " + getFrameRate());
            LAST_FRAME_TIMESTAMP = System.currentTimeMillis();
            if (LAST_FRAME_TIMESTAMP - LAUNCH_TIMESTAMP > 50000) {
                Stop();
            }
        }
    }

    public void Stop() {
        engineRunning = false;
    }

    public void Update() throws InterruptedException {
        for (Updatable u : SIM_OBJECT_REGISTRY) {
            u.Update();
        }
        TimeUnit.MILLISECONDS.sleep(16);
    }

    public boolean isEngineRunning() {
        return engineRunning;
    }

    public long getFrameTime() {
        return CURRENT_FRAME_TIMESTAMP - LAST_FRAME_TIMESTAMP;
    }

    public int getFrameRate(){
        return (int) (1000/getFrameTime());
    }
}
