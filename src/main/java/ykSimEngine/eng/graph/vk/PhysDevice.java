package ykSimEngine.eng.graph.vk;

import Lib.ykLoggerLib.LogLevel;
import Lib.ykLoggerLib.Logger;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.vulkan.VK11.vkGetPhysicalDeviceProperties2;
import static ykSimEngine.eng.graph.vk.VkUtils.vkCheck;

public class PhysDevice {

    protected static final Set<String> REQUIRED_EXTENSIONS;

    static {
        REQUIRED_EXTENSIONS = new HashSet<>();
        REQUIRED_EXTENSIONS.add(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME);
    }

    private final VkExtensionProperties.Buffer vkDeviceExtensions;
    private final VkPhysicalDeviceMemoryProperties vkMemoryProperties;
    private final VkPhysicalDevice vkPhysicalDevice;
    private final VkPhysicalDeviceFeatures vkPhysicalDeviceFeatures;
    private final VkPhysicalDeviceProperties2 vkPhysicalDeviceProperties;
    private final VkQueueFamilyProperties.Buffer vkQueueFamilyProps;

    private PhysDevice(VkPhysicalDevice vkPhysicalDevice) {
        try (var stack = MemoryStack.stackPush()) {
            this.vkPhysicalDevice = vkPhysicalDevice;

            IntBuffer intBuffer = stack.mallocInt(1);

            // Get device properties
            vkPhysicalDeviceProperties = VkPhysicalDeviceProperties2.calloc().sType$Default();
            vkGetPhysicalDeviceProperties2(vkPhysicalDevice, vkPhysicalDeviceProperties);

            // Get device extensions
            vkCheck(vkEnumerateDeviceExtensionProperties(vkPhysicalDevice, (String) null, intBuffer, null),
                    "Failed to get number of device extension properties");
            vkDeviceExtensions = VkExtensionProperties.calloc(intBuffer.get(0));
            vkCheck(vkEnumerateDeviceExtensionProperties(vkPhysicalDevice, (String) null, intBuffer, vkDeviceExtensions),
                    "Failed to get extension properties");

            // Get Queue family properties
            vkGetPhysicalDeviceQueueFamilyProperties(vkPhysicalDevice, intBuffer, null);
            vkQueueFamilyProps = VkQueueFamilyProperties.calloc(intBuffer.get(0));
            vkGetPhysicalDeviceQueueFamilyProperties(vkPhysicalDevice, intBuffer, vkQueueFamilyProps);

            vkPhysicalDeviceFeatures = VkPhysicalDeviceFeatures.calloc();
            vkGetPhysicalDeviceFeatures(vkPhysicalDevice, vkPhysicalDeviceFeatures);

            // Get Memory information and properties
            vkMemoryProperties = VkPhysicalDeviceMemoryProperties.calloc();
            vkGetPhysicalDeviceMemoryProperties(vkPhysicalDevice, vkMemoryProperties);
        }
    }

