package tmp;

import Menu.ImageButton;

import java.awt.*;
import java.util.ArrayList;

public class Handler {

    public static ArrayList<ImageButton> buttonList = new ArrayList<>(5);

    public static void tick() {

    }

    public static void renderLowerElements(Graphics g) {

    }

    public static void renderMiddleElements(Graphics g) {

    }

    public static void renderHigherElements(Graphics g) {
        if(Game.gameState == Game.STATE.Menu) {
            for(int i = 0; i < buttonList.size(); i++) {
                buttonList.get(i).render(g);
            }
        }
    }

    public static void clearButtons() {
        while(areButtons()) {
            buttonList.remove(0);
        }
    }

    public static boolean areButtons() {
        return !buttonList.isEmpty();
    }

    public static void addButton(ImageButton button) { buttonList.add(button); }
    public void removeButton(ImageButton button) { buttonList.remove(button); }
}
