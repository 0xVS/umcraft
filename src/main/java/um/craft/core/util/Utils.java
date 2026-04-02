package um.craft.core.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Utils {
    public static String loadResource(String path){
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
