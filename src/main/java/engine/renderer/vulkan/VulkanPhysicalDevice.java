package engine.renderer.vulkan;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;

import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.vulkan.KHRSurface.*;
import static org.lwjgl.system.MemoryStack.*;

public class VulkanPhysicalDevice {

    private VkPhysicalDevice physicalDevice;

    public VulkanPhysicalDevice(VulkanInstance instance, VulkanSurface surface) {

        try (MemoryStack stack = stackPush()) {

            // 1️⃣ obtenir le nombre de devices
            IntBuffer deviceCount = stack.ints(0);
            vkEnumeratePhysicalDevices(instance.getInstance(), deviceCount, null);

            if (deviceCount.get(0) == 0) {
                throw new RuntimeException("Aucun GPU Vulkan compatible trouvé !");
            }

            // 2️⃣ récupérer les handles des devices
            PointerBuffer devices = stack.mallocPointer(deviceCount.get(0));
            vkEnumeratePhysicalDevices(instance.getInstance(), deviceCount, devices);

            // 3️⃣ sélectionner le meilleur GPU
            for (int i = 0; i < devices.capacity(); i++) {
                VkPhysicalDevice device = new VkPhysicalDevice(devices.get(i), instance.getInstance());

                if (isDeviceSuitable(device, surface)) {
                    physicalDevice = device;
                    break;
                }
            }

            if (physicalDevice == null) {
                throw new RuntimeException("Aucun GPU Vulkan approprié trouvé !");
            }
        }
    }

    private boolean isDeviceSuitable(VkPhysicalDevice device, VulkanSurface surface) {

        try (MemoryStack stack = stackPush()) {

            // on récupère les propriétés du device
            VkPhysicalDeviceProperties deviceProperties = VkPhysicalDeviceProperties.calloc(stack);
            vkGetPhysicalDeviceProperties(device, deviceProperties);

            // on récupère les features
            VkPhysicalDeviceFeatures deviceFeatures = VkPhysicalDeviceFeatures.calloc(stack);
            vkGetPhysicalDeviceFeatures(device, deviceFeatures);

            // Vérifier si le device peut présenter sur la surface
            IntBuffer queueCount = stack.ints(0);
            vkGetPhysicalDeviceQueueFamilyProperties(device, queueCount, null);

            if (queueCount.get(0) == 0) return false;

            VkQueueFamilyProperties.Buffer queueFamilies = VkQueueFamilyProperties.calloc(queueCount.get(0), stack);
            vkGetPhysicalDeviceQueueFamilyProperties(device, queueCount, queueFamilies);

            boolean foundGraphics = false;
            boolean foundPresent = false;

            for (int i = 0; i < queueFamilies.capacity(); i++) {
                VkQueueFamilyProperties qf = queueFamilies.get(i);

                if ((qf.queueFlags() & VK_QUEUE_GRAPHICS_BIT) != 0) {
                    foundGraphics = true;
                }

                IntBuffer presentSupport = stack.ints(VK_FALSE);
                vkGetPhysicalDeviceSurfaceSupportKHR(device, i, surface.getSurface(), presentSupport);

                if (presentSupport.get(0) == VK_TRUE) {
                    foundPresent = true;
                }

                if (foundGraphics && foundPresent) break;
            }

            return foundGraphics && foundPresent;
        }
    }

    public VkPhysicalDevice getPhysicalDevice() {
        return physicalDevice;
    }
}