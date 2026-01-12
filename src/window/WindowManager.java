package window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class WindowManager {

    private long windowId;
    private int width;
    private int height;
    private String title;

    private float lastFrameTime;
    private float delta;

    public WindowManager(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void create() {

        if (!glfwInit()) {
            throw new IllegalStateException("GLFW initialization failed");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        windowId = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowId == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwMakeContextCurrent(windowId);
        glfwSwapInterval(1); // VSync
        glfwShowWindow(windowId);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.5f, 0.7f, 1.0f, 1.0f);

        lastFrameTime = getTime();
    }

    public void update() {
        glfwSwapBuffers(windowId);
        glfwPollEvents();

        float currentTime = getTime();
        delta = currentTime - lastFrameTime;
        lastFrameTime = currentTime;
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(windowId);
    }

    public float getDeltaTime() {
        return delta;
    }

    public long getWindowId() {
        return windowId;
    }

    public void destroy() {
        glfwDestroyWindow(windowId);
        glfwTerminate();
    }

    private float getTime() {
        return (float) GLFW.glfwGetTime();
    }
}
