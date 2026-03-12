package ykSimEngine.eng.graph;

import ykSimEngine.eng.EngCtx;
import ykSimEngine.eng.graph.vk.VkCtx;
import ykSimEngine.eng.wnd.Window;
import ykSimEngine.eng.scene.Scene;

public class Render {

    private final VkCtx vkCtx;

    public Render(EngCtx engCtx) {
        vkCtx = new VkCtx();
    }

    public void cleanup() {
        vkCtx.cleanup();
    }

    public void render(EngCtx engCtx) {
        // To be implemented
    }
}