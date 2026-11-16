package um.craft.core.scene.menu;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import um.craft.core.input.InputEvent;
import um.craft.core.input.InputListener;
import um.craft.core.render.font.NanoVGFontRenderer;
import um.craft.core.render.ui.Button;
import um.craft.core.render.ui.Label;
import um.craft.core.render.ui.Panel;
import um.craft.core.render.ui.UIInput;
import um.craft.core.render.ui.UIManager;
import um.craft.core.scene.Scene;
import um.craft.core.scene.SceneContext;

public class SettingsScene implements Scene {
    private SceneContext context;
    private UIManager ui;

    private final InputListener escapeListener = event -> {
        if (event.type() == InputEvent.Type.PRESSED && event.action().equals("QUIT")) {
            goBack();
        }
    };

    @Override
    public void onEnter(SceneContext context) {
        this.context = context;
        context.getWindow().setCursorMode(GLFW.GLFW_CURSOR_NORMAL);
        context.getInputHandler().addListener(escapeListener);
        ui = new UIManager(new UIInput(context.getInputs()));
        layout(context.getWindow().getWidth(), context.getWindow().getHeight());
    }

    private void layout(int w, int h) {
        ui.cleanup();
        ui = new UIManager(new UIInput(context.getInputs()));

        ui.add(new Panel(0, 0, w, h, new Vector4f(0.04f, 0.06f, 0.09f, 1f)));
        ui.add(new Label("Einstellungen", w / 2f, h * 0.2f, 3.5f,
                new Vector4f(1f, 1f, 1f, 1f)).align(Label.Align.CENTER));

        float bw = 240f, bh = 44f;
        float bx = (w - bw) / 2f;
        float by = h * 0.6f;

        ui.add(new Button(bx, by, bw, bh, "Back", this::goBack));
    }

    private void goBack() {
        context.getScenes().setScene(new MainMenuScene());
    }

    @Override
    public void update(float deltaTime) {
        ui.update(deltaTime);
    }

    @Override
    public void render() {
        ui.render(context.getUiRenderer(),
                context.getWindow().getWidth(),
                context.getWindow().getHeight());
    }

    @Override
    public void renderText(NanoVGFontRenderer font) {
        ui.renderText(font);
    }

    @Override
    public void resize(int width, int height) {
        layout(width, height);
    }

    @Override
    public void onExit() {
        context.getInputHandler().removeListener(escapeListener);
        ui.cleanup();
    }
}
