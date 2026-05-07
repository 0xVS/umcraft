package um.craft.core.render;

import org.joml.Matrix4f;
import um.craft.core.logic.PlayerController;
import um.craft.core.util.Utils;
import um.craft.core.window.WindowManager;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;


// ERROR: setVec2 throwed error momentan
public class RenderManager {
    static int vaoId;
    static int vboId;
    static int iboId;
    private Shader shader;
    private Camera camera;
    static WindowManager wm;
    private Projection projection;


    private PlayerController playerController;
    private float scale = 0.10f;
    private float[] positions = new float[]{
            -0.5f * scale, 0.5f * scale, 0.0f * scale,
            -0.5f * scale, -0.5f * scale, 0.0f * scale,
            0.5f * scale, -0.5f * scale, 0.0f * scale,
            0.5f * scale, 0.5f * scale, 0.0f * scale,
    };
    private float[] positionssss = new float[]{
            -0.5f * scale, 0.5f * scale, 0.0f * scale,
            -0.5f * scale, -0.5f * scale, 0.0f * scale,
            0.5f * scale, -0.5f * scale, 0.0f * scale,
            0.5f * scale, 0.5f * scale, 0.0f * scale,
    };

    private float[] poses = new float[]{
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f,  0.5f, 0.0f,
    };
    private int[] indices = new int[]{
            0, 1, 3, 3, 1, 2
    };


    public RenderManager(WindowManager wm){
        this.wm = wm;
        this.camera = new Camera();
    }


    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public void init() throws Exception {
        this.shader = new Shader();
        projection = new Projection(wm.getWidth(), wm.getHeight());
        System.out.println("Window size: " + wm.getWidth() + "x" + wm.getHeight());
        System.out.println("ProjMatrix: \n" + projection.getProjMatrix());

        final float[] VERTICES = new float[]{
                // V1
                -0.5f * scale, 0.5f * scale, 0.0f * scale,
                // V2
                -0.5f * scale, -0.5f * scale, 0.0f * scale,
                // V4
                0.5f * scale, 0.5f * scale, 0.0f * scale,
        };

        final float[] VERTICES2 = new float[]{
            0.5f * scale, 0.5f * scale, 0.0f * scale,
            0.5f * scale, -0.5f * scale, 0.0f * scale,
            -0.5f * scale, -0.5f * scale, 0.0f * scale,
        };

        shader.createVertexShader(Utils.loadResource(System.getProperty("user.dir") + "/src/main/java/um/craft/core/render/vertex.glsl"));
        shader.createFragmentShader(Utils.loadResource(System.getProperty("user.dir") + "/src/main/java/um/craft/core/render/fragment.glsl"));
        shader.link();

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
        verticesBuffer.put(positions).flip();

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();

        // 1. VAO binden
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        //memFree(verticesBuffer);

        iboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);


        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // Unbind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        // Unbind the VAO
        glBindVertexArray(0);

        if (verticesBuffer != null && indicesBuffer != null) {
            MemoryUtil.memFree(verticesBuffer);
            MemoryUtil.memFree(indicesBuffer);
        }
    }



    /* TODO:Cube rendern und VBO anpassen
        - Struktur:
            6----7
           /|   /|
          2----3 |
          | 4--|-5
          |/   |/
          0----1
        −
        - Parameter anpassen:
            - position (x, y, z)
            - uv-coords / welcher teil von textur wird gemappt? (u, v)
            - normale / für licht später (nx, ny, nz)
    */





    /*
    public void render() {
        shader.bind();
        shader.setFloat("aspectRatio", (float) wm.getWidth() / wm.getHeight());
        camera.followPlayer(playerController.getPosition());
        shader.setMat4("viewMatrix", camera.getViewMatrix());
        shader.setMat4("projectionMatrix", projection.getProjMatrix());
        //shader.setVec2("offset", 0f, 0f);

        if (playerController != null) {
            //Vector3f position = playerController.getPosition();
            Vector3f position = new Vector3f(1.0f, 1.0f, 0.0f);
            shader.setVec2("offset", position.x, position.y);
        } else {
            System.err.println("PlayerController konnte nicht gesetzt werden");
        }

        // Bind to the VAO
        glBindVertexArray(vaoId);
        //glEnableVertexAttribArray(0);

        // Draw the vertices
        //glDrawArrays(GL_TRIANGLES, 0, 3);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shader.unbind();
    }
    */

    public void render() {
        shader.bind();
        shader.setMat4("projectionMatrix", projection.getProjMatrix());
        //shader.setMat4("viewMatrix", camera.getViewMatrix());
        //shader.setVec2("offset", 0f, 0f);
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
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

        glDeleteBuffers(iboId);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}

