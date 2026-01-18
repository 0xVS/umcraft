package um.craft.core;
import um.craft.core.utils.Constants;

import org.lwjgl.Version;

public class Launcher {
    public static void main(String[] args) {
        System.out.println(Version.getVersion());
        WindowManager wm = new WindowManager(Constants.TITLE, 1280, 720, false);
        wm.init();

        while (!wm.windowShouldClose()) {
            wm.update();
        }

        wm.cleanup();
    }
}
