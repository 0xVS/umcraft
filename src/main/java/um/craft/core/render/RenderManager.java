package um.craft.core.render;

import org.joml.Matrix4f;
import um.craft.core.logic.PlayerController;
import um.craft.core.util.Utils;
import um.craft.core.window.WindowManager;


// ERROR: setVec2 throwed error momentan
public class RenderManager {
    private Shader shader;
    private Mesh meshBlock;
    private final Camera camera;
    private final WindowManager wm;
    private Projection projection;
    float scale = 0.5f;



    private PlayerController playerController;
    private final float[] blockPositions = {
            // vorne
            -0.5f, -0.5f,  0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,

            // hinten
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f
    };

    // würfel hat 6 flächen = 1 fläche besteht aus 2 dreiecken,
    // 6 flächen = 12 dreiecke = 36 indices insg.
    private final int[] blockIndices = {
            // vorne
            0, 1, 2,  0, 2, 3,
            // hinten
            4, 6, 5,  4, 7, 6,
            // rechts
            1, 5, 6,  1, 6, 2,
            // links
            4, 0, 3,  4, 3, 7,
            // oben
            3, 2, 6,  3, 6, 7,
            // unten
            4, 5, 1,  4, 1, 0
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



        shader.createVertexShader(Utils.loadResource("vertex.glsl"));
        shader.createFragmentShader(Utils.loadResource("fragment.glsl"));
        shader.link();

        meshBlock = new Mesh(blockPositions, blockIndices);
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

    public void render() {
        shader.bind();

        camera.setFirstPersonPosition(playerController.getPosition());

        Matrix4f modelMatrix = new Matrix4f()
                .translation(0f, 0f, -2f);

        shader.setMat4("projectionMatrix", projection.getProjMatrix());
        shader.setMat4("viewMatrix", camera.getViewMatrix());
        shader.setMat4("modelMatrix", modelMatrix);

        meshBlock.render();

        shader.unbind();
    }

    public void cleanup() {
        shader.cleanup();
        meshBlock.cleanup();
    }

    public Camera getCamera() { return camera; }
}

