package um.craft.core.world;

import java.util.HashMap;
import java.util.Map;

public class World {
    private final Map<ChunkPos, Chunk> chunks = new HashMap<>();

    public void addChunk(Chunk chunk) {
        chunks.put(chunk.getPos(), chunk);
    }

    public Chunk getChunk(ChunkPos pos) {
        return chunks.get(pos);
    }

    public Map<ChunkPos, Chunk> getChunks() {
        return chunks;
    }
}
