package um.craft.core.world;

import um.craft.core.world.block.BlockState;
import um.craft.core.world.block.BlockType;
import um.craft.core.world.block.Blocks;

public class Chunk {
    public static final int SIZE = 16;
    public static final int HEIGHT = 64;

    private final ChunkPos pos;
    private final BlockState[][][] blocks = new BlockState[SIZE][HEIGHT][SIZE];

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

    public void setBlock(int x, int y, int z, BlockType type){
        blocks[x][y][z] = new BlockState(type);
    }

    public BlockState getBlock(int x, int y, int z){
        return blocks[x][y][z];
    }

    public ChunkPos getPos() {
        return pos;
    }
}
