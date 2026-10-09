package um.craft.core.util;

import um.craft.core.render.Mesh;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ObjLoader {

    public static Mesh load(String path) {
        List<float[]> positions = new ArrayList<>();
        List<float[]> uvs      = new ArrayList<>();

        // fertige interleaved vertices (x, y, z, u, v) und indices für Mesh
        List<Float>   vertices = new ArrayList<>();
        List<Integer> indices  = new ArrayList<>();

        System.out.println("Obj path?: " + ObjLoader.class.getClassLoader().getResource(path));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                ObjLoader.class.getClassLoader().getResourceAsStream("assets/models/entity/player/" + path)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.strip();

                if (line.startsWith("v ")) {
                    // vertex position: "v x y z"
                    String[] t = line.split("\\s+");
                    positions.add(new float[]{
                            Float.parseFloat(t[1]),
                            Float.parseFloat(t[2]),
                            Float.parseFloat(t[3])
                    });

                } else if (line.startsWith("vt ")) {
                    // uv coord: "vt u v"
                    String[] t = line.split("\\s+");
                    uvs.add(new float[]{
                            Float.parseFloat(t[1]),
                            // OBJ hat V von unten nach oben, OpenGL von oben nach unten
                            /*1f - */ Float.parseFloat(t[2])
                    });

                } else if (line.startsWith("f ")) {
                    // Blockbench exportiert Quads: "f v1/vt1 v2/vt2 v3/vt3 v4/vt4"
                    // Wir teilen auf in 2 Dreiecke: (0,1,2) und (0,2,3)
                    String[] t = line.split("\\s+");

                    int[] faceIndices = new int[t.length - 1];

                    for (int i = 1; i < t.length; i++) {
                        String[] parts = t[i].split("/");
                        int vIdx  = Integer.parseInt(parts[0]) - 1;
                        int vtIdx = parts.length > 1 && !parts[1].isEmpty()
                                ? Integer.parseInt(parts[1]) - 1
                                : 0;

                        float[] pos = positions.get(vIdx);
                        float[] uv  = uvs.isEmpty() ? new float[]{0f, 0f} : uvs.get(vtIdx);

                        vertices.add(pos[0]);
                        vertices.add(pos[1]);
                        vertices.add(pos[2]);
                        vertices.add(uv[0]);
                        vertices.add(uv[1]);

                        faceIndices[i - 1] = indices.size() + (i - 1); // noch nicht committed
                    }

                    // Quad → 2 Dreiecke: (0,1,2) und (0,2,3)
                    int base = vertices.size() / 5 - (t.length - 1);
                    indices.add(base);     indices.add(base + 1); indices.add(base + 2);
                    indices.add(base);     indices.add(base + 2); indices.add(base + 3);
                }
                // "vn" (Normalen) und "mtllib" / "usemtl" ignorieren wir erstmal
            }

        } catch (Exception e) {
            System.err.println("[ObjLoader] Fehler beim Laden: " + path);
            e.printStackTrace();
        }

        // in primitive arrays umwandeln für Mesh
        float[] vertexArray = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) vertexArray[i] = vertices.get(i);

        int[] indexArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) indexArray[i] = indices.get(i);

        return new Mesh(vertexArray, indexArray);
    }
}