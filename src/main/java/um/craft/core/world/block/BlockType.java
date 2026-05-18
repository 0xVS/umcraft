package um.craft.core.world.block;

import um.craft.core.render.Texture;

import java.util.Map;

public record BlockType(
        String id,
        boolean transparent,
        boolean solid,
        boolean hasGravity,
        Map<String, Texture> faceTextures
){ // keine ahnung ob ich das brauche aber wenn keine spezifische textur gefunden wird, dann nimm "all"
    public Texture textureFor(String face){
        return faceTextures.getOrDefault(face,
                faceTextures.getOrDefault("all", null));
    }
}
