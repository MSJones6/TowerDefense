package de.msjones.tower_defense.objects;

import de.msjones.tower_defense.waypoints.Waypoint;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class SimpleTower extends Tower {
    public SimpleTower(int x, int y) {
        this.position = new Waypoint(x, y);
        bufferedImage = getBufferedImage();
    }

    @Override
    public BufferedImage getBufferedImage() {
        BufferedImage buffer;
        BufferedImage bufferRotated  = null;

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("pics/tower_1.png");
            buffer = ImageIO.read(is);
            int w = buffer.getWidth();
            int h = buffer.getHeight();

            bufferRotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphic = bufferRotated.createGraphics();
            AffineTransform at = new AffineTransform();
            at.translate(w / 2d, h / 2d);
            graphic.rotate(Math.toRadians(angle), w / 2d, h / 2d);
            graphic.drawImage(buffer, null, 0, 0);
            graphic.dispose();
        } catch (IOException e) {
            log.error("Error while creating tower image: {}", e.getMessage(), e);

        }
        return bufferRotated;
    }
}
