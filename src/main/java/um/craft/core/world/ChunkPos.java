package um.craft.core.world;

public record ChunkPos(int x, int z) {
    public static ChunkPos fromWorld(int worldX, int worldZ) {
        return new ChunkPos(
                Math.floorDiv(worldX, Chunk.SIZE),
                Math.floorDiv(worldZ, Chunk.SIZE)
        );
    }

    public int worldX() { return x * Chunk.SIZE; }
    public int worldZ() { return z * Chunk.SIZE; }
}
