package um.craft.core.render.ui;

import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import um.craft.core.render.Shader;
import um.craft.core.render.Texture;
import um.craft.core.util.Utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class UIRenderer {
    private final Shader shader;
    private final Texture white;
    private final int vao, vbo, ibo;

    public UIRenderer() throws Exception {
        shader = new Shader();
        shader.createVertexShader(Utils.loadResource("assets/shaders/ui_vertex.glsl"));
        shader.createFragmentShader(Utils.loadResource("assets/shaders/ui_fragment.glsl"));
        shader.link();

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, (long) 4 * 4 * Float.BYTES, GL_DYNAMIC_DRAW);

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer indices = stack.mallocInt(6).put(new int[]{0, 1, 2, 0, 2, 3}).flip();
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        }

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2L * Float.BYTES);
        glEnableVertexAttribArray(1);
        glBindVertexArray(0);

        white = createWhiteTexture();
    }

    private static Texture createWhiteTexture() {
        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer pixel = stack.malloc(4);
            pixel.put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 255).flip();
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 1, 1, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixel);
        }
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        return new Texture(id, 1, 1);
    }

    public void begin(int screenWidth, int screenHeight) {
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        shader.bind();
        shader.setInt("uiTexture", 0);
        shader.setVec2("screenSize", screenWidth, screenHeight);
    }


    public void drawQuad(float x, float y, float w, float h, Texture texture, Vector4f tint, float u0, float v0, float u1, float v1) {
        float[] vertices = {
                x, y, u0, v0,
                x + w, y, u1, v0,
                x + w, y + h, u1, v1,
                x, y + h, u0, v1,
        };

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            buffer.put(vertices).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
        }

        shader.setVec4("tint", tint);
        glActiveTexture(GL_TEXTURE0);
        (texture != null ? texture : white).bind();

        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void drawQuad(float x, float y, float w, float h, Texture texture, Vector4f tint) {
        // Texturen laden wir mit stbi_set_flip_vertically_on_load(true) -> V-Achse für UI invertieren
        drawQuad(x, y, w, h, texture, tint, 0f, 1f, 1f, 0f);
    }

    public void end() {
        shader.unbind();
        glDisable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
    }

    public void cleanup() {
        shader.cleanup();
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);
        glDeleteVertexArrays(vao);
    }
}
