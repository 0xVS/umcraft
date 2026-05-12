package um.craft.core.render;

public record CrosshairElement(HudMesh crosshairMesh, Texture crosshairTexture) implements HudElement {
    @Override
    public void render(Shader hudShader) {

    }

    @Override
    public void cleanup() {

    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void setVisible(boolean visible) {

    }
}
