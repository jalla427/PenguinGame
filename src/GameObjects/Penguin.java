package GameObjects;

import tmp.Game;
import tmp.Handler;
import tmp.ID;

import java.awt.*;
import java.awt.geom.Area;

public class Penguin extends GameObject {

    public int color;
    public int animationFrame;
    public float animationTimer = 0;
    public boolean cueBall = false;

    private Polygon collision;
    private int[] xCollision;
    private int[] yCollision;

    public Penguin(float x, float y, int color, boolean cueBall) {
        super(x, y, 65, 65, ID.Penguin);
        this.color = color; //0 = normal, 1 = blue, 2 = red, 3 = green
        this.animationFrame = 1;

        if(Game.gameState == Game.STATE.Game) {
            updateCollision();
        }
    }

    public void tick() {
        if(Game.gameState == Game.STATE.Menu) {
            this.y += 1 * Game.deltaTime;
            if(this.y > Game.sHeight + 7) {
                Handler.removePenguin(this);
            }
        }

        if(Game.gameState == Game.STATE.Game && Handler.currentLevelArea != null) {
            handleCollision();
            updateCollision();
            collision.invalidate();
        }
    }

    public void render(Graphics g) {
        if(Game.gameState == Game.STATE.Menu) {
            this.animationTimer += 1 * Game.deltaTime;
            if(this.animationTimer >= 3) {
                this.animationFrame++;
                this.animationTimer = 0;
            }
            if(this.animationFrame > 105) { this.animationFrame = 1; }

            g.drawImage(Game.penguin_sheet_79x79_105.grabImageFast(color + 1, this.animationFrame), (int) (x - 7), (int) (y - 7), null);
        }

        if(Game.gameState == Game.STATE.Game) {
            this.animationTimer += 1 * Game.deltaTime;
            if(this.animationTimer >= 3) {
                if(this.velX > 0) { this.animationFrame++; }
                if(this.velX < 0) { this.animationFrame--; }
                this.animationTimer = 0;
            }
            if(this.animationFrame > 105) { this.animationFrame = 1; }
            if(this.animationFrame < 1) { this.animationFrame = 105; }

            g.drawImage(Game.penguin_sheet_79x79_105.grabImageFast(color + 1, this.animationFrame), (int) (x - 7), (int) (y - 7), null);
        }
    }

    protected void updateCollision() {
        this.xCollision = new int[] {(int) this.x, ((int) this.x) + this.width, ((int) x) + this.width, (int) this.x};
        this.yCollision = new int[] {(int) this.y + (this.height / 2), (int) this.y + (this.height / 2), ((int) this.y) + this.height, ((int) y) + this.height};

        this.collision = new Polygon();
        this.collision.xpoints = this.xCollision;
        this.collision.ypoints = this.yCollision;
        this.collision.npoints = this.xCollision.length;
    }

    protected void handleCollision() {
        //Stage collisions
        Area a1;
        Area a2 = Handler.currentLevelArea;

        //Horizontal collisions
        this.x += this.velX * Game.deltaTime;
        updateCollision();

        //Find area shared by penguin and tile
        a1 = new Area(this.collision);
        a1.intersect(a2);

        if(!a1.isEmpty()) {
            //Reverse bad movement
            this.x -= this.velX * Game.deltaTime;
            updateCollision();
            a1.reset();
            a1 = new Area(this.collision);
            a1.intersect(a2);

            //Move to the wall slowly until overlapping by one pixel
            while(a1.isEmpty()) {
                this.x += Math.signum(this.velX);
                updateCollision();
                a1.reset();
                a1 = new Area(this.collision);
                a1.intersect(a2);
            }

            //Position one pixel outside of wall
            this.x -= Math.signum(this.velX);
            updateCollision();
            this.velX = -this.velX;
        }

        //Vertical collisions
        this.y += this.velY * Game.deltaTime;
        updateCollision();

        //Find area shared by penguin and tile
        a1 = new Area(this.collision);
        a1.intersect(a2);

        if(!a1.isEmpty()) {
            //Reverse bad movement
            y -= velY * Game.deltaTime;
            updateCollision();
            a1.reset();
            a1 = new Area(collision);
            a1.intersect(a2);

            //Move to the wall slowly until overlapping by one pixel
            while(a1.isEmpty()) {
                y += Math.signum(velY);
                updateCollision();
                a1.reset();
                a1 = new Area(collision);
                a1.intersect(a2);
            }

            //Position one pixel outside of wall
            y -= Math.signum(velY);
            updateCollision();
            velY = -velY;
        }
    }

    public void launch(int targetX, int targetY, float speed) {
        double dx = targetX - this.x - (this.width / 2);
        double dy = targetY - this.y - (3 * (this.height / 4));
        double angle = Math.atan2(dy, dx);

        this.velX = (float) (Math.cos(angle) * speed);
        this.velY = (float) (Math.sin(angle) * speed);
    }

    public Polygon getBounds() {
        return null;
    }
}
