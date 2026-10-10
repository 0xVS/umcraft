package um.craft.core.render.scene;

import org.joml.Matrix4f;

// TODO: projMat implementieren
public class Projection {
    // FOV = 60 gerade
    private static float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private final Matrix4f projMatrix;

    public Projection(int width, int height){
        projMatrix = new Matrix4f();
        updateProjMatrix(width, height);
    }

    private float getFOV(){
        return FOV;
    }

    private void setFOV(int newFOV){
        FOV = (float) Math.toRadians(newFOV);
    }

    public Matrix4f getProjMatrix(){
        return projMatrix;
    }
    public void updateProjMatrix(int width, int height) {
        if (height == 0 || width == 0) return;
        projMatrix.setPerspective(FOV, ((float) width / (float) height), Z_NEAR, Z_FAR);
    }
}
