package um.craft.core.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;

public class TextureAtlasMaker {

    public static void main(String[] args) throws URISyntaxException, IOException {
        getAllTextures();
    }

    public static void getAllTextures() throws URISyntaxException, IOException {
        URL url = Utils.class.getClassLoader().getResource("assets/textures/");

        if (url != null) {
            List<String> pngFiles = Files.list(Paths.get(url.toURI()))
                    .map(path -> path.getFileName().toString())
                    .filter(n -> n.endsWith(".png"))
                    .toList();

            pngFiles.forEach(System.out::println);
        }
        //return pngFiles;
    }
}