package um.craft.core;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import um.craft.core.input.Inputs;
import um.craft.core.logic.GameLoop;
import um.craft.core.logic.PlayerController;
import um.craft.core.render.Shader;
import um.craft.core.util.Utils;
import um.craft.core.window.WindowManager;


public class Launcher {
    static Shader shader;

    public static void main(String[] args) throws Exception {
        System.out.println("[INFO] LWJGL Version: " + Version.getVersion());

        WindowManager wm = new WindowManager(
                Utils.readSettings("settings.json", "window.title", "default_craft"),
                Utils.readSettings("settings.json", "window.width", 900),
                Utils.readSettings("settings.json", "window.height", 600),
                Utils.readSettings("settings.json", "window.vsync", false)
        );

        Inputs inputs = new Inputs();
        long windowHandle = wm.getWindowHandle();

        PlayerController playerController = new PlayerController(inputs, wm, wm.rm.getCamera());
        GameLoop gameLoop = new GameLoop(wm, playerController);

        wm.rm.setPlayerController(playerController);
        glfwInit(windowHandle, wm, inputs);
        GLFW.glfwSetInputMode(windowHandle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        try {
            gameLoop.start();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //wm.rm.cleanup();
            wm.cleanup();
        }
    }

    public static void glfwInit(long windowHandle, WindowManager wm, Inputs inputs) {
        GLFW.glfwSetKeyCallback(windowHandle, inputs.getKeyboardCallback());
        GLFW.glfwSetMouseButtonCallback(windowHandle, inputs.getMouseButtonsCallback());
        GLFW.glfwSetCursorPosCallback(windowHandle, inputs.getCursorMovementCallback());
    }
}
