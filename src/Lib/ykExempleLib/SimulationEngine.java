package Lib.ykExempleLib;

import java.util.ArrayList;
import java.util.List;

public final class SimulationEngine {

    private float LAST_FRAME_TIMESTAMP;

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

    public void register(Updatable object) {
        SIM_OBJECT_REGISTRY.add(object);
    }

    public void unregister(Updatable object) {
        SIM_OBJECT_REGISTRY.remove(object);
    }

    public void Start(){

    }

    public void End(){
        LAST_FRAME_TIMESTAMP = System.currentTimeMillis();
    }

    public void Update() {
        for (Updatable u : SIM_OBJECT_REGISTRY) {
            u.Update();
            System.out.println();
        }
    }

    public float getFrameTime() {
        return LAST_FRAME_TIMESTAMP - System.currentTimeMillis();
    }

    public int getFramerate(){
        return (int) (1/getFrameTime());
    }
}
