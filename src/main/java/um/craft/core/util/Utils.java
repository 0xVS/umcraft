package um.craft.core.util;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

import com.google.gson.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Utils {

    public static List<String> fetchPngs() {
        URL url = Utils.class.getClassLoader().getResource("assets/textures/blocks");
        if (url == null) return List.of();
        try (Stream<Path> stream = Files.list(Paths.get(url.toURI()))) {
            return stream.map(path -> path.getFileName().toString())
                    .filter(f -> f.endsWith(".png"))
                    .toList();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String loadResource(String file) {
        try {
            // resource aus java resources im classpath laden
            URL url = Utils.class.getClassLoader().getResource(file);
            if (url == null) throw new FileNotFoundException("Resource nicht gefunden: " + file);


            try (InputStream is = url.openStream();
                 BufferedReader reader = new BufferedReader(
                         new InputStreamReader(is))) {

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            }
        } catch (Exception e) {
            throw new RuntimeException("Resource '" + file + "' nicht gefunden", e);
        }
    }


    public static int loadTexture(String path) {

        int width, height;
        ByteBuffer image;

        String absPath;
        try {
            URL url = Utils.class.getClassLoader().getResource("assets/textures/" + path);
            if (url == null)
                throw new RuntimeException("Textur nicht im Classpath gefunden: assets/textures/" + path);
            absPath = java.nio.file.Paths.get(url.toURI()).toFile().getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Pfad-Handling der Textur: " + path, e);
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(true);
            image = stbi_load(absPath, w, h, comp, 4);
            if (image == null)
                throw new RuntimeException("stbi konnte textur nicht laden: " + path + " , " + stbi_failure_reason());

            width = w.get(0);
            height = h.get(0);
        }

        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // texutr in gpu geladen
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        System.out.println("[DEBUG] Textur geladen: " + path);

        stbi_image_free(image);
        return textureID;
    }
    /*

     key ist z. B.:
     "window.with" oder "window.vsync" in:
    {
      "window": {
        "width": 1280,
        "height": 720,
        "title": "Craft 3D",
        "vsync": true
        },
        ...
    }
     */

    public static JsonObject loadJson(String path) {
        String content = loadResource(path);
        return JsonParser.parseString(content).getAsJsonObject();
    }


    private static JsonElement getJsonElement(String file, String key) {
        String content = loadResource(file);
        JsonObject root = JsonParser.parseString(content).getAsJsonObject();

        String[] parts = key.split("\\.");
        JsonElement current = root;

        for (String part : parts) {
            if (!current.isJsonObject())
                throw new IllegalArgumentException("Key '" + key + "' nicht gefunden");
            current = current.getAsJsonObject().get(part);
            if (current == null)
                throw new IllegalArgumentException("Key '" + key + "' nicht gefunden");
        }
        return current;
    }

    /*
            weil alle ja das selbe machen nur für verschiedene typen
            -> überladene methoden für jeweils den vorhandenen typ
     */

    public static float readSettings(String path, String key, float defaultValue) {
        try {
            return getJsonElement(path, key).getAsFloat();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int readSettings(String path, String key, int defaultValue) {
        try {
            return getJsonElement(path, key).getAsInt();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean readSettings(String path, String key, boolean defaultValue) {
        try {
            return getJsonElement(path, key).getAsBoolean();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String readSettings(String path, String key, String defaultValue) {
        try {
            return getJsonElement(path, key).getAsString();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static JsonArray readArray(String path, String key) {
        try {
            return getJsonElement(path, key).getAsJsonArray();
        } catch (Exception e) {
            return new JsonArray();
        }
    }

    public static JsonObject readObject(String path, String key) {
        try {
            return getJsonElement(path, key).getAsJsonObject();
        } catch (Exception e) {
            return new JsonObject();
        }
    }
}
