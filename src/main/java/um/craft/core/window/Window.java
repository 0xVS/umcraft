package um.craft.core.window;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.MemoryStack;
import um.craft.core.util.Log;

import java.nio.IntBuffer;

public class Window {
    private final long handle;
    private final String title;
    private int width, height;
    private final boolean vsync;
    private boolean resized;

    public Window(String title, int width, int height, boolean vsync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vsync = vsync;
        this.handle = createWindow();
    }


    private long createWindow() {
        GLFWErrorCallback.create((error, description) ->
                Log.error("GLFW Error [" + error + "]: " + MemoryUtil.memUTF8(description))).set();

        // wenn GLFW nicht initted wurde, dann throw
        if (!GLFW.glfwInit()) throw new IllegalStateException("Fehler beim Initialisieren von GLFW");

        /* default werte fürs init-en */

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE); // fenster ist resizable = true

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);

        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        // nicht maximiert, AUßER:
        boolean maximized = false;

        if (height == 0 || width == 0) {
            height = 100;
            width = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximized = true;
        }

        long window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) throw new RuntimeException("Fehler beim erstellen vom GLFW window");

        // methode, um window size zu aktualisieren beim resizen
        GLFW.glfwSetFramebufferSizeCallback(window, this::onFramebufferResize);

        centerWindow(window, maximized);

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        if (vsync) {
            GLFW.glfwSwapInterval(1);
        }

        GLFW.glfwShowWindow(window);

        return window;
    }


    private void onFramebufferResize(long window, int width, int height) {
        Log.noti("Resized: " + width + " x " + height);
        if (height == 0 || width == 0) return;
        this.width = width;
        this.height = height;
        this.resized = true;
        GL11.glViewport(0, 0, width, height);
    }

    private void centerWindow(long window, boolean maximized) {
        if (maximized) {
            GLFW.glfwMaximizeWindow(window);
            return;
            // zentriert das fenster in der monitorhälfte (in höhe und breite)
        }

        // mein wayland compositor auf arch macht probleme wenn ich es centern will, deswegen muss ich returnen lolol
        if (GLFW.glfwGetPlatform() == GLFW.GLFW_PLATFORM_WAYLAND) return;

        long monitor = GLFW.glfwGetPrimaryMonitor();
        if (monitor == MemoryUtil.NULL) return;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer mx = stack.mallocInt(1);
            IntBuffer my = stack.mallocInt(1);
            IntBuffer mw = stack.mallocInt(1);
            IntBuffer mh = stack.mallocInt(1);

            // die size reicht nicht aus, kann bugs auslösen bei multi monitor setups
            //GLFW.glfwGetWindowSize(window, w, h);
            //GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwGetMonitorWorkarea(monitor, mx, my, mw, mh);

            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            GLFW.glfwGetWindowSize(window, w, h);

            // holt werte aus buffer, zentriert mit echten werten
            /*GLFW.glfwSetWindowPos(window,
                    (vidmode.width() - w.get(0)) / 2,
                    (vidmode.height() - h.get(0)) / 2); */
            GLFW.glfwSetWindowPos(window,
                    mx.get(0) + (mw.get(0) - w.get(0)) / 2,
                    my.get(0) + (mh.get(0) - h.get(0)) / 2);
        }
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(handle);
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    public void destroy() {
        GLFW.glfwDestroyWindow(handle);
        //GLFW.glfwTerminate();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    // getter
    public long getHandle() {
        return handle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVsync() {
        return vsync;
    }

    public boolean isResized(){
        return resized;
    }

    public void resetResized(){
        resized = false;
    }
}
