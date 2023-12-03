package de.msjones.tower_defense;

import de.msjones.tower_defense.levels.LevelList;
import de.msjones.tower_defense.levels.TargetAppearance;
import de.msjones.tower_defense.objects.SimpleTower;
import de.msjones.tower_defense.objects.Target;
import de.msjones.tower_defense.objects.Tower;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class Main extends JPanel implements ActionListener, MouseListener {
    private final List<Target> targetList = new ArrayList<>();
    private final Set<Tower> towerList = new HashSet<>();

    private static final int DELAY = 50; // Verzögerung für die Timer-Animation

    private static final int TARGET_SIZE = 20;

    public Main() throws IOException {
        BackgroundGenerator backgroundGenerator = new BackgroundGenerator();

        int level = 1;
        backgroundGenerator.generateMap(LevelList.listOfLevels.get(level - 1).waypointList());

        this.setPreferredSize(new Dimension(800, 600));
        Timer timer = new Timer(DELAY, this);
        timer.start();

        this.addMouseListener(this);

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
            log.error("Error while loading background: {}", e.getMessage(), e);
            return;
        }

        Graphics2D g2d = buffer.createGraphics();
        g2d.drawImage(buffer, 0, 0, this);

        targetList.parallelStream().forEach(e -> {
            if (e.getSpeed() < 1) {
                g2d.setColor(Color.GREEN);
            } else if (e.getSpeed() == 1) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.BLACK);
            }
            g2d.fillOval((int) e.getX() - TARGET_SIZE / 2, (int) e.getY() - TARGET_SIZE / 2, TARGET_SIZE, TARGET_SIZE);
        });

        towerList.parallelStream().forEach(e -> {
            e.setAngleOnNearestTarget(targetList);
            g2d.drawImage(e.getBufferedImage(), (int) e.getPosition().getX(), (int) e.getPosition().getY(), null);
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

    @Override
    public void mouseClicked(MouseEvent e) {
        Tower t = new SimpleTower(e.getX(), e.getY());
        towerList.add(t);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Nothing to do at the moment
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Nothing to do at the moment
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Nothing to do at the moment
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Nothing to do at the moment
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
                log.error("Error while loading the frame: {}", e.getMessage(), e);
            }
        });
    }

    private void generateTargets(List<TargetAppearance> targetAppearanceList) {
        Runnable targetCreator = () -> {
            for (TargetAppearance targetAppearance : targetAppearanceList) {
                try {
                    Thread.sleep(targetAppearance.getDelay());
                } catch (InterruptedException e) {
                    log.error("Error while Thread.sleep: {}", e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }

                targetList.add(targetAppearance.getTarget());
            }
        };
        new Thread(targetCreator).start();
    }
}
