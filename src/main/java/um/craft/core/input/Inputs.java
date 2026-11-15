package um.craft.core.input;


import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

public class Inputs {
    // array mit größe der anzahl von allen unterstützen tasten von glfw
    private final boolean[] keys =  new boolean[GLFW.GLFW_KEY_LAST];

    // das gleiche für mouse buttons
    private final boolean[] buttons =  new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

    private static double lastMouseX, lastMouseY;
    private static double mouseDeltaX, mouseDeltaY;
    private static double mouseX, mouseY;





    private boolean firstMouse = true;

    private final GLFWKeyCallback keyboard;
    private final GLFWCursorPosCallback cursorMovement;
    private final GLFWMouseButtonCallback mouseButtons;

    public Inputs() {
        keyboard = new GLFWKeyCallback() {
            @Override
            /*
                key = spezifischer key beim drücken
                scancode =
             */
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key < 0 || key >= keys.length) return;
                keys[key] = action != GLFW.GLFW_RELEASE;
            }
        };

        cursorMovement = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xPos, double yPos) {
                if (firstMouse) {
                    lastMouseX = xPos;
                    lastMouseY = yPos;
                    updateMousePosition(window, xPos, yPos);
                    mouseDeltaX = 0;
                    mouseDeltaY = 0;
                    firstMouse = false;
                    return;
                }

                mouseDeltaX = xPos - lastMouseX;
                mouseDeltaY = lastMouseY - yPos;

                updateMousePosition(window, xPos, yPos);

                lastMouseX = xPos;
                lastMouseY = yPos;
            }
        };

        mouseButtons = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                buttons[button] = action != GLFW.GLFW_RELEASE;
            }
        };
    }

    // GLFW liefert Cursor Pos in Fenster Koordinaten
    // UI wird jedoch in Framebuffer/Pixel Koordinaten gerendert -> umrechnen
    private static void updateMousePosition(long window, double xPos, double yPos) {
        float scaleX = 1f;
        float scaleY = 1f;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer windowWidth = stack.mallocInt(1);
            IntBuffer windowHeight = stack.mallocInt(1);
            IntBuffer framebufferWidth = stack.mallocInt(1);
            IntBuffer framebufferHeight = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(window, windowWidth, windowHeight);
            GLFW.glfwGetFramebufferSize(window, framebufferWidth, framebufferHeight);

            if (windowWidth.get(0) > 0) {
                scaleX = (float) framebufferWidth.get(0) / windowWidth.get(0);
            }
            if (windowHeight.get(0) > 0) {
                scaleY = (float) framebufferHeight.get(0) / windowHeight.get(0);
            }
        }

        mouseX = xPos * scaleX;
        mouseY = yPos * scaleY;
    }

    public boolean isKeyDown(int key) {
        return keys[key];
    }
    public boolean isButtonDown(int button) {
        return buttons[button];
    }

    public void reset() {
        keyboard.free();
        mouseButtons.free();
        cursorMovement.free();
    }

    public float getMouseX() { return (float) mouseX; }
    public float getMouseY() { return (float) mouseY; }

    public double getMouseDeltaX() {
        return mouseDeltaX;
    }

    public double getMouseDeltaY() {
        return mouseDeltaY;
    }

    public void resetMouseDelta(){ mouseDeltaX = 0; mouseDeltaY = 0; }

    public GLFWKeyCallback getKeyboardCallback() {
        return keyboard;
    }

    public GLFWCursorPosCallback getCursorMovementCallback() {
        return cursorMovement;
    }

    public GLFWMouseButtonCallback getMouseButtonsCallback() {
        return mouseButtons;
    }
}
