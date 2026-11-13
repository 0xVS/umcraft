package um.craft.core.render.hud;

import org.joml.Vector3f;
import um.craft.core.Launcher;
import um.craft.core.debug.DebugContext;
import um.craft.core.world.Chunk;

import java.util.function.Supplier;

public enum DebugInfo {
    VERSION(Launcher::getVersion),
    POSITION(() -> {
        Vector3f position = DebugContext.player().getPosition();
        return String.format("x: %.2f y: %.2f z: %.2f", position.x, position.y, position.z);
    }),
    ROTATION(() -> String.format("yaw: %.2f pitch: %.2f",
            DebugContext.camera().getYaw(),
            DebugContext.camera().getPitch())),
    FPS(() -> "FPS: " + DebugContext.fps()),
    RAM(() -> {
        Runtime runtime = Runtime.getRuntime();
        long used = (runtime.totalMemory() - runtime.freeMemory()) / (1024L * 1024L);
        long max = runtime.maxMemory() / (1024L * 1024L);
        return String.format("RAM: %d / %d MB", used, max);
    }),
    CHUNK_POS(() -> {
        Vector3f position = DebugContext.player().getPosition();
        return String.format("chunk: %d %d",
                (int) Math.floor(position.x / Chunk.SIZE),
                (int) Math.floor(position.z / Chunk.SIZE));
    }),
    LOADED_CHUNKS(() -> "loaded chunks: " + DebugContext.world().getChunks().size());

    private final Supplier<String> output;

    DebugInfo(Supplier<String> output) {
        this.output = output;
    }

    public String get() {
        return output.get();
    }
}
