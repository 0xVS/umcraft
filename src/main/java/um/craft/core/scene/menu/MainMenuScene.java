package um.craft.core.scene.menu;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import um.craft.core.input.InputEvent;
import um.craft.core.input.InputListener;
import um.craft.core.render.Texture;
import um.craft.core.render.font.NanoVGFontRenderer;
import um.craft.core.render.ui.Button;
import um.craft.core.render.ui.Panel;
import um.craft.core.render.ui.UIInput;
import um.craft.core.render.ui.UIManager;
import um.craft.core.scene.Scene;
import um.craft.core.scene.SceneContext;
import um.craft.core.scene.world.WorldScene;

public class MainMenuScene implements Scene {
    private SceneContext context;
    private UIManager ui;
    private Texture titleTexture;

    private final InputListener escapeListener = event -> {
        if (event.type() == InputEvent.Type.PRESSED && event.action().equals("QUIT")) {
            context.getWindow().setShouldClose();
        }
    };

    @Override
    public void onEnter(SceneContext context) {
        this.context = context;
        context.getWindow().setCursorMode(GLFW.GLFW_CURSOR_NORMAL);
        context.getInputHandler().addListener(escapeListener);

        ui = new UIManager(new UIInput(context.getInputs()));
        titleTexture = new Texture("textures/gui/umcraft_title.png");
        layout(context.getWindow().getWidth(), context.getWindow().getHeight());
    }

    private void layout(int w, int h) {
        ui.cleanup();
        ui = new UIManager(new UIInput(context.getInputs()));

        ui.add(new Panel(0, 0, w, h, new Vector4f(0.04f, 0.06f, 0.09f, 1f)));

        float titleW = Math.min(w * 0.55f, 640f);
        float titleH = titleW * titleTexture.height() / titleTexture.width();
        ui.add(new Panel((w - titleW) / 2f, h * 0.12f, titleW, titleH,
                titleTexture, new Vector4f(1f, 1f, 1f, 1f)));

        float bw = 240f, bh = 44f, gap = 12f;
        float bx = (w - bw) / 2f;
        float by = h * 0.55f;

        ui.add(new Button(bx, by, bw, bh, "Spielen",
                () -> context.getScenes().setScene(new WorldScene())));
        ui.add(new Button(bx, by + bh + gap, bw, bh, "Einstellungen",
                () -> context.getScenes().setScene(new SettingsScene())));
        ui.add(new Button(bx, by + 2 * (bh + gap), bw, bh, "Beenden",
                () -> context.getWindow().setShouldClose()));
    }

    @Override public void update(float deltaTime) { ui.update(deltaTime); }

    @Override
    public void render() {
        ui.render(context.getUiRenderer(),
                context.getWindow().getWidth(),
                context.getWindow().getHeight());
    }

    @Override
    public void renderText(NanoVGFontRenderer font) { ui.renderText(font); }

    @Override
    public void resize(int width, int height) { layout(width, height); }

    @Override
    public void onExit() {
        context.getInputHandler().removeListener(escapeListener);
        ui.cleanup();
    }
}