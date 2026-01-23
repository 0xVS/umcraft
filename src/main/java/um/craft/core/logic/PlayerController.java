package um.craft.core.logic;

import org.lwjgl.glfw.GLFW;
import um.craft.core.input.*;

import org.joml.Vector3f;
import um.craft.core.window.Window;
import um.craft.core.window.WindowManager;

public class PlayerController implements InputListener {
    private InputHandler inputHandler;

    private final Vector3f position;
    private final Vector3f rotation;

    // zustände
    private boolean isMovingForward = false;
    private boolean isMovingBackward = false;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private boolean isSneaking = false;
    private boolean isSprinting = false;

    // maus
    private float mouseDeltaX = 0;
    private float mouseDeltaY = 0;

    WindowManager window;

     public PlayerController(Inputs rawInput, WindowManager wm) {
         this.inputHandler = new InputHandler(rawInput);
         this.inputHandler.addListener(this);
         this.window = wm;

         // custom bindings, z.B. aus dem load? jetzt erstmal als beispiel
         inputHandler.bindKey(GLFW.GLFW_KEY_F, "INVENTORY");

         this.position = new Vector3f(0,0,0);
         this.rotation = new Vector3f(0,0,0);
     }


    @Override
    public void onInputEvent(InputEvent event) {
        handleAction(event.getAction(), event.getType());
    }

    private void handleAction(String action, InputEvent.Type type) {
        boolean isActive = (type == InputEvent.Type.PRESSED || type == InputEvent.Type.HELD);

        switch (action) {
            case "MOVE_FORWARD" -> isMovingForward = isActive;
            case "MOVE_BACKWARD" -> isMovingBackward = isActive;
            case "MOVE_LEFT" -> isMovingLeft = isActive;
            case "MOVE_RIGHT" -> isMovingRight = isActive;
            case "QUIT" -> window.cleanup();
            /*
            case "JUMP":
                if (type == InputEvent.Type.PRESSED) {
                    jump();
                }
                break;
                */
            // ... usw
        }
    }

    public void update(float deltaTime) {
        inputHandler.update();

        // bewegung berechnen basierend auf aktuellen zuständen
        updateMovement(deltaTime);

        // Debug output ?? ka ob wir das brauchen aber ja
        debugOutput();
    }

    private void updateMovement(float deltaTime) {
        // einfache Bewegung (zum testen)
        float speed = 5.0f * deltaTime;

        if (isMovingForward) position.z -= speed;
        if (isMovingBackward) position.z += speed;
        if (isMovingLeft) position.x -= speed;
        if (isMovingRight) position.x += speed;
    }

    private void debugOutput() {
        // Alle 60 Frames Position ausgeben
        if (System.currentTimeMillis() % 1000 < 16) { // Ca. jede Sekunde
            System.out.printf("Player position: (%.2f, %.2f, %.2f)%n",
                    position.x, position.y, position.z);
        }
    }
}
