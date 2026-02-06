package tmp;

import GameObjects.Penguin;
import GameObjects.StageChunk;

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
    public static BufferedImage stage_boundary_1280x720;
    public static BufferedImage stage_bottom_1280x106;
    public static SpriteSheet penguin_sheet_79x79_105;
    public static BufferedImage crosshair_25x25;
    public static BufferedImage button_menu_200x120;
    public static BufferedImage button_menu_hover_200x120;
    public static BufferedImage button_retry_95x47;
    public static BufferedImage button_retry_pressed_95x47;
    public static BufferedImage button_menu_95x47;
    public static BufferedImage button_menu_pressed_95x47;
    public static BufferedImage overlay_launch_191x47;
    public static BufferedImage overlay_launch_label_191x47;

    //Rendering vars
    BufferStrategy bs;
    Graphics g;

    //Menu vars
    protected Menu menu;
    private static float menuPenguinTimer = 150;

    //Gameplay vars
    public static float chargePower = 0;
    public static boolean charging = false;

    //Used for determining the current scene
    public enum STATE {
        Menu,
        Controls,
        Game
    }

    public static STATE gameState = STATE.Menu;

    public Game() {
        //Load assets
        BufferedImageLoader loader = new BufferedImageLoader();

        icebackground_1280x720 = loader.loadImage("/IceBackground_1280x720.png");
        stage_boundary_1280x720 = loader.loadImage("/stage_boundary_1280x720.png");
        stage_bottom_1280x106 = loader.loadImage("/stage_bottom_1280x106.png");
        penguin_sheet_79x79_105 = new SpriteSheet(loader.loadImage("/penguin_sheet_79x79.png"), 4, 105, 79, 79);
        crosshair_25x25 = loader.loadImage("/crosshair_25x25.png");
        button_menu_200x120 = loader.loadImage("/button_menu_200x120.png");
        button_menu_hover_200x120 = loader.loadImage("/button_menu_hover_200x120.png");
        button_retry_95x47 = loader.loadImage("/button_retry_95x47.png");
        button_retry_pressed_95x47 = loader.loadImage("/button_retry_pressed_95x47.png");
        button_menu_95x47 = loader.loadImage("/button_menu_95x47.png");
        button_menu_pressed_95x47 = loader.loadImage("/button_menu_pressed_95x47.png");
        overlay_launch_191x47 = loader.loadImage("/overlay_launch_191x47.png");
        overlay_launch_label_191x47 = loader.loadImage("/overlay_launch_label_191x47.png");

        //Create core objects
        menu = new Menu();
        this.addMouseListener(menu);

        //Start game
        new Main("Penguin Pool", this);
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
        menu.tick();
        if(gameState == STATE.Menu) {
            menuPenguinTimer += 1 * deltaTime;
            if(menuPenguinTimer >= 200) {
                int pengColor = 0;
                double pengColorRoll = Math.random();

                if(pengColorRoll >= 0.7) { pengColor++; }
                if(pengColorRoll >= 0.8) { pengColor++; }
                if(pengColorRoll >= 0.9) { pengColor++; }

                Handler.addPenguin(new Penguin((int) (Math.random() * sWidth), -79, pengColor, true));
                menuPenguinTimer = 0;
            }
        }

        if(gameState == STATE.Game) {
            if(charging) {
                chargePower += 0.05 * deltaTime;
                chargePower = clamp(chargePower, 0, 10);
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

    public static void beginGame(int level) {
        Handler.stageList.add(new StageChunk(0, 0, 1280, 100));
        Handler.stageList.add(new StageChunk(0, sHeight - 82, 1280, 82));
        Handler.stageList.add(new StageChunk(1186, 100, 94, 143));
        Handler.stageList.add(new StageChunk(1186, 470, 94, 471));
        Handler.stageList.add(new StageChunk(-100, 0, 100, 1280));
        Handler.findTotalLevelArea();

        Handler.setNewCueBallPenguin();
    }

    public static void resetBoard() {
        Handler.clearPenguins();
        Handler.removeCrosshair();
        Handler.clearButtons();

        charging = false;
        chargePower = 0;

        Handler.setNewCueBallPenguin();
    }

    public static void clearGameElements() {
        Handler.clearPenguins();
        Handler.removeCrosshair();
        Handler.clearButtons();
        Handler.clearStageBoundaries();

        charging = false;
        chargePower = 0;
    }

    public static boolean isPointInBounds(int mx, int my, int x, int y, int width, int height) {
        return mx > x && mx < x + width && my > y && my < y + height;
    }

    public static float clamp(float value, float min, float max) {
        if(value > max) {
            return max;
        } else if(value < min) {
            return min;
        } else return value;
    }

    public static int clamp(int value, int min, int max) {
        if(value > max) {
            return max;
        } else if(value < min) {
            return min;
        } else return value;
    }
}
