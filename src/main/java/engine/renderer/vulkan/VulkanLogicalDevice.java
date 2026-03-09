package engine.renderer.vulkan;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;

import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.vulkan.KHRSurface.*;

public class VulkanLogicalDevice {

    private VkDevice device;
    private int graphicsQueueFamily;
    private int presentQueueFamily;
    private VkQueue graphicsQueue;
    private VkQueue presentQueue;

    public VulkanLogicalDevice(VulkanPhysicalDevice physicalDevice, VulkanSurface surface) {
        try (MemoryStack stack = MemoryStack.stackPush()) {

            VkPhysicalDevice vkPhysical = physicalDevice.getPhysicalDevice();

            // 1️⃣ Récupérer les propriétés des familles de queues
            IntBuffer queueFamilyCount = stack.ints(0);
            vkGetPhysicalDeviceQueueFamilyProperties(vkPhysical, queueFamilyCount, null);

            VkQueueFamilyProperties.Buffer queueFamilies =
                    VkQueueFamilyProperties.calloc(queueFamilyCount.get(0), stack);
            vkGetPhysicalDeviceQueueFamilyProperties(vkPhysical, queueFamilyCount, queueFamilies);

            graphicsQueueFamily = -1;
            presentQueueFamily = -1;

            // 2️⃣ Identifier les familles graphiques et de présentation
            for (int i = 0; i < queueFamilies.capacity(); i++) {
                VkQueueFamilyProperties qf = queueFamilies.get(i);

                if ((qf.queueFlags() & VK_QUEUE_GRAPHICS_BIT) != 0 && graphicsQueueFamily == -1) {
                    graphicsQueueFamily = i;
                }

                IntBuffer presentSupport = stack.ints(VK_FALSE);
                vkGetPhysicalDeviceSurfaceSupportKHR(vkPhysical, i, surface.getSurface(), presentSupport);

                if (presentSupport.get(0) == VK_TRUE && presentQueueFamily == -1) {
                    presentQueueFamily = i;
                }

                if (graphicsQueueFamily != -1 && presentQueueFamily != -1) break;
            }

            if (graphicsQueueFamily == -1 || presentQueueFamily == -1) {
                throw new RuntimeException("Impossible de trouver les queues GPU nécessaires !");
            }

            // 3️⃣ Créer les queues
            float priority = 1.0f;
            VkDeviceQueueCreateInfo.Buffer queueCreateInfos = VkDeviceQueueCreateInfo.calloc(2, stack);

            // Graphique
            queueCreateInfos.get(0)
                    .sType(VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO)
                    .queueFamilyIndex(graphicsQueueFamily)
                    .pQueuePriorities(stack.floats(priority));

            // Présentation (si différente)
            if (graphicsQueueFamily != presentQueueFamily) {
                queueCreateInfos.get(1)
                        .sType(VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO)
                        .queueFamilyIndex(presentQueueFamily)
                        .pQueuePriorities(stack.floats(priority));
            }

            // 4️⃣ Features du GPU
            VkPhysicalDeviceFeatures deviceFeatures = VkPhysicalDeviceFeatures.calloc(stack);

            // 5️⃣ Créer le Logical Device
            VkDeviceCreateInfo createInfo = VkDeviceCreateInfo.calloc(stack)
                    .sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO)
                    .pQueueCreateInfos(queueCreateInfos)
                    .pEnabledFeatures(deviceFeatures);

            PointerBuffer pDevice = stack.mallocPointer(1);
            if (vkCreateDevice(vkPhysical, createInfo, null, pDevice) != VK_SUCCESS) {
                throw new RuntimeException("Échec de la création du Logical Device");
            }

            device = new VkDevice(pDevice.get(0), vkPhysical, createInfo);

            // 6️⃣ Récupérer les queues
            PointerBuffer pQueue = stack.mallocPointer(1);

            vkGetDeviceQueue(device, graphicsQueueFamily, 0, pQueue);
            graphicsQueue = new VkQueue(pQueue.get(0), device);

            vkGetDeviceQueue(device, presentQueueFamily, 0, pQueue);
            presentQueue = new VkQueue(pQueue.get(0), device);
        }
    }

    public VkDevice getDevice() {
        return device;
    }

    public VkQueue getGraphicsQueue() {
        return graphicsQueue;
    }

    public VkQueue getPresentQueue() {
        return presentQueue;
    }

    public int getGraphicsQueueFamily() {
        return graphicsQueueFamily;
    }

    public int getPresentQueueFamily() {
        return presentQueueFamily;
    }

    public void cleanup() {
        vkDestroyDevice(device, null);
    }
}