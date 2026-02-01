package tmp;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import MenuItems.*;
import MenuItems.Button;
import tmp.Handler;

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

        if(Game.gameState == Game.STATE.Controls && buttonClicked != null) {
            //Controls menu
            if(buttonClicked.getName() == "BACK") {
                buttonClicked.isClicked = true;
            }
        }

        if(Game.gameState == Game.STATE.Game) {
            //Place crosshair marker
            if(my > 100 && my < (Game.sHeight - 106) && mx > 18 && mx < Game.sWidth - 21) {
                if(Handler.areCrosshair()) { Handler.removeCrosshair(); }
                Handler.addCrosshair(mx - 12, my - 12);
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
                Handler.clearButtons();
                Handler.clearPenguins();
                Game.gameState = Game.STATE.Game;
            }
            if(buttonClicked.getName() == "CONTROLS") {
                Handler.clearButtons();
                Game.gameState = Game.STATE.Controls;
            }
            if(buttonClicked.getName() == "QUIT") {
                System.exit(1);
            }
        }

        if(Game.gameState == Game.STATE.Controls && buttonClicked != null) {
            //Controls menu
            if(buttonClicked.getName() == "BACK") {
                Handler.clearButtons();
                Game.gameState = Game.STATE.Menu;
            }
        }
    }

    protected void tick() {
        boolean buttonsFound = Handler.areButtons();

        //Create main menu buttons
        if(Game.gameState == Game.STATE.Menu && !buttonsFound) {
            Handler.addButton(new ImageTextButton(mainButtonFont, Color.black, "PLAY", Game.button_menu_200x120, Game.button_menu_hover_200x120, (Game.sWidth / 2) - 100, (Game.sHeight / 2) - 60, 200, 120));
            Handler.addButton(new ImageTextButton(mainButtonFont, Color.black, "CONTROLS", Game.button_menu_200x120, Game.button_menu_hover_200x120, (Game.sWidth / 2) - 100, (Game.sHeight / 2) + 70, 200, 120));
            Handler.addButton(new ImageTextButton(mainButtonFont, Color.black, "QUIT", Game.button_menu_200x120, Game.button_menu_hover_200x120, (Game.sWidth / 2) - 100, (Game.sHeight / 2) + 200, 200, 120));
        }

        //Create controls menu buttons
        if(Game.gameState == Game.STATE.Controls && !buttonsFound) {
            Handler.addButton(new ImageTextButton(mainButtonFont, Color.black, "BACK", Game.button_menu_200x120, Game.button_menu_hover_200x120, (Game.sWidth / 2) - 100, Game.sHeight - 150, 200, 120));
        }
    }
}
