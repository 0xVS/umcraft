package um.craft.core;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import um.craft.core.debug.DebugContext;
import um.craft.core.input.InputHandler;
import um.craft.core.input.Inputs;
import um.craft.core.logic.GameLoop;
import um.craft.core.logic.PlayerController;
import um.craft.core.render.Shader;
import um.craft.core.render.ui.UIRenderer;
import um.craft.core.scene.SceneContext;
import um.craft.core.scene.SceneManager;
import um.craft.core.scene.menu.MainMenuScene;
import um.craft.core.util.Log;
import um.craft.core.util.Utils;
import um.craft.core.window.WindowManager;


public class Launcher {
    static Shader shader;
    static final String VERSION = "umcraft alpha v1.0";

    public static void main(String[] args) throws Exception {


        Log.info("LWJGL Version: " + Version.getVersion());

        WindowManager wm = new WindowManager(
                Utils.readSettings("settings.json", "window.title", "default_craft"),
                Utils.readSettings("settings.json", "window.width", 900),
                Utils.readSettings("settings.json", "window.height", 600),
                Utils.readSettings("settings.json", "window.vsync", false)
        );

        Inputs inputs = new Inputs();
        glfwInit(wm.getWindowHandle(), wm, inputs);
        InputHandler inputHandler = new InputHandler(inputs);
        PlayerController playerController = new PlayerController(inputs, inputHandler, wm, wm.getCamera());
        UIRenderer uiRenderer = new UIRenderer();
        SceneContext context = new SceneContext(wm, inputs, inputHandler, wm.rm, uiRenderer, wm.rm.getFont(), playerController);
        SceneManager sceneManager = new SceneManager(context);
        context.setSceneManager(sceneManager);

        wm.setPlayerController(playerController);


        sceneManager.setScene(new MainMenuScene());
        // sceneManager.transition();

        // GameLoop gameLoop = new GameLoop(wm, playerController, inputHandler, sceneManager);
        GameLoop gameLoop = new GameLoop(wm, inputHandler, sceneManager);


        /*
        long windowHandle = wm.getWindowHandle();
        glfwInit(windowHandle, wm, inputs);
        GLFW.glfwSetInputMode(windowHandle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED); */



        try {
            gameLoop.start();
        } catch (Exception e){
            Log.error("Spiel konnte nicht gestartet werden", e);
        } finally {
            wm.cleanup();
        }
    }

    public static void glfwInit(long windowHandle, WindowManager wm, Inputs inputs) {
        GLFW.glfwSetKeyCallback(windowHandle, inputs.getKeyboardCallback());
        GLFW.glfwSetMouseButtonCallback(windowHandle, inputs.getMouseButtonsCallback());
        GLFW.glfwSetCursorPosCallback(windowHandle, inputs.getCursorMovementCallback());
    }

    public static String getVersion() {
        return VERSION;
    }
}
