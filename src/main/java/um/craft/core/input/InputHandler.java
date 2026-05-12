package um.craft.core.input;

import org.lwjgl.glfw.GLFW;
import um.craft.core.util.Utils;

import java.lang.reflect.Field;
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
        bindKey(glfwNumConverter(Utils.readSettings("keybinds.json", "movement.forward", "I")), "MOVE_FORWARD");
        bindKey(glfwNumConverter(Utils.readSettings("keybinds.json", "movement.left", "J")), "MOVE_LEFT");
        bindKey(glfwNumConverter(Utils.readSettings("keybinds.json", "movement.right", "L")), "MOVE_RIGHT");
        bindKey(glfwNumConverter(Utils.readSettings("keybinds.json", "movement.backward", "K")), "MOVE_BACKWARD");
        bindKey(glfwNumConverter(Utils.readSettings("keybinds.json", "movement.jump", "SPACE")), "MOVE_UP");
        bindKey(glfwNumConverter(Utils.readSettings("keybinds.json", "movement.crouch", "RIGHT_SHIFT")), "MOVE_DOWN");
        /*bindKey(GLFW.GLFW_KEY_W, "MOVE_FORWARD");
        bindKey(GLFW.GLFW_KEY_A, "MOVE_LEFT");
        bindKey(GLFW.GLFW_KEY_S, "MOVE_BACKWARD");
        bindKey(GLFW.GLFW_KEY_D, "MOVE_RIGHT");
        bindKey(GLFW.GLFW_KEY_LEFT_SHIFT, "MOVE_DOWN");
        bindKey(GLFW.GLFW_KEY_SPACE, "MOVE_UP"); */

        //String test = "GLFW.GLFW_KEY_" + Utils.readSettings("keybinds.json", "movement.forward");

        // maus bindings

        String f3Key = Utils.readSettings("keybinds.json", "debug.info", "F3");
        System.out.println("[DEBUG] debug.info key: '" + f3Key + "'");
        bindKeySafe(f3Key, "DEBUG_INFO");

        bindMouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT, "ATTACK");
        bindMouseButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT, "USE");
        bindMouseButton(GLFW.GLFW_MOUSE_BUTTON_MIDDLE, "PICK_BLOCK");

        // ui / etc
        bindKey(GLFW.GLFW_KEY_ESCAPE, "QUIT");
        bindKey(GLFW.GLFW_KEY_E, "INVENTORY");
        bindKey(GLFW.GLFW_KEY_TAB, "PLAYER_LIST");
        bindKey(GLFW.GLFW_KEY_F5, "SWITCH_PERSPECTIVE");
    }

    private int glfwNumConverter(String keybind){
        String fieldStr = "GLFW_KEY_" + keybind.toUpperCase();

        try {
            Field field = GLFW.class.getField(fieldStr);
            return field.getInt(null);
        } catch (NoSuchFieldException e) {
            System.err.println("[ERROR] GLFW Key wurde nicht gefunden");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    // keybinding setzen
    public void bindKey(int glfwKey, String action) {
        keyBindings.put(glfwKey, action);
    }

    private void bindKeySafe(String keyName, String action) {
        int key = glfwNumConverter(keyName);
        if (key == -1) {
            System.err.println("[WARN] Keybind übersprungen – unbekannte Taste: " + keyName);
            return;
        }
        bindKey(key, action);
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
        return (float) rawInput.getMouseDeltaX();
    }

    public float getMouseDeltaY() {
        return (float) rawInput.getMouseDeltaY();
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
