package engine.renderer.vulkan;

import engine.Window;
import org.lwjgl.system.MemoryStack;

import java.nio.LongBuffer;

import static org.lwjgl.glfw.GLFWVulkan.*;
import static org.lwjgl.vulkan.KHRSurface.vkDestroySurfaceKHR;
import static org.lwjgl.vulkan.VK10.*;

public class VulkanSurface {

    private long surface;

    public VulkanSurface(VulkanInstance instance, Window window) {

        try (MemoryStack stack = MemoryStack.stackPush()) {

            LongBuffer surfaceBuffer = stack.mallocLong(1);

            if (glfwCreateWindowSurface(
                    instance.getInstance(),
                    window.getHandle(),
                    null,
                    surfaceBuffer
            ) != VK_SUCCESS) {

                throw new RuntimeException("Failed to create Vulkan surface");
            }

            surface = surfaceBuffer.get(0);
        }
    }

    public long getSurface() {
        return surface;
    }

    public void cleanup(VulkanInstance instance) {
        vkDestroySurfaceKHR(instance.getInstance(), surface, null);
    }
}