package engine.renderer.vulkan;

import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class VulkanPipeline {

    private final VkDevice device;
    private long pipeline;
    private long pipelineLayout;

    public VulkanPipeline(VkDevice device, VulkanRenderPass renderPass) {
        this.device = device;

        try (var stack = org.lwjgl.system.MemoryStack.stackPush()) {

            // 1️⃣ Pipeline layout minimal (pas de descriptors pour l’instant)
            VkPipelineLayoutCreateInfo layoutInfo = VkPipelineLayoutCreateInfo.calloc(stack)
                    .sType(VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO)
                    .pSetLayouts(null)
                    .pPushConstantRanges(null);

            LongBuffer pLayout = stack.mallocLong(1);
            if (vkCreatePipelineLayout(device, layoutInfo, null, pLayout) != VK_SUCCESS) {
                throw new RuntimeException("Échec de la création du pipeline layout !");
            }
            pipelineLayout = pLayout.get(0);

            // 2️⃣ Shaders (ici on supposera que les modules sont déjà créés)
            // VkPipelineShaderStageCreateInfo.Buffer shaderStages = ...

            // 3️⃣ Structure pipeline minimale
            VkGraphicsPipelineCreateInfo.Buffer pipelineInfo = VkGraphicsPipelineCreateInfo.calloc(1, stack)
                    .sType(VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO)
                    .layout(pipelineLayout)
                    .renderPass(renderPass.getRenderPass())
                    .subpass(0);
            // Pour l’instant on n’a pas encore bind shaders → à compléter

            LongBuffer pPipeline = stack.mallocLong(1);
            if (vkCreateGraphicsPipelines(device, VK_NULL_HANDLE, pipelineInfo, null, pPipeline) != VK_SUCCESS) {
                throw new RuntimeException("Échec de la création du pipeline !");
            }
            pipeline = pPipeline.get(0);
        }
    }

    public long getPipeline() {
        return pipeline;
    }

    public long getPipelineLayout() {
        return pipelineLayout;
    }

    public void cleanup() {
        vkDestroyPipeline(device, pipeline, null);
        vkDestroyPipelineLayout(device, pipelineLayout, null);
    }
}