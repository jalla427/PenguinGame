package tmp;

import GameObjects.*;
import MenuItems.*;
import MenuItems.Button;

import java.awt.*;
import java.util.ArrayList;

public class Handler {

    public static ArrayList<Button> buttonList = new ArrayList<>(5);
    public static ArrayList<Penguin> penguinList = new ArrayList<>(20);

    public static void tick() {
        if(Game.gameState == Game.STATE.Menu) {
            for(int i = 0; i < buttonList.size(); i++) {
                buttonList.get(i).tick();
            }
            for(int i = 0; i < buttonList.size(); i++) {
                buttonList.get(i).tick();
            }
            for(int i = 0; i < penguinList.size(); i++) {
                penguinList.get(i).tick();
            }
        }
    }

    public static void renderLowerElements(Graphics g) {

    }

    public static void renderMiddleElements(Graphics g) {
        if(Game.gameState == Game.STATE.Menu) {
            for(int i = 0; i < penguinList.size(); i++) {
                penguinList.get(i).render(g);
            }
        }
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
    public static void addButton(Button button) { buttonList.add(button); }
    public static void removeButton(Button button) { buttonList.remove(button); }
    public static Button getButtonAtLocation(int mx, int my) {
        for(int i = 0; i < buttonList.size(); i++) {
            Button tempObject = buttonList.get(i);
            if(Game.isPointInBounds(mx, my, tempObject.getX(), tempObject.getY(), tempObject.getWidth(), tempObject.getHeight())) {
                return tempObject;
            }
        }
        return null;
    }
    public static boolean arePenguins() { return !penguinList.isEmpty(); }
    public static void addPenguin(Penguin penguin) { penguinList.add(penguin); }
    public static void removePenguin(Penguin penguin) { penguinList.remove(penguin); }
}
