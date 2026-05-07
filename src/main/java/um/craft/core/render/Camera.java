package um.craft.core.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
// TODO: Kameraklasse für Maus Movement später
public class Camera {
    private static final float Z_OFFSET = 5f;
    private Vector3f position = new Vector3f(0, 0, 5f);

    public void followPlayer(Vector3f playerPos) {
        position.set(playerPos.x, playerPos.y, playerPos.z + Z_OFFSET);
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(
                position,
                new Vector3f(position.x, position.y, position.z + 1f),
                new Vector3f(0, 1, 0)
        );
    }
}
