package um.craft.core.world;

import um.craft.core.render.Mesh;
import um.craft.core.world.block.BlockState;
import um.craft.core.world.block.BlockType;
import um.craft.core.world.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
    public static final int SIZE = 16;
    public static final int HEIGHT = 64;

    private final ChunkPos pos;
    private final BlockState[][][] blocks = new BlockState[SIZE][HEIGHT][SIZE];

    private Mesh cachedMesh = null;
    // dirty = true, wenn mesh veraltet ist. -> neues rendern
    // dirty = false, wenn mesh aktuell ist. -> cachedMesh verwenden
    private boolean dirty = true;


    public Chunk(ChunkPos pos){
        this.pos = pos;
        for (int x = 0; x < SIZE; x++){
            for (int y = 0; y < HEIGHT; y++){
                for (int z = 0; z < SIZE; z++){
                    blocks[x][y][z] = new BlockState(Blocks.AIR);
                }
            }
        }
    }

    public Mesh getOrBuildMesh(float[] blockVertices, int[] blockIndices){
        if (!dirty && cachedMesh != null) return cachedMesh;

        List<Float> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        int offset = 0;

        int originX = pos.worldX();
        int originZ = pos.worldZ();

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < SIZE; z++) {
                    BlockState state = blocks[x][y][z];
                    if (state.type() == Blocks.AIR) continue;

                    float wx = originX + x;
                    float wy = y;
                    float wz = originZ + z;

                    for (int i = 0; i < blockVertices.length; i += 5) {
                        vertices.add(blockVertices[i]     + wx);
                        vertices.add(blockVertices[i + 1] + wy);
                        vertices.add(blockVertices[i + 2] + wz);
                        vertices.add(blockVertices[i + 3]); // u
                        vertices.add(blockVertices[i + 4]); // v
                    }

                    for (int index : blockIndices){
                        indices.add(index + offset);
                    }
                    // 24 Vertices pro Block, 4 Seiten x 6
                    offset += 24;

                }
            }
        }

        float[] vertArray = new float[vertices.size()];
        for (int i = 0; i < vertArray.length; i++) vertArray[i] = vertices.get(i);
        int[] indArray = indices.stream()
                .mapToInt(Integer::intValue)
                .toArray();
        if (cachedMesh != null) cachedMesh.cleanup();
        cachedMesh = new Mesh(vertArray, indArray);
        dirty = false;

        return cachedMesh;
    }

    public void markAsDirty(){
        dirty = true;
    }

    public void setBlock(int x, int y, int z, BlockType type){
        blocks[x][y][z] = new BlockState(type);
        markAsDirty();
    }

    public BlockState getBlock(int x, int y, int z){
        return blocks[x][y][z];
    }

    public ChunkPos getPos() {
        return pos;
    }
}
