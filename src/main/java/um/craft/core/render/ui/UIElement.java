package um.craft.core.render.ui;

import um.craft.core.render.font.NanoVGFontRenderer;

public interface UIElement {
    float x();
    float y();
    float width();
    float height();

    void update(float deltaTime, UIInput input);
    void render(UIRenderer ui);
    default void renderText(NanoVGFontRenderer font) {}
    default void resize(int windowWidth, int windowHeight) {}
    void cleanup();

    default boolean contains(float px, float py) {
        return px >= x() && px <= x() + width()
                && py >= y() && py <= y() + height();
    }

    boolean isVisible();
    void setVisible(boolean visible);

}
