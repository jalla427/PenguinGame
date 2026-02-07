package MenuItems;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageTextDisplayButton extends ImageTextButton {

    public ImageTextDisplayButton(Font font, Color textColor, String name, String text, BufferedImage image, BufferedImage clickedImage, int x, int y, int width, int height) {
        super(font, textColor, name, image, clickedImage, x, y, width, height);
        this.text = text;
    }

    @Override
    public void render(Graphics g) {
        FontMetrics metrics = g.getFontMetrics(this.getFont());
        int textX = this.getX() + (this.getWidth() - metrics.stringWidth(this.getText())) / 2;
        int textY = this.getY() + ((this.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

        if(this.isClicked) {
            g.drawImage(this.clickedImage, this.getX(), this.getY(), null);
        }
        else { g.drawImage(this.image, this.getX(), this.getY(), null); }

        g.setColor(this.color);
        g.setFont(this.font);
        g.drawString(this.getText(), textX, textY);
    }
}
