package um.craft.core.logic;

import um.craft.core.window.WindowManager;
import um.craft.core.render.*;

public class GameLoop {

    private final WindowManager windowManager;
    private final PlayerController playerController;
    private boolean isRunning = false;

    Shader shader = new Shader();

    // zeitvariablen für gameloop
    private long lastTime;
    private float deltaTime;
    private int fps;
    private long lastFpsTime;
    private int frameCount;

    public GameLoop(WindowManager windowManager, PlayerController playerController) throws Exception {
        this.windowManager = windowManager;
        this.playerController = playerController;
    }

    public void start() {
        if (isRunning) return;

        isRunning = true;

        lastTime = System.nanoTime();
        lastFpsTime = System.nanoTime();
        frameCount = 0;

        System.out.println("Gameloop gestartet");


        run();
    }

    private void run() {
        // main game loop
        while (isRunning && !windowManager.windowShouldClose()) {
            long currentTime = System.nanoTime();

            deltaTime = (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;

            update(deltaTime);
            updateFPS();
            windowManager.update();
        }
        shader.cleanup();
    }

    private void update(float deltaTime) {
        playerController.update(deltaTime);
    }

    private void updateFPS() {
        frameCount++;
        long currentTime = System.nanoTime();

        if (currentTime - lastFpsTime >= 1_000_000_000) {
            fps = frameCount;
            frameCount = 0;
            lastFpsTime = currentTime;

            System.out.println("FPS: " + fps + " | Delta: " + String.format("%.6f", deltaTime));
        }
    }

    private void stop() {
        isRunning = false;
    }
}
