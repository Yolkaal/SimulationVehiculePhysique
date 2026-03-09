package engine.renderer.vulkan;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.KHRSwapchain.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR;
import static org.lwjgl.vulkan.VK10.*;

public class VulkanRenderPass {

    private final VkDevice device;
    private long renderPass;

    public VulkanRenderPass(VkDevice device, int imageFormat) {
        this.device = device;

        try (MemoryStack stack = MemoryStack.stackPush()) {

            // Attachement couleur
            VkAttachmentDescription colorAttachment = VkAttachmentDescription.calloc(stack)
                    .format(imageFormat)
                    .samples(VK_SAMPLE_COUNT_1_BIT)
                    .loadOp(VK_ATTACHMENT_LOAD_OP_CLEAR)
                    .storeOp(VK_ATTACHMENT_STORE_OP_STORE)
                    .stencilLoadOp(VK_ATTACHMENT_LOAD_OP_DONT_CARE)
                    .stencilStoreOp(VK_ATTACHMENT_STORE_OP_DONT_CARE)
                    .initialLayout(VK_IMAGE_LAYOUT_UNDEFINED)
                    .finalLayout(VK_IMAGE_LAYOUT_PRESENT_SRC_KHR);

            // Reference d'attachement
            VkAttachmentReference colorAttachmentRef = VkAttachmentReference.calloc(stack)
                    .attachment(0)
                    .layout(VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);

            // Buffer pour les references de couleurs
            VkAttachmentReference.Buffer colorAttachmentRefs = VkAttachmentReference.calloc(1, stack);
            colorAttachmentRefs.put(0, colorAttachmentRef);

            // Subpass
            VkSubpassDescription subpass = VkSubpassDescription.calloc(stack)
                    .pipelineBindPoint(VK_PIPELINE_BIND_POINT_GRAPHICS)
                    .colorAttachmentCount(1)
                    .pColorAttachments(colorAttachmentRefs);

            // Buffer de subpasses
            VkSubpassDescription.Buffer subpasses = VkSubpassDescription.calloc(1, stack);
            subpasses.put(0, subpass);

            // Buffer d'attachments
            VkAttachmentDescription.Buffer attachments = VkAttachmentDescription.calloc(1, stack);
            attachments.put(0, colorAttachment);

            // Création du Render Pass
            VkRenderPassCreateInfo renderPassInfo = VkRenderPassCreateInfo.calloc(stack)
                    .sType(VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO)
                    .pAttachments(attachments)
                    .pSubpasses(subpasses);

            // Handle
            LongBuffer pRenderPass = stack.mallocLong(1);
            if (vkCreateRenderPass(device, renderPassInfo, null, pRenderPass) != VK_SUCCESS) {
                throw new RuntimeException("Échec de la création du Render Pass !");
            }
            renderPass = pRenderPass.get(0);
        }
    }

    public long getRenderPass() {
        return renderPass;
    }

    public void cleanup() {
        vkDestroyRenderPass(device, renderPass, null);
    }
}