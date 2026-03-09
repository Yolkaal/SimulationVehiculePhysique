package engine.renderer;

import engine.Window;
import engine.renderer.vulkan.*;

public class Renderer {

    private VulkanInstance instance;
    private VulkanSurface surface;
    private VulkanPhysicalDevice physicalDevice;

    public Renderer(Window window) {

        instance = new VulkanInstance();
        surface = new VulkanSurface(instance, window);
        physicalDevice = new VulkanPhysicalDevice(instance, surface);
    }

    public void render() {
        // futur rendu Vulkan
    }

    public void cleanup() {
        // pour l'instant cleanup surface et instance
        surface.cleanup(instance);
        instance.cleanup();
    }

    public VulkanPhysicalDevice getPhysicalDevice() {
        return physicalDevice;
    }
}