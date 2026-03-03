package um.craft.core;

import org.lwjgl.glfw.GLFW;
import um.craft.core.input.Inputs;

import org.lwjgl.Version;
import um.craft.core.logic.PlayerController;
import um.craft.core.logic.GameLoop;
import um.craft.core.window.WindowManager;


public class Launcher {
    public static void main(String[] args) {
        System.out.println("LWJGL Version: " + Version.getVersion());

        WindowManager wm = new WindowManager("umcraft", 1280, 720, true);

        Inputs inputs = new Inputs();
        long windowHandle = wm.getWindowHandle();

        // TODO: input mit fenster verbinden, muss in WindowManager geschehen eig upsi
        GLFW.glfwSetKeyCallback(windowHandle, inputs.getKeyboardCallback());
        GLFW.glfwSetMouseButtonCallback(windowHandle, inputs.getMouseButtonsCallback());
        GLFW.glfwSetCursorPosCallback(windowHandle, inputs.getCursorMovementCallback());

        System.out.println("[+] Window und Inputs initialisiert");

        PlayerController playerController = new PlayerController(inputs, wm);
        System.out.println("[+] PlayerController initialisiert");

        GameLoop gameLoop = new GameLoop(wm, playerController);
        gameLoop.start();


    }
}
