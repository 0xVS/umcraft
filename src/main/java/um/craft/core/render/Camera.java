package um.craft.core.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position = new Vector3f(0, 0, -2f);

    public void followPlayer(Vector3f playerPos) {
        position.set(playerPos.x, playerPos.y, playerPos.z);
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(
                position,
                new Vector3f(position.x, position.y, position.z - 1.0f),
                new Vector3f(0, 1, 0)
        );
    }
}
