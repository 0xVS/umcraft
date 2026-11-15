package um.craft.core.scene;

import um.craft.core.input.InputHandler;
import um.craft.core.input.Inputs;
import um.craft.core.logic.PlayerController;
import um.craft.core.render.RenderManager;
import um.craft.core.render.font.NanoVGFontRenderer;
import um.craft.core.render.ui.UIRenderer;
import um.craft.core.window.Window;
import um.craft.core.window.WindowManager;
import um.craft.core.world.World;

public class SceneContext {
    private final WindowManager window;
    private final Inputs inputs;
    private final InputHandler inputHandler;
    private final RenderManager renderManager;
    private final UIRenderer uiRenderer;
    private final NanoVGFontRenderer font;
    private final PlayerController playerController;

    private SceneManager sceneManager;

    public SceneContext(WindowManager window, Inputs inputs, InputHandler inputHandler,
                        RenderManager renderManager, UIRenderer uiRenderer,
                        NanoVGFontRenderer font, PlayerController playerController) {
        this.window = window;
        this.inputs = inputs;
        this.inputHandler = inputHandler;
        this.renderManager = renderManager;
        this.uiRenderer = uiRenderer;
        this.font = font;
        this.playerController = playerController;
    }

    public void setSceneManager(SceneManager sceneManager) { this.sceneManager = sceneManager; }
    public SceneManager getScenes() { return sceneManager; }
    public WindowManager getWindow() { return window; }
    public Inputs getInputs() { return inputs; }
    public InputHandler getInputHandler() { return inputHandler; }
    public RenderManager getRenderManager() { return renderManager; }
    public UIRenderer getUiRenderer() { return uiRenderer; }
    public NanoVGFontRenderer getFont() { return font; }
    public PlayerController getPlayerController() { return playerController; }
}
