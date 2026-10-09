package um.craft.core.render.scene;

import um.craft.core.world.block.BlockType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
public class TextureAtlas {
    private int textureId;
    private int atlasSize;
    private int tileSize; // grösse der textur (16x16) gerade
    private int tilesPerRow;

    private final Map<BlockType, float[]> uvMap = new HashMap<>();


    // block texturen register + atlas bauen


    1.)
    -> 9 texturen, 3x3 Raster => 96x96px bei 32x texturen

    public void build(Map<BlockType, String> texturePath){

    }

    // uv coords zurückgeben
    public float[] getUV(BlockType type){
        return uvMap.get(type);
    }

    // an opengl textur binden
    public void bind(){}

    // ja
    public void cleanup(){}
}
*/

public class TextureAtlas {
    private static final int TILES = 16;
    private final Map<String, float[]> uvs = new HashMap<>();
    private int textureId;

    public void build(List<String> names){

    }
}