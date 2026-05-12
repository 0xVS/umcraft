package um.craft.core.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import um.craft.core.util.Utils;

// TODO: Kameraklasse für Maus Movement später
public class Camera {
    private final Vector3f position = new Vector3f(0, 0, 5f);
    private float pitch = 0f;
    private float yaw = -90f;
    private static final float SENSITIVITY = Utils.readSettings("settings.json", "mouse.sensitivity", 0.175f);


    public void handleMouseMovement(float deltaX, float deltaY) {
        yaw += deltaX * SENSITIVITY;
        pitch += deltaY * SENSITIVITY;
        // maximal von -89 bis + 89
        pitch = Math.max(-89f, Math.min(89f, pitch));
    }

    public Vector3f getFrontVector() {
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        return new Vector3f(
                (float)(Math.cos(pitchRad) * Math.cos(yawRad)),
                (float) Math.sin(pitchRad),
                (float)(Math.cos(pitchRad) * Math.sin(yawRad))
        ).normalize();
    }

    public Vector3f getRightVector() {
        return new Vector3f(getFrontVector()).cross(0, 1, 0).normalize();
    }


    public void setFirstPersonPosition(Vector3f playerPos) {
        position.set(playerPos.x, playerPos.y, playerPos.z);
    }

    public Matrix4f getViewMatrix() {
        // blickrichtung berechnen
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        Vector3f frontView = new Vector3f(
                (float)(Math.cos(pitchRad) * Math.cos(yawRad)),
                (float) Math.sin(pitchRad),
                (float)(Math.cos(pitchRad) * Math.sin(yawRad))
        );


        return new Matrix4f().lookAt(
                position,
                new Vector3f(position).add(frontView),
                new Vector3f(0, 1, 0)
        );
    }

    public float getPitch() {
        return pitch;
    }
    public float getYaw() {
        return yaw;
    }
}
