package um.craft.core;

import org.lwjgl.Version;

public class Launcher {
    public static void main(String[] args) {
        System.out.println(Version.getVersion());
        WindowManager wm = new WindowManager("umcraft", 500, 300, false);
        wm.init();

        while (!wm.windowShouldClose()) {
            wm.update();
        }

        wm.cleanup();
    }
}
