package um.craft.core.render.ui;

import org.lwjgl.glfw.GLFW;
import um.craft.core.input.Inputs;

public class UIInput {
    private final Inputs inputs;


    private float x;
    private float y;

    private boolean leftDown;
    private boolean leftWasDown;
    private boolean leftJustPressed;
    private boolean leftJustReleased;

    public UIInput(Inputs inputs) {
        this.inputs = inputs;
    }

    public void update() {
        x = inputs.getMouseX();
        y = inputs.getMouseY();

        leftDown = inputs.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT);
        leftJustPressed = leftDown && !leftWasDown;
        leftJustReleased = !leftDown && leftWasDown;
        leftWasDown = leftDown;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public boolean isLeftJustPressed() { return leftJustPressed; }
    public boolean isLeftJustReleased() { return leftJustReleased; }

}
