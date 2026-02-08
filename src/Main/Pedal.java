package Main;

import Lib.ykEngineArchitectureLib.SimulationEngine;
import Lib.ykEngineArchitectureLib.Updatable;

public abstract class Pedal extends Updatable {

    private int maxStrength;
    private int strength;


    //CONSTRUCTOR

    public Pedal(int maxStrength) {
        register();
        this.maxStrength = maxStrength;
    }

    //GETTER

    public int getStrength() {
        return strength;
    }

    //SETTER

    public void setStrength(int maxStrength) {
        maxStrength = strength;
    }
}
