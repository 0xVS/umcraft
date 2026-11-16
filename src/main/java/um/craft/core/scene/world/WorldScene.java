package um.craft.core.scene.world;

import org.lwjgl.glfw.GLFW;
import um.craft.core.input.InputEvent;
import um.craft.core.input.InputListener;
import um.craft.core.scene.Scene;
import um.craft.core.scene.SceneContext;
import um.craft.core.scene.menu.MainMenuScene;

public class WorldScene implements Scene {
    private SceneContext context;

    private final InputListener escapeListener = event -> {
        if (event.type() == InputEvent.Type.PRESSED && event.action().equals("QUIT")) {
            context.getScenes().setScene(new MainMenuScene());
        }
    };

    @Override
    public void onEnter(SceneContext context) {
        this.context = context;
        context.getWindow().setCursorMode(GLFW.GLFW_CURSOR_DISABLED);
        context.getInputHandler().addListener(escapeListener);
    }

    @Override
    public void update(float deltaTime) {
        context.getPlayerController().update(deltaTime);
    }

    @Override
    public void render() {
        context.getRenderManager().render();
    }

    @Override
    public void onExit() {
        context.getInputHandler().removeListener(escapeListener);
    }
}
