package um.craft.core.util;

import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
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
        System.out.println("Lade Textur: " + path);

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
            IntBuffer w    = stack.mallocInt(1);
            IntBuffer h    = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(true);
            image = stbi_load(absPath, w, h, comp, 4);
            if (image == null)
                throw new RuntimeException("stbi konnte textur nicht laden: " + path + " , " + stbi_failure_reason());

            width  = w.get(0);
            height = h.get(0);
        }

        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

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

    private static String getJsonValue(String file, String key, ValueType type){
        String content = loadResource(file);
        String[] parts = key.split("\\.");
        StringBuilder regex = new StringBuilder();

        // für inneren block
        for (int i = 0; i < parts.length - 1; i++) {
            regex.append("\"")
                    .append(parts[i])
                    .append("\"\\s*:\\s*\\{[^}]*");
        }

        regex.append("\"")
                .append(parts[parts.length - 1])
                .append("\"\\s*:\\s*")
                .append(type.getPattern());

        Matcher matcher = Pattern.compile(regex.toString()).matcher(content);
        if (matcher.find()) return matcher.group(1).trim();
        throw new IllegalArgumentException("Key '" + key + "' nicht gefunden");
    }

    /*
            weil alle ja das selbe machen nur für verschiedene typen
            -> überladene methoden für jeweils den vorhandenen typ
     */

    public static float readSettings(String path, String key) {
        try {
            return Float.parseFloat(getJsonValue(path, key, ValueType.NUMBER)); }
        catch (Exception e) {
            System.err.printf("float '%s' nicht gefunden%n", key);
            return 0.0f;
        }
    }

    public static int readSettings(String path, String key, int defaultValue) {
        try {
            return Integer.parseInt(getJsonValue(path, key, ValueType.NUMBER));
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean readSettings(String path, String key, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(getJsonValue(path, key, ValueType.BOOLEAN));
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public static String readSettings(String path, String key, String defaultValue) {
        try {
            return getJsonValue(path, key, ValueType.STRING);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
}
