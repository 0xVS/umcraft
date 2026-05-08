package um.craft.core.logic;

import org.joml.Vector3f;

import org.lwjgl.glfw.GLFW;

import um.craft.core.input.InputEvent;
import um.craft.core.input.InputHandler;
import um.craft.core.input.InputListener;
import um.craft.core.input.Inputs;
import um.craft.core.render.Camera;
import um.craft.core.window.WindowManager;

import java.util.EnumSet;

// Movement soll Objekt nicht mehr bewegen sondern später den Spieler
public class PlayerController implements InputListener {

    private InputHandler inputHandler;
    private Inputs rawInput;
    private Camera camera;
    //private final Vector3f position;
    //private final Vector3f rotation;
    private Vector3f moveDir = new Vector3f();
    private final Vector3f position = new Vector3f(0, 0, 0);

    WindowManager window;

    public PlayerController(Inputs rawInput, WindowManager wm, Camera camera) {
        this.inputHandler = new InputHandler(rawInput);
        this.inputHandler.addListener(this);
        this.window = wm;
        this.rawInput = rawInput;
        this.camera = camera;
        // custom bindings, z.B. aus dem load? jetzt erstmal als beispiel
        inputHandler.bindKey(GLFW.GLFW_KEY_F, "INVENTORY");

    }
    private enum PlayerAction {
        MOVING_FORWARD,
        MOVING_BACKWARD,
        MOVING_LEFT,
        MOVING_RIGHT,
        MOVING_UP,
        MOVING_DOWN,
        IS_SPRINTING,
        IS_SNEAKING
    }

    private final EnumSet<PlayerAction> activeActions = EnumSet.noneOf(PlayerAction.class);

    @Override
    public void onInputEvent(InputEvent event) {
        actionTest(event.action(), event.type());
    }
    private void actionTest(String action, InputEvent.Type type) {
        switch (type) {
            case PRESSED, HELD -> {
                switch (action) {
                    case "MOVE_FORWARD" -> activeActions.add(PlayerAction.MOVING_FORWARD);
                    case "MOVE_BACKWARD" -> activeActions.add(PlayerAction.MOVING_BACKWARD);
                    case "MOVE_LEFT" -> activeActions.add(PlayerAction.MOVING_LEFT);
                    case "MOVE_RIGHT" -> activeActions.add(PlayerAction.MOVING_RIGHT);
                    case "MOVE_UP" -> activeActions.add(PlayerAction.MOVING_UP);
                    case "MOVE_DOWN" -> activeActions.add(PlayerAction.MOVING_DOWN);
                    case "QUIT" -> window.setShouldClose();
                }
            }
            case RELEASED -> {
                switch (action) {
                    case "MOVE_FORWARD" -> activeActions.remove(PlayerAction.MOVING_FORWARD);
                    case "MOVE_BACKWARD" -> activeActions.remove(PlayerAction.MOVING_BACKWARD);
                    case "MOVE_LEFT" -> activeActions.remove(PlayerAction.MOVING_LEFT);
                    case "MOVE_RIGHT" -> activeActions.remove(PlayerAction.MOVING_RIGHT);
                    case "MOVE_UP" -> activeActions.remove(PlayerAction.MOVING_UP);
                    case "MOVE_DOWN" -> activeActions.remove(PlayerAction.MOVING_DOWN);
                    case "QUIT" -> window.setShouldClose();
                }
            }
        }
    }

    public void update(float deltaTime) {
        inputHandler.update();

        float dx = (float) rawInput.getMouseDeltaX();
        float dy = (float) rawInput.getMouseDeltaY();
        camera.handleMouseMovement(dx, dy);
        rawInput.resetMouseDelta();

        vectorMovement(deltaTime);
        debugOutput(dx, dy);
    }

    public void vectorMovement(float deltaTime){
        // old val 1.2
        float moveSpeed = 5f * deltaTime;

        Vector3f forward = camera.getFrontVector();
        Vector3f right = camera.getRightVector();

        Vector3f forwardFlatTest = new Vector3f(forward.x, 0, forward.z).normalize();

        moveDir.set(0, 0, 0);

        if (activeActions.contains(PlayerAction.MOVING_FORWARD)) moveDir.add(forwardFlatTest);
        if (activeActions.contains(PlayerAction.MOVING_BACKWARD)) moveDir.sub(forwardFlatTest);
        if (activeActions.contains(PlayerAction.MOVING_UP)) moveDir.y += 1;
        if (activeActions.contains(PlayerAction.MOVING_DOWN)) moveDir.y -= 1;
        if (activeActions.contains(PlayerAction.MOVING_LEFT)) moveDir.sub(right);
        if (activeActions.contains(PlayerAction.MOVING_RIGHT)) moveDir.add(right);

        // damit diagonaler speed nicht größer ist als horizontal / vertikal
        if (moveDir.lengthSquared() > 0){
            moveDir.normalize().mul(moveSpeed);
            position.add(moveDir);
        }
    }

    public Vector3f getPosition() {
        return position;
    }


    private void debugOutput(float dx, float dy) {
        if (System.currentTimeMillis() % 1000 < 16) {
            System.out.printf("> COORDS: (%.2f, %.2f, %.2f)%n",
                    position.x, position.y, position.z);
            System.out.printf("\n> MOUSE: (%.2f, %.2f)%n",
                    dx, dy);
        }
    }
}
