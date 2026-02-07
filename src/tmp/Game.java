package tmp;

import GameObjects.GoalZone;
import GameObjects.Penguin;
import GameObjects.StageChunk;
import MenuItems.ImageTextButton;

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
    public static BufferedImage main_banner_600x250;
    public static BufferedImage about_page;
    public static BufferedImage clear_label_600x250;
    public static BufferedImage fail_label_600x250;
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
    public static BufferedImage score_label_95x47;
    public static BufferedImage score_display_47x47;

    //Rendering vars
    BufferStrategy bs;
    Graphics g;

    //Menu vars
    protected Menu menu;
    private static float menuPenguinTimer = 150;

    //Score & Level vars
    public static int currentLevel = 1;
    public static int sequenceTarget = 1;
    public static int[] currentSequence = new int[] { 1 };
    public static int currentPar = 1;
    public static int strokes = 0;
    public static boolean passed = false;
    public static boolean levelComplete = false;
    public static boolean transitioning = false;

    //Gameplay vars
    public static float chargePower = 0;
    public static boolean charging = false;

    //Used for determining the current scene
    public enum STATE {
        Menu,
        About,
        Game
    }

    public static STATE gameState = STATE.Menu;

    public Game() {
        //Load assets
        BufferedImageLoader loader = new BufferedImageLoader();

        icebackground_1280x720 = loader.loadImage("/IceBackground_1280x720.png");
        main_banner_600x250 = loader.loadImage("/main_banner_600x250.png");
        about_page = loader.loadImage("/about_page.png");
        clear_label_600x250 = loader.loadImage("/clear_label_600x250.png");
        fail_label_600x250 = loader.loadImage("/fail_label_600x250.png");
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
        score_label_95x47 = loader.loadImage("/score_label_95x47.png");
        score_display_47x47 = loader.loadImage("/score_display_47x47.png");

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
        Handler.tick();

        if(gameState == STATE.Game) {
            if(levelComplete && !transitioning) {
                //Update highscore if needed
                if(passed && strokes < LevelCollection.getLevelBest(currentLevel)) {
                    LevelCollection.setLevelBest(currentLevel, strokes);
                }
                //Set to transitioning state
                transitioning = true;
                levelComplete = false;
                Handler.addButton(new ImageTextButton(Menu.mainButtonFont, Color.black, "CONTINUE", Game.button_menu_200x120, Game.button_menu_hover_200x120, (Game.sWidth / 2) - 100, (Game.sHeight / 2) + 70, 200, 120));
            }
            if(charging && !transitioning) {
                chargePower += 0.05 * deltaTime;
                chargePower = clamp(chargePower, 0, 10);
            }
        }

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
        Handler.goalZone = new GoalZone(Game.sWidth - 94, 243, 94, 227);

        currentPar = LevelCollection.getLevelPar(level);
        LevelCollection.setupLevel(currentLevel);
    }

    public static void resetBoard() {
        Handler.clearPenguins();
        Handler.removeCrosshair();
        Handler.clearButtons();

        charging = false;
        chargePower = 0;
        strokes = 0;
        passed = false;

        Handler.setNewCueBallPenguin();

        currentPar = LevelCollection.getLevelPar(currentLevel);
        LevelCollection.setupLevel(currentLevel);

        transitioning = false;
    }

    public static void clearGameElements() {
        Handler.clearPenguins();
        Handler.removeCrosshair();
        Handler.clearButtons();
        Handler.clearStageBoundaries();
        Handler.goalZone = null;

        charging = false;
        chargePower = 0;
        strokes = 0;
        sequenceTarget = 1;
        passed = false;

        transitioning = false;
    }

    public static void resetLevelVars() {
        currentLevel = 1;
        currentPar = 1;
        strokes = 0;
        currentSequence = new int[] { 1 };
        sequenceTarget = 1;
        levelComplete = false;
        passed = false;
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
