package tmp;

import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

import Menu.ImageButton;

public class Menu extends MouseAdapter {
    protected static void tick() {
        boolean buttonsFound = !Handler.areButtons();

        if(Game.gameState == Game.STATE.Menu && buttonsFound) {
            Handler.addButton(new ImageButton("Play", Game.button_menu_200x120, (Game.sWidth / 2) + 100, (Game.sHeight / 2) + 60, 200, 120));
        }
    }

}
