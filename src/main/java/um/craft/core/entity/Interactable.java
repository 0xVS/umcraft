package um.craft.core.entity;

import um.craft.core.entity.living.player.Player;

public interface Interactable {
    default void onInteract(Player player){
        System.out.println("ja");
    }

    void test();

}
