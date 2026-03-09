package engine.renderer.vulkan;

import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class VulkanImageViews {

    private final VkDevice device;
    private final long[] imageViews;

    public VulkanImageViews(VkDevice device, long[] swapchainImages, int imageFormat) {
        this.device = device;
        this.imageViews = new long[swapchainImages.length];

        for (int i = 0; i < swapchainImages.length; i++) {
            VkImageViewCreateInfo createInfo = VkImageViewCreateInfo.calloc()
                    .sType(VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO)
                    .image(swapchainImages[i])
                    .viewType(VK_IMAGE_VIEW_TYPE_2D)
                    .format(imageFormat)
                    .components(c -> c
                            .r(VK_COMPONENT_SWIZZLE_IDENTITY)
                            .g(VK_COMPONENT_SWIZZLE_IDENTITY)
                            .b(VK_COMPONENT_SWIZZLE_IDENTITY)
                            .a(VK_COMPONENT_SWIZZLE_IDENTITY))
                    .subresourceRange(srr -> srr
                            .aspectMask(VK_IMAGE_ASPECT_COLOR_BIT)
                            .baseMipLevel(0)
                            .levelCount(1)
                            .baseArrayLayer(0)
                            .layerCount(1));

            LongBuffer pImageView = org.lwjgl.system.MemoryStack.stackGet().mallocLong(1);
            if (vkCreateImageView(device, createInfo, null, pImageView) != VK_SUCCESS) {
                throw new RuntimeException("Échec de la création de l'image view !");
            }
            imageViews[i] = pImageView.get(0);
            createInfo.free();
        }
    }

    public long[] getImageViews() {
        return imageViews;
    }

    public void cleanup() {
        for (long view : imageViews) {
            vkDestroyImageView(device, view, null);
        }
    }
}

