package um.craft.core.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.net.URL;

public class TextureAtlasMaker {

    public static void main(String[] args) throws URISyntaxException, IOException {
        getAllTextures();
    }

    public static void getAllTextures() throws URISyntaxException, IOException {
        URL url = Utils.class.getClassLoader().getResource("assets/textures/blocks");

        if (url != null) {
            List<String> pngFiles = Files.list(Paths.get(url.toURI()))
                    .map(path -> path.getFileName().toString())
                    .filter(n -> n.endsWith(".png"))
                    .toList();
        }
        //return pngFiles;
    }
}