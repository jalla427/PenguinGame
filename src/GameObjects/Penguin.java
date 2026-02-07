package GameObjects;

import tmp.AudioPlayer;
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
    double friction = 0.001;

    private Polygon collision;
    private int[] xCollision;
    private int[] yCollision;

    public Penguin(float x, float y, int color, boolean cueBall) {
        super(x, y, 65, 65, ID.Penguin);
        this.color = color; //0 = normal, 1 = blue, 2 = red, 3 = green
        this.animationFrame = 1;
        this.cueBall = cueBall;

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
            //Remove if penguin is out of bounds
            if(((this.x + this.width) > Game.sWidth || (this.x + this.width) < 100)) {
                if(this.color == 0) { Handler.penguinList.remove(this); }
            }

            handleCollision();
            updateCollision();
            collision.invalidate();
            applyFriction();
        }
    }

    public void render(Graphics g) {
        float rotateSpeed = Game.clamp((Math.max(Math.abs(this.velY), Math.abs(this.velX)) / 10) * 3, (float) 0.5, 10);
        if(Game.transitioning) { rotateSpeed = 0; }

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
            this.animationTimer += rotateSpeed * Game.deltaTime;
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
        boolean collidedStageX = false;
        boolean collidedStageY = false;

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
            collidedStageX = true;
        } else {
            //No stage collisions, walk back move. Will be checked again against penguins
            this.x -= this.velX * Game.deltaTime;
        }

        //Vertical collisions
        this.y += this.velY * Game.deltaTime;
        updateCollision();

        //Find area shared by penguin and tile
        a1 = new Area(this.collision);
        a1.intersect(a2);

        if(!a1.isEmpty()) {
            //Reverse bad movement
            this.y -= this.velY * Game.deltaTime;
            updateCollision();
            a1.reset();
            a1 = new Area(this.collision);
            a1.intersect(a2);

            //Move to the wall slowly until overlapping by one pixel
            while(a1.isEmpty()) {
                this.y += Math.signum(this.velY);
                updateCollision();
                a1.reset();
                a1 = new Area(this.collision);
                a1.intersect(a2);
            }

            //Position one pixel outside of wall
            this.y -= Math.signum(this.velY);
            updateCollision();
            this.velY = -this.velY;
            collidedStageY = true;
        } else {
            //No stage collisions, walk back move. Will be checked again against penguins
            this.y -= this.velY * Game.deltaTime;
        }

        //Penguin collisions
        for(Penguin penguin:Handler.penguinList) {
            if(penguin == this) { continue; }

            a2 = new Area(penguin.getBounds());

            //Horizontal collisions
            if(!collidedStageX) {
                this.x += this.velX * Game.deltaTime;
                updateCollision();

                //Find area shared by penguins
                a1 = new Area(this.collision);
                a1.intersect(a2);

                if(!a1.isEmpty()) {
                    //Reverse bad movement
                    this.x -= this.velX * Game.deltaTime;
                    updateCollision();
                    a1.reset();
                    a1 = new Area(this.collision);
                    a1.intersect(a2);

                    //Move to slowly until overlapping by one pixel
                    while(a1.isEmpty()) {
                        this.x += Math.signum(this.velX);
                        updateCollision();
                        a1.reset();
                        a1 = new Area(this.collision);
                        a1.intersect(a2);
                    }

                    //Position one pixel outside
                    this.x -= Math.signum(this.velX);
                    updateCollision();

                    //Split the velocity between the two affected penguins
                    penguin.velX = 3 * (this.velX / 4);
                    this.velX = -this.velX / 4;
                    if(penguin.velY == 0) {
                        penguin.velY = -this.velY / 2;
                    }

                    collidedStageX = true;
                } else {
                    //No collisions, walk back move. Will be checked again against remaining penguins
                    this.x -= this.velX * Game.deltaTime;
                }
            }

            //Vertical collisions
            if(!collidedStageY) {
                this.y += this.velY * Game.deltaTime;
                updateCollision();

                //Find area shared by penguins
                a1 = new Area(this.collision);
                a1.intersect(a2);

                if(!a1.isEmpty()) {
                    //Reverse bad movement
                    this.y -= this.velY * Game.deltaTime;
                    updateCollision();
                    a1.reset();
                    a1 = new Area(this.collision);
                    a1.intersect(a2);

                    //Move to slowly until overlapping by one pixel
                    while(a1.isEmpty()) {
                        this.y += Math.signum(this.velY);
                        updateCollision();
                        a1.reset();
                        a1 = new Area(this.collision);
                        a1.intersect(a2);
                    }

                    //Position one pixel outside
                    this.y -= Math.signum(this.velY);
                    updateCollision();

                    //Split the velocity between the two affected penguins
                    penguin.velY = 3 * (this.velY / 4);
                    this.velY = -this.velY / 4;

                    if(penguin.velX == 0) {
                        penguin.velX = -this.velX / 2;
                    }

                    collidedStageY = true;
                } else {
                    //No collisions, walk back move. Will be checked again against remaining penguins
                    this.y -= this.velY * Game.deltaTime;
                }
            }
        }

        //If no collisions were found on an axis, allow the move
        if(!collidedStageX) { this.x += this.velX * Game.deltaTime; }
        if(!collidedStageY) { this.y += this.velY * Game.deltaTime; }

        //If any collisions, play penguin noise
        if((collidedStageX || collidedStageY) && this.velX != 0 && this.velY != 0) { AudioPlayer.playRandomNeutralPenguinSound(); }

        //If penguin is too far out of bounds, kill velocity
        if(this.x > Game.sWidth + 150) {
            this.velX = 0;
            this.velY = 0;
        }
    }

    private void applyFriction() {
        double decay = Math.exp(-this.friction * Game.deltaTime);

        this.velX *= decay;
        this.velY *= decay;
        this.friction *= 1.00005;

        if(Math.abs(this.velX) < 0.04 && Math.abs(this.velY) < 0.04) {
            this.velX = 0;
            this.velY = 0;
            this.friction = 0.001;
        }
    }

    public void launch(int targetX, int targetY, float speed) {
        double dx = targetX - this.x - (this.width / 2);
        double dy = targetY - this.y - (3 * (this.height / 4));
        double angle = Math.atan2(dy, dx);

        this.velX = (float) (Math.cos(angle) * speed);
        this.velY = (float) (Math.sin(angle) * speed);

        this.cueBall = false;
    }

    public Polygon getBounds() {
        return this.collision;
    }
}
