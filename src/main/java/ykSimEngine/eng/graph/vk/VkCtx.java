package ykSimEngine.eng.graph.vk;

import ykSimEngine.eng.EngCfg;

public class VkCtx {

    private final Instance instance;

    public VkCtx() {
        var engCfg = EngCfg.getInstance();
        instance = new Instance(engCfg.isVkValidate());
    }

    public void cleanup() {
        instance.cleanup();
    }
}