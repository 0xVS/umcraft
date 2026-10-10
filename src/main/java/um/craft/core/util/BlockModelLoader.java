package um.craft.core.util;

import com.google.gson.JsonObject;
import um.craft.core.render.Texture;
import um.craft.core.world.block.BlockType;

import java.util.*;

public class BlockModelLoader {
    public static BlockType load(String name){
        JsonObject blockAsJson = Utils.loadJson("assets/models/blockstates/" + name + ".json");
        String modelPath = blockAsJson.get("model").getAsString();
        JsonObject modelAsJson = Utils.loadJson("assets/models/" + modelPath + ".json");

        List<String> validFaces = new ArrayList<>();
        modelAsJson.get("faces").getAsJsonArray()
                .forEach(face -> validFaces.add(face.getAsString()));


        String id = blockAsJson.get("id").getAsString();
        boolean solid = blockAsJson.get("solid").getAsBoolean();
        boolean transparent = blockAsJson.get("transparent").getAsBoolean();
        boolean gravity = blockAsJson.get("gravity").getAsBoolean();

        Map<String, Texture> faces = loadFaces(blockAsJson.getAsJsonObject("textures"), validFaces);

        return new BlockType(id, transparent, solid, gravity, faces);
    }

    private static Map<String, Texture> loadFaces(JsonObject textures, List<String> validFaces) {
        Map<String, Texture> result = new LinkedHashMap<>();

        // "all" unter "textures":... für alle faces vom block
        if (textures.has("all")) {
            Texture fallback = new Texture(textures.get("all").getAsString() + ".png");
            validFaces.forEach(face -> result.put(face, fallback));
        }

        // spezifische faces überschreiben "all" unter "textures":...
        for (var entry : textures.entrySet()) {
            if (entry.getKey().equals("all")) continue;
            if (validFaces.contains(entry.getKey())) {
                result.put(entry.getKey(), new Texture(entry.getValue().getAsString() + ".png"));
            }
        }

        return result;
    }
}
