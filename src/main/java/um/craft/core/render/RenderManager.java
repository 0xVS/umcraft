package um.craft.core.render;

import org.lwjgl.opengl.GL11;
import um.craft.core.render.Shader;


import org.lwjgl.system.MemoryUtil;
import um.craft.core.util.Utils;
import um.craft.core.window.WindowManager;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;




public class RenderManager {
    static int vaoId;
    static int vboId;
    private Shader shader;
    static WindowManager wm;


    public void init() throws Exception {
        this.shader = new Shader();
        float[] vertices = new float[]{
                0.0f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
        };

        shader.createVertexShader(Utils.loadResource(System.getProperty("user.dir") + "/src/main/java/um/craft/core/render/vertex.vs"));
        shader.createFragmentShader(Utils.loadResource(System.getProperty("user.dir") + "/src/main/java/um/craft/core/render/fragment.fs"));
        shader.link();

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        //memFree(verticesBuffer);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Unbind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Unbind the VAO
        glBindVertexArray(0);

        if (verticesBuffer != null) {
            MemoryUtil.memFree(verticesBuffer);
        }
    }



    public void render() {
        //clear();
        shader.bind();

        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);

        // Draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, 3);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shader.unbind();
    }

    public void cleanup() {
        if (shader != null){
            shader.cleanup();
        }

        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
