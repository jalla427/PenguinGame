package tmp;

import GameObjects.Penguin;

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
    public static BufferedImage icebackground_1280x720;
    public static SpriteSheet penguin_sheet_79x79_106;
    public static BufferedImage button_menu_200x120;

    //Rendering vars
    BufferStrategy bs;
    Graphics g;

    //Menu vars
    private static float menuPenguinTimer = 0;

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

        icebackground_1280x720 = loader.loadImage("/IceBackground_1280x720.png");
        penguin_sheet_79x79_106 = new SpriteSheet(loader.loadImage("/penguin_sheet_79x79.png"), 4, 106, 79, 79);
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
            menuPenguinTimer += 1 * deltaTime;
            if(menuPenguinTimer >= 100) {
                int pengColor = 0;
                double pengColorRoll = Math.random();

                if(pengColorRoll >= 0.7) { pengColor++; }
                if(pengColorRoll >= 0.8) { pengColor++; }
                if(pengColorRoll >= 0.9) { pengColor++; }

                Handler.addPenguin(new Penguin((int) (Math.random() * sWidth), -79, pengColor));
                menuPenguinTimer = 0;
            }
        }

        Handler.tick();
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
        g.setColor(Color.white);
        g.fillRect(0, 0, sWidth, sHeight);
        g.drawImage(icebackground_1280x720, 0, 0, null);

        Handler.renderLowerElements(g);
        Handler.renderMiddleElements(g);
        Handler.renderHigherElements(g);

        g.dispose();
        bs.show();
    }
}
