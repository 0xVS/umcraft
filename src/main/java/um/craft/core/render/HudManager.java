package um.craft.core.render;

import um.craft.core.render.HudElement;
import um.craft.core.render.Shader;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;

public class HudManager {
    private final Map<String, HudElement> hudElements = new LinkedHashMap<>();

    public void add(String id, HudElement element) {
        hudElements.put(id, element);
    }

    public void render(Shader hudShader) {
        glDisable(GL_DEPTH_TEST);
        hudShader.bind();
        glActiveTexture(GL_TEXTURE0);

        hudElements.values().stream()
                .filter(HudElement::isVisible)
                .forEach(e -> e.render(hudShader));

        hudShader.unbind();
        glEnable(GL_DEPTH_TEST);
    }

    public void cleanup() {
        hudElements.values().forEach(HudElement::cleanup);
    }

    public HudElement get(String id) {
        return hudElements.get(id);
    }
}