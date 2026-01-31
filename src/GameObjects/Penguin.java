package GameObjects;

import tmp.Game;
import tmp.Handler;
import tmp.ID;

import java.awt.*;

public class Penguin extends GameObject {

    public int color;
    public int animationFrame;
    public float animationTimer = 0;

    public Penguin(float x, float y, int color) {
        super(x, y, 65, 65, ID.Penguin);
        this.color = color; //0 = normal, 1 = blue, 2 = red, 3 = green
        this.animationFrame = 1;
    }

    public void tick() {
        if(Game.gameState == Game.STATE.Menu) {
            this.y += 1 * Game.deltaTime;
            if(this.y > Game.sHeight + 7) {
                Handler.removePenguin(this);
            }
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
    }

    public Polygon getBounds() {
        return null;
    }
}
