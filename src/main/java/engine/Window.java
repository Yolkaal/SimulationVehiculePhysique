package engine;

import Lib.ykLoggerLib.LogLevel;
import Lib.ykLoggerLib.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    private long handle;

    public Window(int width, int height, String title) {

        if (!glfwInit()) {
            Logger.log(LogLevel.ERROR, "GLFW initialization failed.");
            throw new IllegalStateException("GLFW init failed");
        }
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);

        handle = glfwCreateWindow(width, height, title, NULL, NULL);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public long getHandle() {
        return handle;
    }

    public void cleanup() {
        glfwDestroyWindow(handle);
        glfwTerminate();
    }
}