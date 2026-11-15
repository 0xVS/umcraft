package um.craft.core.render;

import um.craft.core.util.Utils;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.glBindTexture;

public record Texture(int id, int width, int height) {
    public Texture(String path) {
        this(Utils.loadTextureData(path));
    }

    private Texture(Utils.LoadedTexture data) {
        this(data.id(), data.width(), data.height());
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }
}
