package engine.renderer.vulkan;

import Lib.ykLoggerLib.LogLevel;
import Lib.ykLoggerLib.Logger;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

import static org.lwjgl.glfw.GLFWVulkan.glfwGetRequiredInstanceExtensions;
import static org.lwjgl.vulkan.VK10.*;


public class VulkanInstance {

    private VkInstance instance;

    public VulkanInstance(){
        createInstance();
    }

    private void createInstance() {
        try (MemoryStack stack = MemoryStack.stackPush()) {

            VkApplicationInfo appInfo = VkApplicationInfo.calloc(stack);
            appInfo.sType(VK_STRUCTURE_TYPE_APPLICATION_INFO);
            appInfo.pApplicationName(stack.UTF8("SimEngine"));
            appInfo.applicationVersion(VK_MAKE_VERSION(1,0,0));
            appInfo.pEngineName(stack.UTF8("SimEngine"));
            appInfo.engineVersion(VK_MAKE_VERSION(1,0,0));
            appInfo.apiVersion(VK_API_VERSION_1_0);

            VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.calloc(stack);
            createInfo.sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO);
            createInfo.pApplicationInfo(appInfo);

            // 🔹 Extensions nécessaires pour GLFW
            PointerBuffer glfwExtensions = glfwGetRequiredInstanceExtensions();
            if (glfwExtensions == null) {
                throw new RuntimeException("GLFW required Vulkan extensions not found");
            }

            createInfo.ppEnabledExtensionNames(glfwExtensions);

            PointerBuffer instancePtr = stack.mallocPointer(1);

            if (vkCreateInstance(createInfo, null, instancePtr) != VK_SUCCESS) {
                Logger.log(LogLevel.ERROR, "Vulkan instance creation Failed");
                throw new RuntimeException("Failed to create Vulkan instance");
            }

            instance = new VkInstance(instancePtr.get(0), createInfo);
        }
    }

    public VkInstance getInstance() {
        return instance;
    }

    public void cleanup() {
        vkDestroyInstance(instance, null);
    }
}