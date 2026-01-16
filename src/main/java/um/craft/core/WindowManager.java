package um.craft.core;

import org.joml.Matrix4f;


import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL46C;


public class WindowManager {

    public static final float FOV = (float) Math.toRadians(60.0f);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000.0f;

    private final String title;

    private int width, height;
    private long window;

    private boolean resize, vsync;

    private final Matrix4f projectionMatrix;

    public WindowManager(String title, int width, int height, boolean vsync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vsync = vsync;
        projectionMatrix = new Matrix4f();
    }

    // init methode mit GLFW (lib für OpenGL programmierung)
    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        // wenn GLFW nicht initted wurde, dann throw
        if (!GLFW.glfwInit()) throw new IllegalStateException("Fehler beim Initialisieren von GLFW");

        /* default werte fürs init-en */

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL40.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // fenster ist resizable = true

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);

        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL40.GL_TRUE);

    }
}
