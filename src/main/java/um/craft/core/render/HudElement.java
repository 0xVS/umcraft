package um.craft.core.render;

public interface HudElement {
    void render(Shader hudShader);
    void cleanup();
    boolean isVisible();
    void setVisible(boolean visible);
}
