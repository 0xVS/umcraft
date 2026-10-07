package um.craft.core.render.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import um.craft.core.logic.PlayerController;
import um.craft.core.util.ObjLoader;
import um.craft.core.world.World;
import um.craft.core.world.block.BlockState;
import um.craft.core.render.Mesh;
import um.craft.core.render.Shader;
import um.craft.core.render.Texture;
import um.craft.core.render.hud.CrosshairElement;
import um.craft.core.render.hud.DebugOverlay;
import um.craft.core.render.hud.HudManager;
import um.craft.core.render.hud.HudMesh;
import um.craft.core.util.Utils;
import um.craft.core.window.WindowManager;
import um.craft.core.world.Chunk;
import um.craft.core.world.ChunkPos;
import um.craft.core.world.block.Blocks;

import java.util.Map;

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

    // CHUNK ZEUG
    public final int RENDER_DISTANCE = Utils.readSettings("settings.json", "render.render_distance", 4);

    private World world;

    private Chunk testChunk;
    private Chunk chunk;
    private Chunk chunk2;


    private Chunk fullChunk;
    //

    // PLAYER MDOEL TEST ZEUG
    private Mesh playerMesh;
    private Texture playerTexture;


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



    //private final List<Block> blocks = new ArrayList<>();

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

        crosshairTexture = new Texture("gui/crosshair.png");
        buildCrosshair(windowWidth, windowHeight);


        playerMesh = ObjLoader.load("player_model.obj");
        playerTexture = new Texture("entity/player/debug_player.png");

        world = new World();

        /*

            !!!!!!!!!!!!!!

            HIER SPÄTER WORLDGEN?

            !!!!!!!!!!!!!

         */
        for (int cx = -2; cx <= 2; cx++) {
            for (int cz = -2; cz <= 2; cz++) {
                Chunk chunk = new Chunk(new ChunkPos(cx, cz));

                for (int x = 0; x < Chunk.SIZE; x++) {
                    for (int z = 0; z < Chunk.SIZE; z++) {
                        chunk.setBlock(x, 0, z, Blocks.STONE);
                        chunk.setBlock(x, 1, z, Blocks.DIRT);
                        chunk.setBlock(x, 2, z, Blocks.GRASS);
                    }
                }

                world.addChunk(chunk);
            }
        }

        hudManager.add("crosshair", new CrosshairElement(crosshairMesh, crosshairTexture));

        shader.createVertexShader(Utils.loadResource("assets/shaders/vertex.glsl"));
        shader.createFragmentShader(Utils.loadResource("assets/shaders/fragment.glsl"));
        shader.link();

        meshBlock = new Mesh(blockPositions, blockIndices);
    }

    public void onResize(int width, int height){
        windowHeight = height;
        windowWidth = width;
        projection.updateProjMatrix(width, height);
        updateHudAspect();
    }

    public void updateHudAspect(){
        hudShader.bind();
        hudShader.setFloat("aspect_r", (float) windowWidth / windowHeight);
    }

    private void buildCrosshair(int w, int h){
        float sizeX = 0.05f;
        float sizeY = 0.05f * ((float) windowWidth / windowHeight); // aspect ratio korrektur
        //float sizeY = 0.05f; // * ((float) w / h);
        System.out.println("[DEBUG] crosshair rebuilt: " + windowWidth + "x" + windowHeight
                + " sizeY=" + sizeY);

        float[] crosshairVertices = {
                -sizeX, -sizeY,   0f, 0f,
                sizeX, -sizeY,   1f, 0f,
                sizeX,  sizeY,   1f, 1f,
                -sizeX,  sizeY,   0f, 1f,
        };
        int[] crosshairIndices = { 0, 1, 2,  0, 2, 3 };

        crosshairMesh = new HudMesh(crosshairVertices, crosshairIndices);
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

    public void renderWorld(World world){
        ChunkPos playerChunk = ChunkPos.fromWorld(
                (int) playerController.getPosition().x,
                (int) playerController.getPosition().z
        );

        for (Map.Entry<ChunkPos, Chunk> entry : world.getChunks().entrySet()) {
            ChunkPos pos = entry.getKey();
            int dx = Math.abs(pos.x() - playerChunk.x());
            int dz = Math.abs(pos.z() - playerChunk.z());

            if (dx <= RENDER_DISTANCE && dz <= RENDER_DISTANCE){
                renderChunk(entry.getValue());
            }

        }
    }

    public void render() {
        shader.bind();

        camera.updatePosition(playerController.getPosition());

        shader.setMat4("projectionMatrix", projection.getProjMatrix());
        shader.setMat4("viewMatrix", camera.getViewMatrix());

            //meshBlock.render();
        /*
        renderChunk(testChunk);
        renderChunk(chunk);
        renderChunk(chunk2);

        renderChunk(fullChunk);
*/
        renderWorld(world);

        // SPIELER MODEL TEST ZEUG
        if (camera.getCurrentMode() != CameraMode.FIRST_PERSON) {
            renderPlayerCube();
        }

        shader.unbind();
        //renderHUD();
        hudManager.render(hudShader);
    }

    private void renderPlayerCube() {
        //Vector3f bodyPos = camera.getPlayerBodyPosition();
        Vector3f bodyPos = playerController.getPosition();

        Matrix4f modelMatrix = new Matrix4f()
                .translation(bodyPos.x, bodyPos.y, bodyPos.z)
                // SPÄTER FÜR DEN KOPF DIE ROTATION EINBAUEN, DER KÖRPER SOLL NUR DIE TRANSLATION HABEN DAMIT DER KOPF DER KAMERA FOLGT
                .rotateY((float) Math.toRadians(-camera.getYaw() - 90f));;
        shader.setMat4("modelMatrix", modelMatrix);
        glActiveTexture(GL_TEXTURE0);
        playerTexture.bind();
        shader.setInt("textureSampler", 0);
        playerMesh.render();
    }

    private void renderChunk(Chunk chunk) {
        Mesh mesh = chunk.getOrBuildMesh(blockPositions, blockIndices);
        if (mesh == null) return;

        shader.setMat4("modelMatrix", new Matrix4f()); // Position schon im Mesh eingebaut
        glActiveTexture(GL_TEXTURE0);
        shader.setInt("textureSampler", 0);
        mesh.render();
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

