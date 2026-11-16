package um.craft.core.render.ui;

import um.craft.core.render.font.NanoVGFontRenderer;

import java.util.ArrayList;
import java.util.List;

public class UIManager {
    private final List<UIElement> elements = new ArrayList<>();
    private final UIInput input;

    public UIManager(UIInput input) { this.input = input; }

    public void add(UIElement element) { elements.add(element); }

    public void update(float deltaTime) {
        input.update();
        elements.forEach(e -> e.update(deltaTime, input));
    }

    public void render(UIRenderer ui, int width, int height) {
        ui.begin(width, height);
        elements.stream().filter(UIElement::isVisible).forEach(e -> e.render(ui));
        ui.end();
    }

    public void renderText(NanoVGFontRenderer font) {
        elements.stream().filter(UIElement::isVisible).forEach(e -> e.renderText(font));
    }

    public void resize(int width, int height) {
        elements.forEach(e -> e.resize(width, height));
    }

    public void cleanup() {
        elements.forEach(UIElement::cleanup);
        elements.clear();
    }

}
