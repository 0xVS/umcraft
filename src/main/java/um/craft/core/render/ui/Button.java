package um.craft.core.render.ui;

import org.joml.Vector4f;
import um.craft.core.render.Texture;
import um.craft.core.render.font.NanoVGFontRenderer;

public class Button extends Panel {
    private final Label label;
    private final Runnable onClick;

    private final Vector4f normalTint;
    private final Vector4f hoverTint;
    private final Vector4f pressedTint;

    private Texture normalTexture;
    private Texture hoverTexture;
    private Texture pressedTexture;

    private boolean hovered;
    private boolean pressed;

    public Button(float x, float y, float w, float h, String text, Runnable onClick) {
        super(x, y, w, h, new Vector4f(0.16f, 0.19f, 0.24f, 1f));
        this.normalTint = new Vector4f(0.16f, 0.19f, 0.24f, 1f);
        this.hoverTint = new Vector4f(0.24f, 0.29f, 0.36f, 1f);
        this.pressedTint = new Vector4f(0.10f, 0.13f, 0.18f, 1f);
        this.onClick = onClick;
        this.label = new Label(text, x + w / 2f, y + h / 2f - 7f, 2.5f,
                new Vector4f(1f, 1f, 1f, 1f)).align(Label.Align.CENTER);
    }

    public Button setTextures(Texture normal, Texture hover, Texture pressed) {
        this.normalTexture = normal;
        this.hoverTexture = hover;
        this.pressedTexture = pressed;
        return this;
    }

    @Override
    public void update(float deltaTime, UIInput input) {
        hovered = contains(input.getX(), input.getY());

        if (input.isLeftJustPressed() && hovered) {
            pressed = true;
        }

        if (input.isLeftJustReleased()) {
            if (pressed && hovered && onClick != null) {
                onClick.run();
            }
            pressed = false;
        }
    }

    @Override
    public void render(UIRenderer ui) {
        Texture texture = pressed ? pressedTexture : hovered ? hoverTexture : normalTexture;
        Vector4f color = pressed ? pressedTint : hovered ? hoverTint : normalTint;
        ui.drawQuad(x, y, width, height, texture, color);
    }

    @Override
    public void renderText(NanoVGFontRenderer font) {
        label.renderText(font);
    }

    @Override
    public void setBounds(float x, float y, float w, float h) {
        super.setBounds(x, y, w, h);
        label.setPosition(x + w / 2f, y + h / 2f - 7f);
    }

}
