package de.msjones.tower_defense;

import de.msjones.tower_defense.levels.LevelList;
import de.msjones.tower_defense.levels.TargetAppearance;
import de.msjones.tower_defense.objects.Target;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends JPanel implements ActionListener {
    private List<Target> targetList = new ArrayList<>();

    private BufferedImage buffer;
    //    private int[] waypointsX = {50, 150, 250, 350}; // X-Koordinaten der Wegpunkte
//    private int[] waypointsY = {50, 250, 100, 200}; // Y-Koordinaten der Wegpunkte
    private final int DELAY = 50; // Verzögerung für die Timer-Animation

    private final int TARGET_SIZE = 20;
    private Timer timer;

    private int level = 1;

    public Main() {
        BackgroundGenerator backgroundGenerator = new BackgroundGenerator();

        backgroundGenerator.generateMap(LevelList.levelList.get(level - 1).getWaypointList());

        this.setPreferredSize(new Dimension(800, 600));
        timer = new Timer(DELAY, this);
        timer.start();

        generateTargets(LevelList.levelList.get(level - 1).getTargetAppearanceList());
//        Target t = new Target(waypointList);
//        t.setSpeed(1);
//        targetList.add(t);

//        Target t = new Target(LevelList.levelList.get(level - 1));
//        t.setSpeed(3.1);
//        targetList.add(t);


//        t = new Target(waypointList);
//        t.setSpeed(.5);
//        targetList.add(t);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        try {
            File bg = new File("tower_defense_background.png");
            buffer = ImageIO.read(bg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Graphics2D g2d = buffer.createGraphics();
        g2d.drawImage(buffer, 0, 0, this);

        targetList.forEach(e -> {
            if (e.getSpeed() < 1) {
                g2d.setColor(Color.GREEN);
            } else if (e.getSpeed() == 1) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.BLACK);
            }
            g2d.fillOval((int) e.getX() - TARGET_SIZE / 2, (int) e.getY() - TARGET_SIZE / 2, TARGET_SIZE, TARGET_SIZE);
        });

        g.drawImage(buffer, 0, 0, null);
        g2d.dispose();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        targetList.forEach(Target::updatePosition);
        int oldTargetListSize = targetList.size();
        if (targetList.removeIf(Target::isFinished)) {
            int newTargetListSize = targetList.size();
            System.out.println((oldTargetListSize - newTargetListSize) + " Leben verloren");
        }
        repaint(); // Das Panel neu zeichnen, um die Bewegung anzuzeigen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Waypoint Movement");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.add(new Main());
            frame.setVisible(true);
            frame.pack();
        });
    }

    private void generateTargets(List<TargetAppearance> targetAppearanceList) {
        Runnable targetCreator = () -> {
            Iterator<TargetAppearance> iterator = targetAppearanceList.listIterator();
            while (iterator.hasNext()) {
                TargetAppearance targetAppearance = iterator.next();

                try {
                    Thread.sleep(targetAppearance.getDelay());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                targetList.add(targetAppearance.getTarget());
            }
        };
        new Thread(targetCreator).start();
    }
}
