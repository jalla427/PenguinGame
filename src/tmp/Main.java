package tmp;

import javax.swing.*;
import java.awt.*;

public class Main extends Canvas {

    public Main(String title, Game gamePlay) {
        //Import icon image
        ImageIcon iconImg = new ImageIcon("./res/icon.png");

        //Instantiate Outer Container
        JFrame jfrGameCore = new JFrame(title);

        setJFrameAttributes(jfrGameCore, iconImg);

        //Prepare inner game frame
        gamePlay.setSize(Game.sWidth, Game.sHeight);
        jfrGameCore.add(gamePlay);
        jfrGameCore.pack();
        gamePlay.start();
    }

    private void setJFrameAttributes(JFrame frameMain, ImageIcon iconImg) {
        //Sets attributes for main container
        frameMain.setLocation(200, 50);
        frameMain.setResizable(false);
        frameMain.setVisible(true);
        frameMain.setIconImage(iconImg.getImage());
        frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Game();
    }
}