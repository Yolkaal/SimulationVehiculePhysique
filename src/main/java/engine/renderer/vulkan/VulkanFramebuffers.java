package engine.renderer.vulkan;

import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class VulkanFramebuffers {

    private final VkDevice device;
    private final long[] framebuffers;

    public VulkanFramebuffers(VkDevice device, VulkanRenderPass renderPass, long[] imageViews, VkExtent2D extent) {
        this.device = device;
        this.framebuffers = new long[imageViews.length];

        try (var stack = org.lwjgl.system.MemoryStack.stackPush()) {
            LongBuffer attachments = stack.mallocLong(1); // 1 attachement couleur

            VkFramebufferCreateInfo framebufferInfo = VkFramebufferCreateInfo.calloc(stack)
                    .sType(VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO)
                    .renderPass(renderPass.getRenderPass())
                    .width(extent.width())
                    .height(extent.height())
                    .layers(1);

            for (int i = 0; i < imageViews.length; i++) {
                attachments.put(0, imageViews[i]);
                framebufferInfo.pAttachments(attachments);

                LongBuffer pFramebuffer = stack.mallocLong(1);
                if (vkCreateFramebuffer(device, framebufferInfo, null, pFramebuffer) != VK_SUCCESS) {
                    throw new RuntimeException("Échec de la création du framebuffer !");
                }
                framebuffers[i] = pFramebuffer.get(0);
            }
        }
    }

    public long[] getFramebuffers() {
        return framebuffers;
    }

    public void cleanup() {
        for (long framebuffer : framebuffers) {
            vkDestroyFramebuffer(device, framebuffer, null);
        }
    }
}