package um.craft.core;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import um.craft.core.input.Inputs;
import um.craft.core.logic.GameLoop;
import um.craft.core.logic.PlayerController;
import um.craft.core.render.Shader;
import um.craft.core.window.WindowManager;


public class Launcher {
    static Shader shader;

    public static void main(String[] args) throws Exception {
        System.out.println("LWJGL Version: " + Version.getVersion());
        WindowManager wm = new WindowManager("umcraft", 1280, 720, true);

        Inputs inputs = new Inputs();
        long windowHandle = wm.getWindowHandle();

        PlayerController playerController = new PlayerController(inputs, wm);
        GameLoop gameLoop = new GameLoop(wm, playerController);

        wm.rm.setPlayerController(playerController);
        glfwInit(windowHandle, wm, inputs);

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
