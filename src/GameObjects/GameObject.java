package GameObjects;

import tmp.ID;

import java.awt.*;

public abstract class GameObject {
    protected ID id;
    protected float x, y;
    protected float velX, velY;
    protected int width, height;

    public GameObject(float x, float y, int width, int height, ID id) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Polygon getBounds();

    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setID(ID id) {
        this.id = id;
    }
    public void setVelX(float velX) {
        this.velX = velX;
    }
    public void setVelY(float velY) {
        this.velY = velY;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public ID getID() {
        return id;
    }
    public float getVelX() {
        return velX;
    }
    public float getVelY() {
        return velY;
    }
}
