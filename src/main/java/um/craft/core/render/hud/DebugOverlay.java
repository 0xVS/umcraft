package um.craft.core.render.hud;

import um.craft.core.render.Shader;
import um.craft.core.render.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DebugOverlay implements HudElement {
    private boolean visible = false;
    private HudMesh backgroundMesh;
    private Texture backgroundTexture;

    private final List<Supplier<String>> lines = new ArrayList<>();

    public DebugOverlay(int windowWidth, int windowHeight) {

        float x = -0.98f;
        float y =  0.95f;
        float w =  0.5f;
        float h =  0.4f;

        float[] vertices = {
                x,     y - h,   0f, 0f,
                x + w, y - h,   1f, 0f,
                x + w, y,       1f, 1f,
                x,     y,       0f, 1f,
        };
        int[] indices = { 0, 1, 2, 0, 2, 3 };

        backgroundMesh    = new HudMesh(vertices, indices);
        backgroundTexture = new Texture("gui/debug_bg.png");
    }

    public void addLine(Supplier<String> line) {
        lines.add(line);
    }

    @Override
    public void render(Shader hudShader) {
        backgroundTexture.bind();
        hudShader.setInt("hudTexture", 0);
        backgroundMesh.render();

        // TODO: text-quads pro zeile rendern (mit FontRenderer)
        lines.stream().map(Supplier::get).forEach(System.out::println); // Platzhalter
    }

    @Override
    public void cleanup() {
        backgroundMesh.cleanup();
    }

    @Override public boolean isVisible()        { return visible; }
    @Override public void setVisible(boolean v) { visible = v;    }
}