package um.craft.core.input;

public class InputEvent {
    public enum Type {
        PRESSED, // taste wurde (gerade) gedrückt
        RELEASED, // taste wurde losgelassen
        HELD, // taste wird gehalten
        MOVED // maus wurde bewegt
    }

    private final String action;
    private final Type type;
    private final long timestamp;

    public InputEvent(String action, Type type) {
        this.action = action;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    public String getAction() {
        return action;
    }

    public Type getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
