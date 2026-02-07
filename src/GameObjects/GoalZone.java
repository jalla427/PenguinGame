package GameObjects;

import tmp.Game;
import tmp.Handler;
import tmp.ID;
import tmp.LevelCollection;

import java.awt.*;
import java.awt.geom.Area;

public class GoalZone extends GameObject {

    private Polygon collision;
    private int[] xCollision;
    private int[] yCollision;

    public GoalZone(int x, int y, int width, int height) {
        super(x, y, width, height, ID.Goal);
        updateCollision();
    }

    public void tick() {
        Area a1;
        Area a2;

        for(Penguin penguin:Handler.penguinList) {
            a1 = new Area(this.collision);
            a2 = new Area(penguin.getBounds());
            a1.intersect(a2);

            if(!a1.isEmpty() && penguin.color == Game.currentSequence[Game.sequenceTarget - 1]) {
                Handler.removePenguin(penguin);
                Game.sequenceTarget++;
                if(Game.sequenceTarget > Game.currentSequence.length) {
                    Game.levelComplete = true;
                    Game.passed = true;
                    Game.sequenceTarget = 1;
                }
                break;
            } else if(!a1.isEmpty() && penguin.color != Game.currentSequence[Game.sequenceTarget - 1] && penguin.color != 0) {
                Game.levelComplete = true;
                Game.passed = false;
                Game.sequenceTarget = 1;
            }
        }
    }

    public void render(Graphics g) {

    }

    public Polygon getBounds() {
        return collision;
    }

    private void updateCollision() {
        xCollision = new int[] {(int) this.x + 14, (int) this.x + this.width, (int) this.x + this.width, (int) this.x + 14};
        yCollision = new int[] {(int) this.y, (int) this.y, (int) this.y + this.height, (int) this.y + this.height};

        collision = new Polygon();
        collision.xpoints = xCollision;
        collision.ypoints = yCollision;
        collision.npoints = xCollision.length;

    }
}
