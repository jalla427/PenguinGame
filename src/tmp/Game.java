package tmp;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable {

    private Thread thread;
    private boolean running = false;
    public static float deltaTime;

    //Main frame dimensions
    public static int sWidth = 1280;
    public static int sHeight = 720;

    //Sprites
    public static BufferedImage button_menu_200x120;

    //Rendering vars
    BufferStrategy bs;
    Graphics g;

    //Used for determining the current scene
    public enum STATE {
        Menu,
        Settings,
        Game
    }

    public static STATE gameState = STATE.Menu;

    public Game() {
        //Load assets
        BufferedImageLoader loader = new BufferedImageLoader();

        button_menu_200x120 = loader.loadImage("/button_menu_200x120.png");

        //Start game
        new Main("Penguin Bowling", this);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch(Exception e) {
            System.out.println("Application failed to stop properly");
        }
    }

    public void run() {
        int fps = 60;
        double nsPerTick = (double) 1000000000 / fps;
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(running){
            long now = System.nanoTime();
            long timeTaken = now - lastTime;
            lastTime = now;

            deltaTime = (float) (timeTaken / nsPerTick);

            tick();
            render();
            frames++;
        }
        stop();
    }

    private void tick() {
        if(gameState == STATE.Menu) {
            Menu.tick();
        }
    }

    private void render() {
        if(this.g == null) {
            //Initialize Rendering
            bs = this.getBufferStrategy();
            if(bs == null) {
                this.createBufferStrategy(3);
                return;
            }
        }
        g = bs.getDrawGraphics();

        //Background
        g.setColor(Color.black);
        g.fillRect(0, 0, sWidth, sHeight);

        Handler.renderHigherElements(g);
        Handler.renderMiddleElements(g);
        Handler.renderLowerElements(g);

        g.dispose();
        bs.show();
    }
}
