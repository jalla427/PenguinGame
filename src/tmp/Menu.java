package tmp;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import MenuItems.*;
import MenuItems.Button;

public class Menu extends MouseAdapter {

    public static Font mainButtonFont = new Font("SansSerif", Font.BOLD, 32);

    public Menu() {}

    public void mousePressed(MouseEvent e) {
        //Grab mouse coordinates and check for overlap with any button
        int mx = e.getX();
        int my = e.getY();
        Button buttonClicked = Handler.getButtonAtLocation(mx, my);

        if(Game.gameState == Game.STATE.Menu && buttonClicked != null) {
            //Main menu
            if(buttonClicked.getName() == "PLAY") {
                buttonClicked.isClicked = true;
            }
            if(buttonClicked.getName() == "CONTROLS") {
                buttonClicked.isClicked = true;
            }
            if(buttonClicked.getName() == "QUIT") {
                buttonClicked.isClicked = true;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        //Grab mouse coordinates and check for overlap with any button
        int mx = e.getX();
        int my = e.getY();
        Button buttonClicked = Handler.getButtonAtLocation(mx, my);

        if(Game.gameState == Game.STATE.Menu) {
            //All buttons
            for (int i = 0; i < Handler.buttonList.size(); i++) {
                Handler.buttonList.get(i).isClicked = false;
            }
        }

        if(Game.gameState == Game.STATE.Menu && buttonClicked != null) {
            //Main menu
            if(buttonClicked.getName() == "PLAY") {
                //Game.gameState = Game.STATE.Game;
                //Game.clearButtons = true;
            }
            if(buttonClicked.getName() == "CONTROLS") {
                //Game.gameState = Game.STATE.Controls;
                //Game.clearButtons = true;
            }
            if(buttonClicked.getName() == "QUIT") {
                System.exit(1);
            }
        }
    }

    protected void tick() {
        boolean buttonsFound = !Handler.areButtons();

        if(Game.gameState == Game.STATE.Menu && buttonsFound) {
            Handler.addButton(new ImageTextButton(mainButtonFont, Color.black, "PLAY", Game.button_menu_200x120, Game.button_menu_hover_200x120, (Game.sWidth / 2) - 100, (Game.sHeight / 2) - 60, 200, 120));
            Handler.addButton(new ImageTextButton(mainButtonFont, Color.black, "CONTROLS", Game.button_menu_200x120, Game.button_menu_hover_200x120, (Game.sWidth / 2) - 100, (Game.sHeight / 2) + 70, 200, 120));
            Handler.addButton(new ImageTextButton(mainButtonFont, Color.black, "QUIT", Game.button_menu_200x120, Game.button_menu_hover_200x120, (Game.sWidth / 2) - 100, (Game.sHeight / 2) + 200, 200, 120));
        }
    }
}
