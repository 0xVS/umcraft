package um.craft.core;

import org.joml.Matrix4f;


import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;


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

        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) throw new RuntimeException("Fehler beim erstellen vom GLFW window");

        // methode, um window size zu aktualisieren beim resizen
        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.height = height;
            this.width = width;
        });

        // eine art listener, aktualisiert sich bei key presses
        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true);
            } else if (key == GLFW.GLFW_KEY_L && action == GLFW.GLFW_RELEASE) {
                System.out.println("!!! Legat !!!");
            }
        });
        // GLFW.glfwSetCursorPosCallback(window, (window, x, y) -> {})

        if (maximized) {
            GLFW.glfwMaximizeWindow(window);
            // zentriert das fenster in der monitorhälfte (in höhe und breite)
        } else {
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, (vidmode.width() - width) / 2,  (vidmode.height() - height) / 2);
        }

        GLFW.glfwMakeContextCurrent(window);

        if (vsync) {
            GLFW.glfwSwapInterval(1);
        }

        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //GL11.glViewport(0, 0, width, height);

        // TODO: für später? mal schauen wa
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        // cull face -> nur äußere texturen vom objekt werden gerendert, alles andere nicht
        GL11.glEnable(GL11.GL_CULL_FACE);
        // TODO: später in game logic ersetzen durch implementation wie in mc
        GL11.glCullFace(GL11.GL_BACK);
    }

    public void update() {
        GLFW.glfwSwapBuffers(window);
        // sagt opengl, dass alle jetzigen objekte in der queue gerendert werden sollen
        GLFW.glfwPollEvents();
    }

    public void cleanup() {
        GLFW.glfwDestroyWindow(window);
    }

    public void ichLiebeFarben(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    public boolean isKeyPressed(int keycode) {
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle() {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public boolean isResize() {
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindow() {
        return window;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width,  int height) {
        float aspectRatio = (float) width / height;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

}
