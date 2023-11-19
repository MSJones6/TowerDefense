package de.msjones.tower_defense;

import de.msjones.tower_defense.levels.LevelList;
import de.msjones.tower_defense.levels.TargetAppearance;
import de.msjones.tower_defense.objects.Target;
import lombok.extern.slf4j.Slf4j;

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
import java.util.List;

@Slf4j
public class Main extends JPanel implements ActionListener {
    private final List<Target> targetList = new ArrayList<>();

    private static final int DELAY = 50; // Verzögerung für die Timer-Animation

    private static final int TARGET_SIZE = 20;

    public Main() throws IOException {
        BackgroundGenerator backgroundGenerator = new BackgroundGenerator();

        int level = 1;
        backgroundGenerator.generateMap(LevelList.listOfLevels.get(level - 1).waypointList());

        this.setPreferredSize(new Dimension(800, 600));
        Timer timer = new Timer(DELAY, this);
        timer.start();

        generateTargets(LevelList.listOfLevels.get(level - 1).targetAppearanceList());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        BufferedImage buffer;
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
            log.info((oldTargetListSize - newTargetListSize) + " Leben verloren");
        }
        repaint(); // Das Panel neu zeichnen, um die Bewegung anzuzeigen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                JFrame frame = new JFrame("Waypoint Movement");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.add(new Main());
                frame.setVisible(true);
                frame.pack();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void generateTargets(List<TargetAppearance> targetAppearanceList) {
        Runnable targetCreator = () -> {
            for (TargetAppearance targetAppearance : targetAppearanceList) {
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
