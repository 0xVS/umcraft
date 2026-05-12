package um.craft.core.world.block;

import org.joml.Vector2d;
import um.craft.core.render.Texture;

public class Blocks {

    public static BlockType solidCube(String id, Texture texture){
        return new BlockType(
                id,
                false,
                true,
                false,
                texture
        );
    }

    public static BlockType solidCubeGrav(String id, Texture texture){
        return new BlockType(
                id,
                false,
                true,
                true,
                texture
        );
    }

    public static final BlockType AIR = new BlockType(
            "umcraft:air",
            true,
            false,
            false,
            null
    );

    public static final BlockType GRASS = solidCube(
            "umcraft:grass",
            new Texture("blocks/grass.png")
    );

    public static final BlockType DIRT = solidCube(
            "umcraft:dirt",
            new Texture("blocks/dirt.png")
    );

    public static final BlockType STONE = solidCube(
            "umcraft:stone",
            new Texture("blocks/stone.png")
    );

    public static final BlockType COBBLESTONE = solidCube(
            "umcraft:cobblestone",
            new Texture("blocks/stone.png")
    );

    public static final BlockType ORE_COAL = solidCube(
            "umcraft:ore_coal",
            new Texture("blocks/stone.png")
    );

}
