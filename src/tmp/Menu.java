package tmp;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import MenuItems.*;
import MenuItems.Button;

import javax.swing.*;

public class Menu extends MouseAdapter {

    public static Font mainButtonFont = new Font("SansSerif", Font.BOLD, 32);
    public static Font smallButtonFont = new Font("SansSerif", Font.BOLD, 20);

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
            if(buttonClicked.getName() == "ABOUT") {
                buttonClicked.isClicked = true;
            }
            if(buttonClicked.getName() == "QUIT") {
                buttonClicked.isClicked = true;
            }
        }

        if(Game.gameState == Game.STATE.About && buttonClicked != null) {
            //Controls menu
            if(buttonClicked.getName() == "BACK") {
                buttonClicked.isClicked = true;
            }
        }

        if(Game.gameState == Game.STATE.Game) {
            //Game buttons
            if(buttonClicked != null) {
                if(buttonClicked.getName() == "RESET") {
                    buttonClicked.isClicked = true;
                }
                if(buttonClicked.getName() == "MENU") {
                    buttonClicked.isClicked = true;
                }
            }

            //Place crosshair marker
            if(my > 100 && my < (Game.sHeight - 106) && mx > 18 && mx < Game.sWidth - 21 && SwingUtilities.isLeftMouseButton(e) && !Handler.arePenguinsMoving()) {
                if(Handler.areCrosshair()) { Handler.removeCrosshair(); }
                Handler.addCrosshair(mx - 12, my - 12);
            }
            //Charge launch
            if(Handler.areCrosshair() && SwingUtilities.isRightMouseButton(e)) {
                Game.charging = true;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        //Grab mouse coordinates and check for overlap with any button
        int mx = e.getX();
        int my = e.getY();
        Button buttonClicked = Handler.getButtonAtLocation(mx, my);

        //All buttons
        for (int i = 0; i < Handler.buttonList.size(); i++) {
            Handler.buttonList.get(i).isClicked = false;
        }

        if(Game.gameState == Game.STATE.Menu && buttonClicked != null) {
            //Main menu
            if(buttonClicked.getName() == "PLAY") {
                AudioPlayer.playSound("/buttonClick.wav");
                Handler.clearButtons();
                Handler.clearPenguins();
                Game.gameState = Game.STATE.Game;
                Game.beginGame(1);
            }
            if(buttonClicked.getName() == "ABOUT") {
                AudioPlayer.playSound("/buttonClick.wav");
                Handler.clearButtons();
                Game.gameState = Game.STATE.About;
            }
            if(buttonClicked.getName() == "QUIT") {
                AudioPlayer.playSound("/buttonClick.wav");
                System.exit(1);
            }
        }

        if(Game.gameState == Game.STATE.About && buttonClicked != null) {
            //Controls menu
            if(buttonClicked.getName() == "BACK") {
                AudioPlayer.playSound("/buttonClick.wav");
                Handler.clearButtons();
                Game.gameState = Game.STATE.Menu;
            }
        }

        if(Game.gameState == Game.STATE.Game) {
            //Reset
            if(buttonClicked != null) {
                if(buttonClicked.getName() == "RESET") {
                    AudioPlayer.playSound("/buttonClick.wav");
                    Handler.resetTrigger = true;
                }
                if(buttonClicked.getName() == "MENU") {
                    AudioPlayer.playSound("/buttonClick.wav");
                    Game.clearGameElements();
                    Game.resetLevelVars();
                    Game.gameState = Game.STATE.Menu;
                }
                if(buttonClicked.getName() == "CONTINUE" && Game.transitioning) {
                    AudioPlayer.playSound("/buttonClick.wav");
                    if(Game.passed) { Game.currentLevel = Game.clamp(Game.currentLevel + 1, 1, LevelCollection.levels.length); }
                    Game.resetBoard();
                }
            }

            //Launch
            if(Handler.areCrosshair() && SwingUtilities.isRightMouseButton(e)) {
                Handler.cueBallPenguin.launch((int) Handler.crosshairList.get(0).getX(), (int) Handler.crosshairList.get(0).getY(), Game.chargePower);
                Handler.removeCrosshair();
                Game.charging = false;
                Game.chargePower = 0;
                Game.strokes++;
            }
        }
    }

    protected void tick() {
        boolean buttonsFound = Handler.areButtons();

        //Create main menu buttons
        if(Game.gameState == Game.STATE.Menu && !buttonsFound) {
            Handler.addButton(new ImageTextButton(mainButtonFont, Color.black, "PLAY", Game.button_menu_200x120, Game.button_menu_hover_200x120, (Game.sWidth / 2) - 100, (Game.sHeight / 2) - 60, 200, 120));
            Handler.addButton(new ImageTextButton(mainButtonFont, Color.black, "ABOUT", Game.button_menu_200x120, Game.button_menu_hover_200x120, (Game.sWidth / 2) - 100, (Game.sHeight / 2) + 70, 200, 120));
            Handler.addButton(new ImageTextButton(mainButtonFont, Color.black, "QUIT", Game.button_menu_200x120, Game.button_menu_hover_200x120, (Game.sWidth / 2) - 100, (Game.sHeight / 2) + 200, 200, 120));
        }

        //Create controls menu buttons
        if(Game.gameState == Game.STATE.About && !buttonsFound) {
            Handler.addButton(new ImageTextButton(mainButtonFont, Color.black, "BACK", Game.button_menu_200x120, Game.button_menu_hover_200x120, (Game.sWidth / 2) - 100, Game.sHeight - 150, 200, 120));
        }

        //Create in-game buttons
        if(Game.gameState == Game.STATE.Game && !buttonsFound) {
            Handler.addButton(new ImageTextButton(smallButtonFont, Color.black, "RESET", Game.button_retry_95x47, Game.button_retry_pressed_95x47, Game.sWidth - 93, 615, 95, 47));
            Handler.addButton(new ImageTextButton(smallButtonFont, Color.black, "MENU", Game.button_menu_95x47, Game.button_menu_pressed_95x47, Game.sWidth - 93, 663, 95, 47));
            Handler.addButton(new ImageButton("LAUNCH_OVERLAY", Game.overlay_launch_191x47, Game.overlay_launch_191x47, 59, 663, 191, 47));
            Handler.addButton(new ImageTextButton(smallButtonFont, Color.black, "LAUNCH POWER", Game.overlay_launch_label_191x47, Game.overlay_launch_label_191x47, 59, 615, 191, 47));
            Handler.addButton(new ImageTextButton(smallButtonFont, Color.black, "SCORE", Game.score_label_95x47, Game.score_label_95x47, 347, 615, 95, 47));
            Handler.addButton(new ImageTextButton(smallButtonFont, Color.black, "PAR", Game.score_label_95x47, Game.score_label_95x47, 443, 615, 95, 47));
            Handler.addButton(new ImageTextButton(smallButtonFont, Color.black, "BEST", Game.score_label_95x47, Game.score_label_95x47, 539, 615, 95, 47));
            Handler.addButton(new ImageTextDisplayButton(smallButtonFont, Color.black, "SCORE_DISPLAY", "0", Game.score_display_47x47, Game.score_display_47x47, 371, 663, 47, 47));
            Handler.addButton(new ImageTextDisplayButton(smallButtonFont, Color.black, "PAR_DISPLAY", "0", Game.score_display_47x47, Game.score_display_47x47, 467, 663, 47, 47));
            Handler.addButton(new ImageTextDisplayButton(smallButtonFont, Color.black, "BEST_DISPLAY", "0", Game.score_display_47x47, Game.score_display_47x47, 563, 663, 47, 47));
            Handler.addButton(new ImageTextButton(smallButtonFont, Color.black, "TARGET", Game.score_label_95x47, Game.score_label_95x47, 635, 615, 95, 47));
            Handler.addButton(new ImageTextDisplayButton(smallButtonFont, Color.black, "TARGET_DISPLAY", "", Game.score_display_47x47, Game.score_display_47x47, 659, 663, 47, 47));
        }
    }
}
