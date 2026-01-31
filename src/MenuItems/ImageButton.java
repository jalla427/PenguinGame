package MenuItems;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageButton extends Button {
    private BufferedImage image;
    private BufferedImage clickedImage;

    public ImageButton(String name, BufferedImage image, BufferedImage clickedImage, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        this.image = image;
        this.clickedImage = clickedImage;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        if(isClicked) {
            g.drawImage(clickedImage, this.getX(), this.getY(), null);
        }
        else { g.drawImage(image, this.getX(), this.getY(), null); }
    }

    public BufferedImage getImage() { return image; }
    public void setImage(BufferedImage image) { this.image = image; }
}