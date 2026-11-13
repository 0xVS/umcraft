package um.craft.core.entity;

import um.craft.core.entity.living.player.Player;
import um.craft.core.util.Log;

public interface Interactable {
    default void onInteract(Player player){
        Log.debug("ja");
    }

    void test();

}
