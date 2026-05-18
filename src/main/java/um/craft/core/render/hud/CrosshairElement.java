package um.craft.core.render.hud;

import um.craft.core.render.Shader;
import um.craft.core.render.Texture;

public class CrosshairElement implements HudElement {
    private final HudMesh crosshairMesh;
    private final Texture crosshairTexture;
    private boolean visible = true;

    public CrosshairElement(HudMesh mesh, Texture texture) {
        this.crosshairMesh = mesh;
        this.crosshairTexture = texture;
    }

    @Override
    public void render(Shader hudShader) {
        crosshairTexture.bind();
        hudShader.setInt("hudTexture", 0);
        crosshairMesh.render();
    }

    @Override
    public void cleanup() { crosshairMesh.cleanup(); }

    @Override public boolean isVisible() { return visible; }
    @Override public void setVisible(boolean v) { visible = v; }
}