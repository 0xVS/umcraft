package um.craft.core.world.block;

import um.craft.core.render.Texture;

public record BlockType(
        String id,
        boolean transparent,
        boolean solid,
        boolean hasGravity,
        Texture texture
){}
