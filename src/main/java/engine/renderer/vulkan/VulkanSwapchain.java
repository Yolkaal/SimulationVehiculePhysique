package engine.renderer.vulkan;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.vulkan.KHRSurface.*;
import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.vulkan.VK10.*;

public class VulkanSwapchain {

    private long swapchain;
    private VkDevice device;
    private VkPhysicalDevice physicalDevice;
    private VulkanSurface surface;

    private int imageFormat;
    private VkExtent2D extent;

    public VulkanSwapchain(VulkanLogicalDevice logicalDevice, VulkanPhysicalDevice physicalDevice, VulkanSurface surface, int width, int height) {
        this.device = logicalDevice.getDevice();
        this.physicalDevice = physicalDevice.getPhysicalDevice();
        this.surface = surface;

        try (MemoryStack stack = MemoryStack.stackPush()) {

            // 1️⃣ Vérifier les formats disponibles
            IntBuffer formatCount = stack.ints(0);
            vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice.getPhysicalDevice(), surface.getSurface(), formatCount, null);

            if (formatCount.get(0) == 0) {
                throw new RuntimeException("Aucun format de surface disponible !");
            }

            VkSurfaceFormatKHR.Buffer surfaceFormats = VkSurfaceFormatKHR.calloc(formatCount.get(0), stack);
            vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice.getPhysicalDevice(), surface.getSurface(), formatCount, surfaceFormats);

            // Choisir un format
            VkSurfaceFormatKHR chosenFormat = surfaceFormats.get(0);
            imageFormat = chosenFormat.format();
            extent = VkExtent2D.calloc(stack).set(width, height);

            // 2️⃣ Créer la swapchain
            VkSwapchainCreateInfoKHR createInfo = VkSwapchainCreateInfoKHR.calloc(stack);
            createInfo.sType(VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR);
            createInfo.surface(surface.getSurface());
            createInfo.minImageCount(2);
            createInfo.imageFormat(imageFormat);
            createInfo.imageColorSpace(chosenFormat.colorSpace());
            createInfo.imageExtent(extent);
            createInfo.imageArrayLayers(1);
            createInfo.imageUsage(VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT);

            int graphicsFamily = logicalDevice.getGraphicsQueueFamily();
            int presentFamily = logicalDevice.getPresentQueueFamily();

            if (graphicsFamily != presentFamily) {
                createInfo.imageSharingMode(VK_SHARING_MODE_CONCURRENT);
                createInfo.pQueueFamilyIndices(stack.ints(graphicsFamily, presentFamily));
            } else {
                createInfo.imageSharingMode(VK_SHARING_MODE_EXCLUSIVE);
            }

            createInfo.preTransform(VK_SURFACE_TRANSFORM_IDENTITY_BIT_KHR);
            createInfo.compositeAlpha(VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR);
            createInfo.presentMode(VK_PRESENT_MODE_FIFO_KHR);
            createInfo.clipped(true);
            createInfo.oldSwapchain(VK_NULL_HANDLE);

            LongBuffer pSwapchain = stack.longs(VK_NULL_HANDLE);
            if (vkCreateSwapchainKHR(device, createInfo, null, pSwapchain) != VK_SUCCESS) {
                throw new RuntimeException("Échec de la création de la swapchain !");
            }

            swapchain = pSwapchain.get(0);
        }
    }

    public long getSwapchain() {
        return swapchain;
    }

    public int getImageFormat() {
        return imageFormat;
    }

    public VkExtent2D getExtent() {
        return extent;
    }

    public void cleanup() {
        vkDestroySwapchainKHR(device, swapchain, null);
    }
}