package um.craft.core.logic;

import org.lwjgl.glfw.GLFW;
import um.craft.core.input.*;

import org.joml.Vector3f;
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
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;

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

        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
    }


    @Override
    public void onInputEvent(InputEvent event) {
        handleAction(event.action(), event.type());
    }

    private void handleAction(String action, InputEvent.Type type) {
        switch (type) {
            case PRESSED, HELD -> {
                // setze bewegung auf true bei PRESSED oder HELD
                switch (action) {
                    case "MOVE_FORWARD" -> isMovingForward = true;
                    case "MOVE_BACKWARD" -> isMovingBackward = true;
                    case "MOVE_LEFT" -> isMovingLeft = true;
                    case "MOVE_RIGHT" -> isMovingRight = true;
                    case "MOVE_UP" -> isMovingUp = true;
                    case "MOVE_DOWN" -> isMovingDown = true;
                    case "QUIT" -> window.cleanup();
                }
            }
            case RELEASED -> {
                // setze bewegung auf false bei RELEASED
                switch (action) {
                    case "MOVE_FORWARD" -> isMovingForward = false;
                    case "MOVE_BACKWARD" -> isMovingBackward = false;
                    case "MOVE_LEFT" -> isMovingLeft = false;
                    case "MOVE_RIGHT" -> isMovingRight = false;
                    case "MOVE_UP" -> isMovingUp = false;
                    case "MOVE_DOWN" -> isMovingDown = false;
                }
            }
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
        float speed = 15.0f * deltaTime;

        if (isMovingForward) position.z -= speed;
        if (isMovingBackward) position.z += speed;
        if (isMovingLeft) position.x -= speed;
        if (isMovingRight) position.x += speed;
        if (isMovingUp) position.y += speed;
        if (isMovingDown) position.y -= speed;
    }

    private void debugOutput() {
        // Alle 60 Frames Position ausgeben
        if (System.currentTimeMillis() % 1000 < 16) { // Ca. jede Sekunde
            System.out.printf("Player position: (%.2f, %.2f, %.2f)%n",
                    position.x, position.y, position.z);
        }
    }
}