    private boolean hasGraphicsQueueFamily() {
        boolean result = false;
        int numQueueFamilies = vkQueueFamilyProps != null ? vkQueueFamilyProps.capacity() : 0;
        for (int i = 0; i < numQueueFamilies; i++) {
            VkQueueFamilyProperties familyProps = vkQueueFamilyProps.get(i);
            if ((familyProps.queueFlags() & VK_QUEUE_GRAPHICS_BIT) != 0) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void cleanup() {
        Logger.log(LogLevel.DEBUG,"Destroying physical device "+ getDeviceName());
        vkMemoryProperties.free();
        vkPhysicalDeviceFeatures.free();
        vkQueueFamilyProps.free();
        vkDeviceExtensions.free();
        vkPhysicalDeviceProperties.free();
    }

    public String getDeviceName() {
        return vkPhysicalDeviceProperties.properties().deviceNameString();
    }

    public VkPhysicalDeviceMemoryProperties getVkMemoryProperties() {
        return vkMemoryProperties;
    }

    public VkPhysicalDevice getVkPhysicalDevice() {
        return vkPhysicalDevice;
    }

    public VkPhysicalDeviceFeatures getVkPhysicalDeviceFeatures() {
        return vkPhysicalDeviceFeatures;
    }

    public VkPhysicalDeviceProperties2 getVkPhysicalDeviceProperties() {
        return vkPhysicalDeviceProperties;
    }

    public VkQueueFamilyProperties.Buffer getVkQueueFamilyProps() {
        return vkQueueFamilyProps;
    }

    public static PhysDevice createPhysicalDevice(Instance instance, String prefDeviceName) {
        Logger.log(LogLevel.DEBUG, "Selecting physical devices");
        PhysDevice result = null;
        try (var stack = MemoryStack.stackPush()) {
            // Get available devices
            PointerBuffer pPhysicalDevices = getPhysicalDevices(instance, stack);
            int numDevices = pPhysicalDevices.capacity();
            // Populate available devices
            var physDevices = new ArrayList<PhysDevice>();
            for (int i = 0; i < numDevices; i++) {
                var vkPhysicalDevice = new VkPhysicalDevice(pPhysicalDevices.get(i), instance.getVkInstance());
                var physDevice = new PhysDevice(vkPhysicalDevice);

                String deviceName = physDevice.getDeviceName();
                if (!physDevice.hasGraphicsQueueFamily()) {
                    Logger.log(LogLevel.DEBUG,"Device " +deviceName+ " does not support graphics queue family");
                    physDevice.cleanup();
                    continue;
                }

                if (!physDevice.supportsExtensions(REQUIRED_EXTENSIONS)) {
                    Logger.log(LogLevel.DEBUG,"Device "+ deviceName +" does not support required extensions");
                    physDevice.cleanup();
                    continue;
                }

                if (prefDeviceName != null && prefDeviceName.equals(deviceName)) {
                    result = physDevice;
                    break;
                }
                if (physDevice.vkPhysicalDeviceProperties.properties().deviceType() == VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU) {
                    physDevices.addFirst(physDevice);
                } else {
                    physDevices.add(physDevice);
                }
            }
            result = result == null && !physDevices.isEmpty() ? physDevices.removeFirst() : result;

            // Clean up non-selected devices
            physDevices.forEach(PhysDevice::cleanup);

            if (result == null) {
                throw new RuntimeException("No suitable physical devices found");
            }
            Logger.log(LogLevel.DEBUG,"Selected device: "+ result.getDeviceName());
        }

        return result;
    }

    protected static PointerBuffer getPhysicalDevices(Instance instance, MemoryStack stack) {
        PointerBuffer pPhysicalDevices;
        // Get number of physical devices
        IntBuffer intBuffer = stack.mallocInt(1);
        vkCheck(vkEnumeratePhysicalDevices(instance.getVkInstance(), intBuffer, null),
                "Failed to get number of physical devices");
        int numDevices = intBuffer.get(0);
        Logger.log(LogLevel.DEBUG,"Detected "+numDevices+" physical device(s)");

        // Populate physical devices list pointer
        pPhysicalDevices = stack.mallocPointer(numDevices);
        vkCheck(vkEnumeratePhysicalDevices(instance.getVkInstance(), intBuffer, pPhysicalDevices),
                "Failed to get physical devices");
        return pPhysicalDevices;
    }

    public boolean supportsExtensions(Set<String> extensions) {
        var copyExtensions = new HashSet<>(extensions);
        int numExtensions = vkDeviceExtensions != null ? vkDeviceExtensions.capacity() : 0;
        for (int i = 0; i < numExtensions; i++) {
            String extensionName = vkDeviceExtensions.get(i).extensionNameString();
            copyExtensions.remove(extensionName);
        }

        boolean result = copyExtensions.isEmpty();
        if (!result) {
            Logger.log(LogLevel.DEBUG,"At least "+  copyExtensions.iterator().next() +" extension is not supported by device "+ getDeviceName());
        }
        return result;
    }

}