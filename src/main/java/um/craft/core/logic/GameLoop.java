package um.craft.core.logic;

import um.craft.core.debug.DebugContext;
import um.craft.core.input.InputHandler;
import um.craft.core.scene.SceneManager;
import um.craft.core.util.Log;
import um.craft.core.window.WindowManager;
// import um.craft.core.render.*;

public class GameLoop {

    private final WindowManager windowManager;
    // private final PlayerController playerController;
    private boolean isRunning = false;

    // final Shader shader = new Shader();

    // zeitvariablen für gameloop
    private long lastTime;
    private float deltaTime;
    private long lastFpsTime;
    private int frameCount;
    private int fps;
    private SceneManager sceneManager;
    private InputHandler inputHandler;

    // public GameLoop(WindowManager windowManager, PlayerController playerController, InputHandler inputHandler, SceneManager sceneManager) throws Exception {
    //     this.windowManager = windowManager;
    //     this.playerController = playerController;
    // }
    public GameLoop(WindowManager windowManager, InputHandler inputHandler, SceneManager sceneManager) {
        this.windowManager = windowManager;
        this.inputHandler = inputHandler;
        this.sceneManager = sceneManager;
    }

    public void start() {
        if (isRunning) return;

        isRunning = true;
        DebugContext.setFpsSupplier(this::getFps);

        lastTime = System.nanoTime();
        lastFpsTime = System.nanoTime();
        frameCount = 0;

        Log.debug("Gameloop wurde gestartet");

        run();
    }

    private void run() {
        // main game loop
        while (isRunning && !windowManager.windowShouldClose()) {
            long currentTime = System.nanoTime();

            deltaTime = (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;

            windowManager.pollEvents();

            // update(deltaTime);
            inputHandler.update();
            sceneManager.update(deltaTime);

            if (windowManager.beginFrame()) {
                sceneManager.resize(windowManager.getWidth(), windowManager.getHeight());
            }
            sceneManager.render();

            updateFPS();
            // windowManager.update();
            windowManager.endFrame();
        }
        // shader.cleanup();
    }

    // private void update(float deltaTime) {
    //     playerController.update(deltaTime);
    // }

    private void updateFPS() {
        frameCount++;
        long currentTime = System.nanoTime();

        if (currentTime - lastFpsTime >= 1_000_000_000) {
            fps = frameCount;
            frameCount = 0;
            lastFpsTime = currentTime;
        }
    }

    public int getFps() {
        return fps;
    }

    private void stop() {
        isRunning = false;
    }
}
