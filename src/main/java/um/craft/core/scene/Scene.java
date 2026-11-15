package um.craft.core.scene;

import um.craft.core.render.font.NanoVGFontRenderer;

public interface Scene {
    void onEnter(SceneContext context);
    void onExit();
    void update(float deltaTime);
    void render();
    default void renderText(NanoVGFontRenderer font) {}
    default void resize(int width, int height) {}

}
