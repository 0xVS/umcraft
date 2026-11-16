package um.craft.core.render.ui;

import org.joml.Vector4f;
import um.craft.core.render.Texture;

public class Panel implements UIElement {
    protected float x, y, width, height;
    protected Texture texture;          // null = solid tint
    protected Vector4f tint;
    protected boolean visible = true;

    public Panel(float x, float y, float w, float h, Texture texture, Vector4f tint) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.texture = texture;
        this.tint = tint;
    }

    public Panel(float x, float y, float w, float h, Vector4f tint) {
        this(x, y, w, h, null, tint);
    }

    public void setBounds(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTint(Vector4f tint) {
        this.tint = tint;
    }

    @Override
    public void update(float deltaTime, UIInput input) {
    }

    @Override
    public void render(UIRenderer ui) {
        ui.drawQuad(x, y, width, height, texture, tint);
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
        return width;
    }

    @Override
    public float height() {
        return height;
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
