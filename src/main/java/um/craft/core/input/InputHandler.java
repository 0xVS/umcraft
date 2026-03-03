package um.craft.core.input;

import org.lwjgl.glfw.GLFW;

import java.util.*;

public class InputHandler {

    private final Inputs rawInput;

    private Map<Integer, String> keyBindings = new HashMap<>();
    private Map<Integer, String> mouseBindings = new HashMap<>();

    // EventListeners
    private List<InputListener> listeners = new ArrayList<>();

    private final Set<String> activeActions = new HashSet<>();

    private boolean[] keyWasPressed = new boolean[GLFW.GLFW_KEY_LAST];

    public InputHandler(Inputs rawInput) {
        this.rawInput = rawInput;
        setupDefaultBindings();
    }

    private void setupDefaultBindings() {
        // movement bindings
        bindKey(GLFW.GLFW_KEY_W, "MOVE_FORWARD");
        bindKey(GLFW.GLFW_KEY_A, "MOVE_LEFT");
        bindKey(GLFW.GLFW_KEY_S, "MOVE_BACKWARD");
        bindKey(GLFW.GLFW_KEY_D, "MOVE_RIGHT");
        bindKey(GLFW.GLFW_KEY_LEFT_SHIFT, "MOVE_DOWN");
        bindKey(GLFW.GLFW_KEY_SPACE, "MOVE_UP");

        // maus bindings

        bindMouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT, "ATTACK");
        bindMouseButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT, "USE");
        bindMouseButton(GLFW.GLFW_MOUSE_BUTTON_MIDDLE, "PICK_BLOCK");

        // ui / etc
        bindKey(GLFW.GLFW_KEY_ESCAPE, "QUIT");
        bindKey(GLFW.GLFW_KEY_E, "INVENTORY");
        bindKey(GLFW.GLFW_KEY_TAB, "PLAYER_LIST");
        bindKey(GLFW.GLFW_KEY_F5, "SWITCH_PERSPECTIVE");
    }

    // keybinding setzen
    public void bindKey(int glfwKey, String action) {
        keyBindings.put(glfwKey, action);
    }

    // mousebinding setzen
    public void bindMouseButton(int glfwButton, String action) {
        mouseBindings.put(glfwButton, action);
    }

    // muss jeden frame aufgerufen werden um inputs zu verarbrbeitenten ach junge scheiß drauf
    public void update() {
        activeActions.clear();

        // keyboard actions
        for (Map.Entry<Integer, String> entry : keyBindings.entrySet()) {
            int key = entry.getKey();
            String action = entry.getValue();

            boolean isPressed = rawInput.isKeyDown(key);
            boolean wasPressed = keyWasPressed[key];


            if (isPressed && !wasPressed) {
                // taste wurde GERADE gedrückt
                notifyListeners(action, InputEvent.Type.PRESSED);
                keyWasPressed[key] = true;
                activeActions.add(action);

            } else if (!isPressed && wasPressed) {
                // taste wurde LOSGELASSEN
                notifyListeners(action, InputEvent.Type.RELEASED);
                keyWasPressed[key] = false;

            } else if (isPressed && wasPressed) {
                // taste wird GEHALTEN
                notifyListeners(action, InputEvent.Type.HELD);
                activeActions.add(action);
            }
        }

        // mouse actions
        for (Map.Entry<Integer, String> entry : mouseBindings.entrySet()) {
            int button = entry.getKey();
            String action = entry.getValue();

            if (rawInput.isButtonDown(button)) {
                activeActions.add(action);
            }
        }

    }


    // prüft, ob aktion aktuell aktiv ist
    public boolean isActionActive(String action) {
        return activeActions.contains(action);
    }

    // prüft, ob aktion gerade gedrückt wurde (nur einmal pro drücken)
    public boolean isActionPressed(String action) {
        for (Map.Entry<Integer, String> entry : keyBindings.entrySet()) {
            if (entry.getValue().equals(action)) {
                int key = entry.getKey();
                return rawInput.isKeyDown(key) && !keyWasPressed[key];
            }
        }
        return false;
    }

    // TODO: implementieren
    public float getMouseDeltaX() {
        return 0.0f;
    }

    public float getMouseDeltaY() {
        return 0.0f;
    }


    public void addListener(InputListener listener) {
        listeners.add(listener);
    }

    public void removeListener(InputListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(String action, InputEvent.Type type) {
        InputEvent event = new InputEvent(action, type);
        for (InputListener listener : listeners) {
            listener.onInputEvent(event);
        }
    }

    // für speichern/laden der config für bindings
    public void saveBindings(String filepath) {
    }

    public void loadBindings(String filepath) {
    }

}
