package um.craft.core.render.ui;

import org.joml.Vector4f;
import um.craft.core.render.font.NanoVGFontRenderer;

public class Label implements UIElement {
    public enum Align {LEFT, CENTER, RIGHT}

    private float x;
    private float y;
    private float scale;
    private String text;
    private final Vector4f color;
    private Align align = Align.LEFT;
    private boolean visible = true;

    public Label(String text, float x, float y, float scale, Vector4f color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.color = color;
    }

    public Label align(Align align) {
        this.align = align;
        return this;
    }

    public Label text(String text) {
        this.text = text;
        return this;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void renderText(NanoVGFontRenderer font) {
        float drawX = switch (align) {
            case LEFT -> x;
            case CENTER -> x - font.getWidth(text, scale) / 2f;
            case RIGHT -> x - font.getWidth(text, scale);
        };
        font.drawString(text, drawX, y, scale,
                color.x, color.y, color.z, color.w);
    }

    @Override
    public void update(float deltaTime, UIInput input) {
    }

    @Override
    public void render(UIRenderer ui) {
    }

    @Override
    public void cleanup() {
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public float width() {
        return 0;
    }

    @Override
    public float height() {
        return 0;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
