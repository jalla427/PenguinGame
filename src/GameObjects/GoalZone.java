package GameObjects;

import tmp.*;

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

        boolean inGoal = false;
        Penguin hitPenguin = null;

        for(Penguin penguin:Handler.penguinList) {
            a1 = new Area(this.collision);
            a2 = new Area(penguin.getBounds());
            a1.intersect(a2);

            if(!a1.isEmpty()) {
                inGoal = true;
                hitPenguin = penguin;
                break;
            }
        }

        if(inGoal) {
            if (hitPenguin.color == Game.currentSequence[Game.sequenceTarget - 1]) {
                Handler.removePenguin(hitPenguin);
                AudioPlayer.playSound("/goal.wav");
                Game.sequenceTarget++;
                if (Game.sequenceTarget > Game.currentSequence.length) {
                    Game.levelComplete = true;
                    Game.passed = true;
                    Game.sequenceTarget = 1;
                }
            } else if (hitPenguin.color != Game.currentSequence[Game.sequenceTarget - 1] && hitPenguin.color != 0) {
                AudioPlayer.playRandomNeutralPenguinSound();
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
