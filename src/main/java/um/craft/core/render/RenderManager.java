package um.craft.core.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import um.craft.core.logic.PlayerController;
import um.craft.core.util.Utils;
import um.craft.core.window.WindowManager;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;


// ERROR: setVec2 throwed error momentan
public class RenderManager {
    private Shader shader;
    private Mesh meshBlock;
    private Texture texture;
    private final Camera camera;
    private WindowManager wm;
    private Projection projection;
    float scale = 0.5f;

    private int windowWidth;
    private int windowHeight;

    // GUI zeug

    private Shader hudShader;
    private HudMesh crosshairMesh;
    private Texture crosshairTexture;

    private HudManager hudManager;
    private DebugOverlay debugOverlay;
    private CrosshairElement crosshairElement;
    // -------------

    private PlayerController playerController;
    private final float[] blockPositions = {
            // vorne                UV
            -0.5f, -0.5f,  0.5f,   0f, 0f,
            0.5f, -0.5f,  0.5f,    1f, 0f,
            0.5f,  0.5f,  0.5f,    1f, 1f,
            -0.5f,  0.5f,  0.5f,   0f, 1f,

            // hinten               UV
            0.5f, -0.5f, -0.5f,   0f, 0f,
            -0.5f, -0.5f, -0.5f,  1f, 0f,
            -0.5f,  0.5f, -0.5f,  1f, 1f,
            0.5f,  0.5f, -0.5f,   0f, 1f,

            // rechts               UV
            0.5f, -0.5f,  0.5f,   0f, 0f,
            0.5f, -0.5f, -0.5f,   1f, 0f,
            0.5f,  0.5f, -0.5f,   1f, 1f,
            0.5f,  0.5f,  0.5f,   0f, 1f,

            // links                UV
            -0.5f, -0.5f, -0.5f,   0f, 0f,
            -0.5f, -0.5f,  0.5f,   1f, 0f,
            -0.5f,  0.5f,  0.5f,   1f, 1f,
            -0.5f,  0.5f, -0.5f,   0f, 1f,

            // oben                 UV
            -0.5f,  0.5f,  0.5f,   0f, 0f,
            0.5f,  0.5f,  0.5f,    1f, 0f,
            0.5f,  0.5f, -0.5f,    1f, 1f,
            -0.5f,  0.5f, -0.5f,   0f, 1f,

            // unten                UV
            -0.5f, -0.5f, -0.5f,   0f, 0f,
            0.5f, -0.5f, -0.5f,    1f, 0f,
            0.5f, -0.5f,  0.5f,    1f, 1f,
            -0.5f, -0.5f,  0.5f,   0f, 1f,
    };

    // würfel hat 6 flächen = 1 fläche besteht aus 2 dreiecken,
    // 6 flächen = 12 dreiecke = 36 indices insg.

    private final int[] blockIndices = {
            0, 1, 2, 0, 2, 3,       // vorne
            4, 5, 6, 4, 6, 7,       // hinten
            8, 9, 10, 8, 10, 11,    // rechts
            12, 13, 14, 12, 14, 15, // links
            16, 17, 18, 16, 18, 19, // oben
            20, 21, 22, 20, 22, 23, // unten
    };


    private final List<Block> blocks = new ArrayList<>();

