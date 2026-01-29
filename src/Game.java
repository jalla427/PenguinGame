import java.awt.*;

public class Game extends Canvas implements Runnable {

    private Thread thread;
    private boolean running = false;
    public static float deltaTime;

    //Main frame dimensions
    public static int sWidth = 1280;
    public static int sHeight = 720;

    public Game() {
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

    }

    private void render() {

    }
}
