package um.craft.core.render.hud;

import um.craft.core.render.Shader;
import um.craft.core.render.font.NanoVGFontRenderer;

public interface HudElement {
    default void renderText(NanoVGFontRenderer font) {}
    default void resize(int width, int height) {}

    void render(Shader hudShader);
    void cleanup();
    boolean isVisible();
    void setVisible(boolean visible);
}
