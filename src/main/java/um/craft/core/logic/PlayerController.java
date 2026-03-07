package um.craft.core.logic;

import org.joml.Vector3f;

import org.lwjgl.glfw.GLFW;

import um.craft.core.input.InputEvent;
import um.craft.core.input.InputHandler;
import um.craft.core.input.InputListener;
import um.craft.core.input.Inputs;
import um.craft.core.window.WindowManager;

import java.util.EnumSet;

public class PlayerController implements InputListener {

    private InputHandler inputHandler;

    //private final Vector3f position;
    //private final Vector3f rotation;
    private final Vector3f position = new Vector3f(0, 0, 0);

    WindowManager window;

    public PlayerController(Inputs rawInput, WindowManager wm) {
        this.inputHandler = new InputHandler(rawInput);
        this.inputHandler.addListener(this);
        this.window = wm;

        // custom bindings, z.B. aus dem load? jetzt erstmal als beispiel
        inputHandler.bindKey(GLFW.GLFW_KEY_F, "INVENTORY");

        //this.position = new Vector3f(0, 0, 0);
        //this.rotation = new Vector3f(0, 0, 0);
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
                    case "QUIT" -> window.cleanup();
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
                    case "QUIT" -> window.cleanup();
                }
            }
        }
    }

    public void update(float deltaTime) {
        inputHandler.update();
        testMovement(deltaTime);
        debugOutput();
    }

    private void testMovement(float deltaTime){
        float speed = 1.2f * deltaTime;
/*
        if (activeActions.contains(PlayerAction.MOVING_FORWARD)) position.x += speed;
        if (activeActions.contains(PlayerAction.MOVING_BACKWARD)) position.x -= speed;
        if (activeActions.contains(PlayerAction.MOVING_LEFT)) position.z -= speed;
        if (activeActions.contains(PlayerAction.MOVING_RIGHT)) position.z += speed;
        if (activeActions.contains(PlayerAction.MOVING_UP)) position.y += speed;
        if (activeActions.contains(PlayerAction.MOVING_DOWN)) position.y -= speed;
        */

        if (activeActions.contains(PlayerAction.MOVING_FORWARD)) position.y += speed;
        if (activeActions.contains(PlayerAction.MOVING_BACKWARD)) position.y -= speed;
        if (activeActions.contains(PlayerAction.MOVING_LEFT)) position.x -= speed;
        if (activeActions.contains(PlayerAction.MOVING_RIGHT)) position.x += speed;
        //if (activeActions.contains(PlayerAction.MOVING_UP)) position.y += 0;
        if (activeActions.contains(PlayerAction.MOVING_UP)){
            position.y = 0;
            position.x = 0;
        }

        if (activeActions.contains(PlayerAction.MOVING_DOWN)) position.y -= 0;

        position.z = 0.0f;

        position.x = Math.max(-1.0f, Math.min(1.0f, position.x));
        position.y = Math.max(-1.0f, Math.min(1.0f, position.y));
    }

    public Vector3f getPosition() {
        return position;
    }


    private void debugOutput() {
        if (System.currentTimeMillis() % 1000 < 16) {
            System.out.printf("> POS: (%.2f, %.2f, %.2f)%n",
                    position.x, position.y, position.z);
        }
    }
}
