package de.msjones.tower_defense;

import de.msjones.tower_defense.waypoints.Waypoint;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BackgroundGenerator {
    private static int WIDTH = 800;
    private static int HEIGHT = 600;
    private static int BORDER = 50;

    public List<Waypoint> generateWaypointsAndMap(int waypointCount) {
        BackgroundGenerator bg = new BackgroundGenerator();
        List<Waypoint> waypointList = bg.generateWaypoints(waypointCount);
        bg.generateMap(waypointList);
        return waypointList;
    }

    public List<Waypoint> generateWaypoints(int waypointCount) {
        List<Waypoint> waypointList = new ArrayList<>();
        waypointList.add(getFirstPoint());
        double direction = waypointList.get(waypointList.size() - 1).getX() == 0 ? 3 : 6;


        double lastRealDirection = direction;
        Waypoint lastWaypoint = null;
        for (int i = 0; i < waypointCount; ++i) {
            Waypoint source = waypointList.get(waypointList.size() - 1);
            Waypoint destination = getNextPoint(source, direction);

            double nextDirection = getNextDirection(direction);
            if (Math.abs(nextDirection - lastRealDirection) == 6) {
                destination = null;
            } else {
                direction = nextDirection;
            }
            if (destination == null) {
                --i;
                continue;
            }
            lastRealDirection = direction;

            waypointList.add(destination);
            lastWaypoint = destination;
        }

        return waypointList;
    }

    public void generateMap(List<Waypoint> waypointList) {
        BufferedImage background = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = background.createGraphics();

        // Zeichne den Hintergrund
        g2d.setColor(Color.GREEN.darker()); // Beispiel für eine grüne Landschaft
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Zeichne Pfade oder Wege
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(30, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int i = 1; i < waypointList.size(); ++i) {
            Waypoint source = waypointList.get(i - 1);
            Waypoint destination = waypointList.get(i);
            g2d.drawLine((int) source.getX(), (int) source.getY(), (int) destination.getX(), (int) destination.getY());
        }

        // Zeichne Schloss
        try {
            Waypoint lastWaypoint = waypointList.get(waypointList.size() - 1);
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("pics/castle2.png");
            BufferedImage castle = ImageIO.read(is);
            System.out.println("castle.getHeight() = " + castle.getHeight());
            System.out.println("castle.getWidth() = " + castle.getWidth());
            g2d.drawImage(castle, (int) lastWaypoint.getX() - 23, (int) lastWaypoint.getY() - 58, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        g2d.dispose();

        savePicture(background, "");
    }


    private Waypoint getFirstPoint() {
        int x;
        int y;

        Random r = new Random();
        int start = r.nextInt(2);
        if (start == 0) {
            x = 0;
            y = r.nextInt(HEIGHT / 50) * 50 + 50;
        } else {
            x = r.nextInt(WIDTH / 50) * 50 + 50;
            y = 0;
        }

        return new Waypoint(x, y);
    }

    private Waypoint getNextPoint(Waypoint lastPoint, double direction) {
        Random r = new Random();
        int length = r.nextInt(10) + 1;

        double x = lastPoint.getX();
        double y = lastPoint.getY();

        if (direction > 0 && direction < 6) {
            length = Math.min(length, (WIDTH - BORDER - (int) x) / 50);
        } else if (direction > 6 && direction < 12) {
            length = Math.min(length, (int) x / 50 - 1);
        }

        if ((direction >= 0 && direction < 3) || (direction > 9 && direction <= 12)) {
            length = Math.min(length, (int) y / 50 - 1);
        } else if (direction > 3 && direction < 9) {
            length = Math.min(length, (HEIGHT - BORDER - (int) y) / 50);
        }


        if (direction > 0 && direction < 6) {
            x += length * 50;
        } else if (direction > 6 && direction < 12) {
            x -= length * 50;
        }

        if ((direction >= 0 && direction < 3) || (direction > 9 && direction <= 12)) {
            y -= length * 50;
        } else if (direction > 3 && direction < 9) {
            y += length * 50;
        }

        if (length == 0) {
            return null;
        }

        System.out.println("Length: " + length + "; dir: " + direction + "; old: " + lastPoint.getX() + "/" + lastPoint.getY() + "; new: " + x + "/" + y);

        return new Waypoint(x, y);
    }

    private double getNextDirection(double direction) {
        Random r = new Random();
        return (r.nextInt(5) * 1.5 + direction - 3 + 24) % 12;
    }

    private void savePicture(BufferedImage background, String praefix) {
        // Speichere das generierte Bild
        try {
            File output = new File("tower_defense_background" + praefix + ".png");
            ImageIO.write(background, "png", output);
            System.out.println("Bild wurde erfolgreich erstellt: " + output.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}