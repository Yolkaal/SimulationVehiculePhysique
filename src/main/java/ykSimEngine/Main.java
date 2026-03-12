package ykSimEngine;

import Lib.ykLoggerLib.*;
import ykSimEngine.eng.*;
import ykSimEngine.eng.graph.Render;
import ykSimEngine.eng.scene.Scene;
import ykSimEngine.eng.wnd.Window;

public class Main implements IGameLogic {

    public static void main(String[] args) {
        Logger.log(LogLevel.INFO,"Starting application");
        var engine = new Engine("Sim Engine", new Main());
        Logger.log(LogLevel.INFO,"Started application");
        engine.run();
    }

    @Override
    public void cleanup() {
        // To be implemented
    }

    @Override
    public void init(EngCtx engCtx) {
        // To be implemented
    }

    @Override
    public void input(EngCtx engCtx, long diffTimeMillis) {
        // To be implemented
    }

    @Override
    public void update(EngCtx engCtx, long diffTimeMillis) {
        // To be implemented
    }
}