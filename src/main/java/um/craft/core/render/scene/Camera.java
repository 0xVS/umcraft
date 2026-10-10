package um.craft.core.render.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import um.craft.core.util.Utils;

// TODO: Kameraklasse für Maus Movement später
public class Camera {
    private final Vector3f position = new Vector3f();
    private final Vector3f playerPosition = new Vector3f();
    private final Vector3f playerBodyPosition = new Vector3f();
    private final Vector3f front = new Vector3f(0, 0, -1.0f);
    private final Vector3f up = new Vector3f(0, 1, 0);

    private float pitch = 0f;
    private float yaw = -90f;
    private static final float SENSITIVITY = Utils.readSettings("settings.json", "mouse.sensitivity", 0.175f);

    public CameraMode getCurrentMode() {
        return currentMode;
    }

    private CameraMode currentMode = CameraMode.FIRST_PERSON;


    public Camera() {
        updateCameraVectors();
    }

    public void toggleCameraMode(){
        switch (currentMode){
            case FIRST_PERSON -> currentMode = CameraMode.THIRD_PERSON_BACK;
            case THIRD_PERSON_BACK -> currentMode = CameraMode.THIRD_PERSON_FRONT;
            case THIRD_PERSON_FRONT -> currentMode = CameraMode.FIRST_PERSON;
        }
    }

    public void handleMouseMovement(float deltaX, float deltaY) {
        yaw += deltaX * SENSITIVITY;
        pitch += deltaY * SENSITIVITY;
        // maximal von -89 bis + 89
        pitch = Math.max(-89f, Math.min(89f, pitch));
        updateCameraVectors();
    }

    private void updateCameraVectors() {
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        front.x = (float) (Math.cos(pitchRad) * Math.cos(yawRad));
        front.y = (float) Math.sin(pitchRad);
        front.z = (float) (Math.cos(pitchRad) * Math.sin(yawRad));
        front.normalize();
    }

    public Vector3f getViewDirection() {
        return new Vector3f(front);
    }

    public Vector3f getRightVector() {
        return new Vector3f(front).cross(up).normalize();
    }


    public void setFirstPersonPosition(Vector3f playerPos) {

        position.set(playerPos.x, playerPos.y, playerPos.z);
    }

    public Matrix4f getViewMatrix() {
        //Vector3f target = new Vector3f(position).add(front);

        float THIRD_PERSON_DISTANCE = 3f;
        switch (currentMode) {
            case FIRST_PERSON -> //target.set(position).add(front);
                    position.set(playerPosition);
            case THIRD_PERSON_BACK -> //target.set(position);
                    position.set(playerPosition)
                            .sub(new Vector3f(front)
                            .mul(THIRD_PERSON_DISTANCE));
            case THIRD_PERSON_FRONT -> //target.set(position);
                    position.set(playerPosition)
                            .add(new Vector3f(front)
                            .mul(THIRD_PERSON_DISTANCE));
        }
        Vector3f target = (currentMode == CameraMode.FIRST_PERSON) ? new Vector3f(position).add(front) : new Vector3f(playerPosition);
        return new Matrix4f().lookAt(position, target, up);
    }

    public float getPitch() {
        return pitch;
    }
    public float getYaw() {
        return yaw;
    }

    public void updatePosition(Vector3f playerPos) {
        this.playerBodyPosition.set(playerPos);
        // ansonsten 1.6f probieren
        float EYE_HEIGHT = 1.7f;
        this.playerPosition.set(playerPos.x, playerPos.y + EYE_HEIGHT, playerPos.z);
    }

    public Vector3f getPlayerBodyPosition() {
        return playerBodyPosition;
    }
}
