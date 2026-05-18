package um.craft.core.world.block;

import org.joml.Vector2d;
import um.craft.core.render.Texture;
import um.craft.core.util.BlockModelLoader;

import java.util.Map;

public class Blocks {

    public static final BlockType AIR = new BlockType("umcraft:air", true, false, false, Map.of());

    public static final BlockType GRASS = BlockModelLoader.load("grass");

    public static final BlockType DIRT = BlockModelLoader.load("dirt");

    public static final BlockType STONE = BlockModelLoader.load("stone");

    public static final BlockType COBBLESTONE = BlockModelLoader.load("cobblestone");

    public static final BlockType ORE_COAL = BlockModelLoader.load("ore_coal");

}
