package um.craft.core.render;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import um.craft.core.render.Shader;


import org.lwjgl.system.MemoryUtil;
import um.craft.core.util.Utils;
import um.craft.core.window.WindowManager;
import um.craft.core.logic.PlayerController;

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

    private PlayerController playerController;

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }


    public void init() throws Exception {
        this.shader = new Shader();
        float scale = 0.1f;
        float[] vertices = new float[]{
                0.0f * scale, 0.5f * scale, 0.0f * scale,
                -0.5f * scale, -0.5f * scale, 0.0f * scale,
                0.5f * scale, -0.5f * scale, 0.0f * scale,
        };

        //shader.createVertexShader(Utils.loadResource(System.getProperty("user.dir") + "/src/main/java/um/craft/core/render/vertex.vs"));
        shader.createVertexShader(Utils.loadResource(System.getProperty("user.dir") + "/src/main/java/um/craft/core/render/vertex.glsl"));
        //shader.createFragmentShader(Utils.loadResource(System.getProperty("user.dir") + "/src/main/java/um/craft/core/render/fragment.fs"));
        shader.createFragmentShader(Utils.loadResource(System.getProperty("user.dir") + "/src/main/java/um/craft/core/render/fragment.glsl"));
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

        shader.bind();


        if (playerController != null) {
            Vector3f position = playerController.getPosition();
            shader.setVec2("offset", position.x, position.y);
        } else {
            System.err.println("PlayerController konnte nicht gesetzt werden");
        }

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
