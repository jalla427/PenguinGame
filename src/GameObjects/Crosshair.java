package GameObjects;

import tmp.Game;
import tmp.ID;

import java.awt.*;

public class Crosshair extends GameObject {

    public Crosshair(float x, float y) {
        super(x, y, 25, 25, ID.Crosshair);
    }

    public void tick() {

    }


    public void render(Graphics g) {
        g.drawImage(Game.crosshair_25x25, (int) x, (int) y, null);
    }


    public Polygon getBounds() {
        return null;
    }
}
