package GameObjects;

import tmp.ID;

import java.awt.*;

public class StageChunk extends GameObject {

    private Polygon collision;
    private int[] xCollision;
    private int[] yCollision;

    public StageChunk(int x, int y, int width, int height) {
        super(x, y, width, height, ID.Level);
        updateCollision();
    }

    public void tick() {

    }

    public void render(Graphics g) {

    }

    public Polygon getBounds() {
        return collision;
    }

    private void updateCollision() {
        xCollision = new int[] {(int) this.x, (int) this.x + this.width, (int) this.x + this.width, (int) x};
        yCollision = new int[] {(int) this.y, (int) this.y, (int) this.y + this.height, (int) this.y + this.height};

        collision = new Polygon();
        collision.xpoints = xCollision;
        collision.ypoints = yCollision;
        collision.npoints = xCollision.length;

    }
}
