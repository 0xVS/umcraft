package um.craft.core.window;

import org.joml.Matrix4f;


import org.lwjgl.opengl.*;
import um.craft.core.render.RenderManager;

public class WindowManager {
// neues Zeug für commit lol
    private Window window;
    private final Matrix4f projectionMatrix;

    public static final float FOV = (float) Math.toRadians(60.0f);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000.0f;

    public RenderManager rm;

    public WindowManager(String title, int width, int height, boolean vsync) {
        this.window = new Window(title, width, height, vsync);
        this.projectionMatrix = new Matrix4f();
        initOpenGL();

    }



    public void initOpenGL() {
        GL.createCapabilities();

        //GL11.glClearColor(100.0f, 0.0f, 0.0f, 100.0f);
        //GL11.glViewport(0, 0, width, height);

        this.rm = new RenderManager(this);

        try {
            this.rm.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // TODO: für später? mal schauen wa
        //GL11.glEnable(GL11.GL_DEPTH_TEST);
        //GL11.glEnable(GL11.GL_STENCIL_TEST);

        // cull face -> nur äußere texturen vom objekt werden gerendert, alles andere nicht
        //GL11.glEnable(GL11.GL_CULL_FACE);
        // TODO: später in game logic ersetzen durch implementation wie in mc
        //GL11.glCullFace(GL11.GL_BACK);


        updateProjectionMatrix();
    }



    public void update() {
        // setzt farbe (temporär) für fenster
        //setClearColor(0, 0, 0, 1.0f);
        //GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glClearColor(0.2f, 0.4f, 0.8f, 1.0f);
        rm.render();
        window.swapBuffers();
        // sagt opengl, dass alle jetzigen objekte in der queue gerendert werden sollen
        window.pollEvents();
    }

    public void cleanup() {
        window.destroy();
    }

    public void setClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    public boolean windowShouldClose() {
        return window.shouldClose();
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) window.getWidth() / window.getHeight();
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width,  int height) {
        float aspectRatio = (float) width / height;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public int getWidth() { return window.getWidth(); }

    public int getHeight() {        return window.getHeight();    }

    public long getWindowHandle() {        return window.getHandle();    }
}
