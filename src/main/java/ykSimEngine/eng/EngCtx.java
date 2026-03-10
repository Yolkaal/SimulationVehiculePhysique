package ykSimEngine.eng;

import ykSimEngine.eng.scene.Scene;
import ykSimEngine.eng.wnd.Window;

public record EngCtx(Window window, Scene scene) {

    public void cleanup() {
        window.cleanup();
    }
}