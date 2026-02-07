package tmp;

import GameObjects.*;
import MenuItems.*;
import MenuItems.Button;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Comparator;

public class Handler {
    public static ArrayList<Button> buttonList = new ArrayList<>(5);
    public static ArrayList<Penguin> penguinList = new ArrayList<>(20);
    public static ArrayList<Crosshair> crosshairList = new ArrayList<>(1);
    public static ArrayList<StageChunk> stageList = new ArrayList<>(5);
    public static GoalZone goalZone;
    public static Area currentLevelArea = null;
    public static Penguin cueBallPenguin = null;

    private static int greenValue = 255;

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

        if(Game.gameState == Game.STATE.Game) {
            if(!Game.transitioning && !Game.levelComplete) {
                for (int i = 0; i < penguinList.size(); i++) {
                    penguinList.get(i).tick();
                }
                if (cueBallPenguin != null) {
                    if (!arePenguinsMoving() && !cueBallPenguin.cueBall) {
                        setNewCueBallPenguin();
                    }
                }
                if (goalZone != null) {
                    goalZone.tick();
                }
            }
        }
    }

    public static void renderLowerElements(Graphics g) {
        if(Game.gameState == Game.STATE.Game) {
            g.drawImage(Game.stage_boundary_1280x720, 0, 0, null);
        }
    }

    public static void renderMiddleElements(Graphics g) {
        penguinList.sort(Comparator.comparing(Penguin::getY));
        if(Game.gameState == Game.STATE.Menu || Game.gameState == Game.STATE.Game) {
            for(int i = 0; i < penguinList.size(); i++) {
                penguinList.get(i).render(g);
            }
        }

        //Lower stage wall
        if(Game.gameState == Game.STATE.Game) {
            g.drawImage(Game.stage_bottom_1280x106, 0, Game.sHeight - 106, null);

            for(int i = 0; i < stageList.size(); i++) {
                stageList.get(i).render(g);
            }
        }
    }

    public static void renderHigherElements(Graphics g) {
        if(Game.gameState == Game.STATE.Game) {
            //Charge Meter
            g.setColor(Color.lightGray);
            g.fillRect(59, 663, 191, 47);
            greenValue = Game.clamp((int) (255 - ((Game.chargePower / 10) * 255)), 0, 255);
            g.setColor(new Color(75, greenValue, 0));
            g.fillRect(59, 663, (int) (Game.chargePower * 19.1), 47);

            //Crosshair
            for(int i = 0; i < crosshairList.size(); i++) {
                if(areCrosshair()) { crosshairList.get(i).render(g); }
            }
        }

        if(Game.gameState == Game.STATE.Menu) {
            g.drawImage(Game.main_banner_600x250, 340, 40, null);
        }

        if(Game.gameState == Game.STATE.About) {
            g.drawImage(Game.about_page, 40, 40, null);
        }

        if(Game.gameState == Game.STATE.Game && Game.transitioning) {
            if(Game.passed) { g.drawImage(Game.clear_label_600x250, 340, 150, null); }
            else { g.drawImage(Game.fail_label_600x250, 340, 150, null); }
        }

        for(int i = 0; i < buttonList.size(); i++) {
            if(Game.gameState == Game.STATE.Game) {
                if(buttonList.get(i).getName() == "SCORE_DISPLAY") { buttonList.get(i).setText(Integer.toString(Game.strokes)); }
                if(buttonList.get(i).getName() == "PAR_DISPLAY") { buttonList.get(i).setText(Integer.toString(Game.currentPar)); }
                if(buttonList.get(i).getName() == "BEST_DISPLAY") {
                    if(LevelCollection.getLevelBest(Game.currentLevel) < 9999) { buttonList.get(i).setText(Integer.toString(LevelCollection.getLevelBest(Game.currentLevel))); }
                    else { buttonList.get(i).setText("?"); }
                }
            }

            buttonList.get(i).render(g);
        }

        //Target Color
        if(Game.gameState == Game.STATE.Game && !Game.transitioning) {
            //0 = normal, 1 = blue, 2 = red, 3 = green
            g.setColor(Color.white);
            if(Game.currentSequence[Game.sequenceTarget - 1] == 1) { g.setColor(new Color(23, 21, 138)); }
            if(Game.currentSequence[Game.sequenceTarget - 1] == 2) { g.setColor(new Color(138, 21, 21)); }
            if(Game.currentSequence[Game.sequenceTarget - 1] == 3) { g.setColor(new Color(16, 107, 21)); }
            g.fillRect(665, 669, 35, 35);
        }
    }

    public static void findTotalLevelArea() {
        Area combinedLevel = new Area();
        for (int i = 0; i < stageList.size(); i++) {
            GameObject tempObject = stageList.get(i);
            if (tempObject.getID() == ID.Level) {
                combinedLevel.add(new Area(tempObject.getBounds()));
            }
        }
        currentLevelArea = combinedLevel;
    }

    public static void clearStageBoundaries() {
        while(areStageBoundaries()) {
            stageList.remove(0);
        }
    }
    public static boolean areStageBoundaries() {
        if(stageList.isEmpty()) { return false; }
        return true;
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

    public static void setNewCueBallPenguin() {
        cueBallPenguin = new Penguin(50, (float) Game.sHeight / 2, 0, true);

        //Remove any penguins in the way
        for(Penguin penguin:penguinList) {
            Area a1 = new Area(penguin.getBounds());
            Area a2 = new Area(cueBallPenguin.getBounds());

            a1.intersect(a2);

            if(!a1.isEmpty() && penguin.color == 0) {
                removePenguin(penguin);
            } else if(!a1.isEmpty()) {
                if(penguin.getY() >= cueBallPenguin.getY()) {
                    cueBallPenguin.setY(cueBallPenguin.getY() - 100);
                } else if(penguin.getY() < cueBallPenguin.getY()) {
                    cueBallPenguin.setY(cueBallPenguin.getY() + 100);
                }
            }
        }

        addPenguin(Handler.cueBallPenguin);
    }

    public static boolean arePenguinsMoving() {
        for(Penguin penguin:penguinList) {
            if(penguin.getVelX() != 0 || penguin.getVelY() != 0) { return true; }
        }
        return false;
    }
    public static void clearPenguins() {
        while(arePenguins()) {
            penguinList.remove(0);
        }
        cueBallPenguin = null;
    }
    public static boolean arePenguins() { return !penguinList.isEmpty(); }
    public static void addPenguin(Penguin penguin) { penguinList.add(penguin); }
    public static void removePenguin(Penguin penguin) { penguinList.remove(penguin); }

    public static boolean areCrosshair() {
        return !crosshairList.isEmpty();
    }
    public static void addCrosshair(float x, float y) {
        crosshairList.add(new Crosshair(x, y));
    }
    public static void removeCrosshair() {
        while(areCrosshair()) {
            crosshairList.remove(0);
        }
    }
}
