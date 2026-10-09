package um.craft.core.render.hud;

import um.craft.core.render.Shader;

public interface HudElement {
    void render(Shader hudShader);
    void cleanup();
    boolean isVisible();
    void setVisible(boolean visible);
}