    public RenderManager(WindowManager wm){
        this.wm = wm;
        this.camera = new Camera();
        this.windowWidth = wm.getWidth();
        this.windowHeight = wm.getHeight();
    }

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public void init() throws Exception {
        this.shader = new Shader();
        projection = new Projection(wm.getWidth(), wm.getHeight());
        System.out.println("[INFO] Window size: " + wm.getWidth() + "x" + wm.getHeight());
        System.out.println("[DEBUG] ProjMatrix: \n" + projection.getProjMatrix());

        hudShader = new Shader();
        hudShader.createVertexShader(Utils.loadResource("assets/shaders/hud_vertex.glsl"));
        hudShader.createFragmentShader(Utils.loadResource("assets/shaders/hud_fragment.glsl"));
        hudShader.link();

        hudManager = new HudManager();
        debugOverlay = new DebugOverlay(windowWidth, windowHeight);
        debugOverlay.addLine(() -> String.format("x: %.2f y: %.2f z: %.2f",
                playerController.getPosition().x,
                playerController.getPosition().y,
                playerController.getPosition().z));
        debugOverlay.addLine(() -> String.format("yaw: %.2f pitch: %.2f",
                camera.getYaw(),
                camera.getPitch()));
        hudManager.add("debugOverlay", debugOverlay);
        //hudManager.add("crosshair")

        float sizeX = 0.05f;
        float sizeY = 0.05f * ((float) windowWidth / windowHeight); // aspect ratio korrektur

        float[] crosshairVertices = {
                -sizeX, -sizeY,   0f, 0f,
                sizeX, -sizeY,   1f, 0f,
                sizeX,  sizeY,   1f, 1f,
                -sizeX,  sizeY,   0f, 1f,
        };
        int[] crosshairIndices = { 0, 1, 2,  0, 2, 3 };


        crosshairMesh = new HudMesh(crosshairVertices, crosshairIndices);
        crosshairTexture = new Texture("gui/crosshair_neutral.png");

        hudManager.add("crosshair", new CrosshairElement(crosshairMesh, crosshairTexture));

        shader.createVertexShader(Utils.loadResource("assets/shaders/vertex.glsl"));
        shader.createFragmentShader(Utils.loadResource("assets/shaders/fragment.glsl"));
        shader.link();

        meshBlock = new Mesh(blockPositions, blockIndices);

        blocks.add(new Block(new Vector3f(0, 0, -2), new Texture("blocks/dirt.png")));
        blocks.add(new Block(new Vector3f(1, 0, -2), new Texture("blocks/dirt_coarse.png")));
        blocks.add(new Block(new Vector3f(2, 0, -2), new Texture("blocks/grass_block_side.png")));
        blocks.add(new Block(new Vector3f(3, 0, -2), new Texture("blocks/sand.png")));
        blocks.add(new Block(new Vector3f(4, 0, -2), new Texture("blocks/stone.png")));
        blocks.add(new Block(new Vector3f(5, 0, -2), new Texture("blocks/cobblestone.png")));
        blocks.add(new Block(new Vector3f(6, 0,-2), new Texture("blocks/ore_diamond.png")));
        blocks.add(new Block(new Vector3f(7, 0,-2), new Texture("blocks/ore_coal.png")));
        blocks.add(new Block(new Vector3f(8, 0,-2), new Texture("blocks/ore_emerald.png")));
        blocks.add(new Block(new Vector3f(9, 0,-2), new Texture("blocks/debug.png")));
        //texture = new Texture("blocks/cobblestone.png");
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

        shader.setMat4("projectionMatrix", projection.getProjMatrix());
        shader.setMat4("viewMatrix", camera.getViewMatrix());

        for (Block block : blocks) {
            Matrix4f modelMatrix = new Matrix4f()
                    .translation(block.position());
            glActiveTexture(GL_TEXTURE0);

            block.texture().bind();
            shader.setInt("textureSampler", 0);
            shader.setMat4("modelMatrix", modelMatrix);
            meshBlock.render();
        }

        shader.unbind();
        //renderHUD();
        hudManager.render(hudShader);
    }

    // render() – NACH dem 3D-rendering aufrufen
    private void renderHUD() {
        glDisable(GL_DEPTH_TEST); // HUD immer vorne

        hudShader.bind();
        glActiveTexture(GL_TEXTURE0);
        crosshairTexture.bind();
        hudShader.setInt("hudTexture", 0);

        crosshairMesh.render();
        hudShader.unbind();

        glEnable(GL_DEPTH_TEST);
    }

    public void toggleF3(){
        hudManager.get("debugOverlay").setVisible(!hudManager.get("debugOverlay").isVisible());
    }

    public void cleanup() {
        shader.cleanup();
        meshBlock.cleanup();
    }



    public Camera getCamera() { return camera; }
}

