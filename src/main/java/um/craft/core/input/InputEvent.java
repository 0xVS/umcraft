package um.craft.core.input;

public record InputEvent(String action, Type type) {
    public enum Type {
        PRESSED, // taste wurde (gerade) gedrückt
        RELEASED, // taste wurde losgelassen
        HELD, // taste wird gehalten
        MOVED // maus wurde bewegt
    }
}
