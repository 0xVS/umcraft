package um.craft.core.debug;

import um.craft.core.Launcher;
import um.craft.core.logic.PlayerController;
import um.craft.core.render.scene.Camera;
import um.craft.core.world.World;

import java.util.function.IntSupplier;

public final class DebugContext {
    private static Camera camera;
    private static PlayerController player;
    private static World world;
    private static IntSupplier fps = () -> 0;

    private DebugContext() {
    }

    public static void setCamera(Camera camera) {
        DebugContext.camera = camera;
    }

    public static void setPlayer(PlayerController player) {
        DebugContext.player = player;
    }

    public static void setWorld(World world) {
        DebugContext.world = world;
    }

    public static void setFpsSupplier(IntSupplier fpsSupplier) {
        DebugContext.fps = fpsSupplier != null ? fpsSupplier : () -> 0;
    }

    public static Camera camera() {
        return camera;
    }

    public static PlayerController player() {
        return player;
    }

    public static World world() {
        return world;
    }

    public static int fps() {
        return fps.getAsInt();
    }

}
