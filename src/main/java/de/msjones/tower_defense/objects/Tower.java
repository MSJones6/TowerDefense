package de.msjones.tower_defense.objects;

import de.msjones.tower_defense.waypoints.Waypoint;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class Tower {
    protected int angle = 0;

    @Setter
    protected Waypoint position;

    protected BufferedImage bufferedImage;

    protected int fireRate;

    private Target calculateNextTarget(List<Target> targetList) {
        Comparator<Target> comparator = Comparator.comparing(e -> Math.sqrt(Math.pow(position.getX() - e.getX(), 2) + Math.pow(position.getY() - e.getY(), 2)));
        return targetList
                .parallelStream()
                .min(comparator).orElse(null);
    }

    public void setAngleOnNearestTarget(List<Target> targetList) {
        Target target = calculateNextTarget(targetList);
        if (target != null) {
            double degrees = Math.toDegrees(Math.atan((target.getX() - position.getX()) / (target.getY() - position.getY())));
            double dx = target.getX() - position.getX();
            double dy = target.getY() - position.getY();

            if (dx >= 0 && dy >= 0) {
                degrees = 180 - degrees;
            } else if (dx >= 0 && dy < 0) {
                degrees = Math.abs(degrees);
            } else if (dx < 0 && dy >= 0) {
                degrees = Math.abs(degrees) + 180;
            } else if (dx < 0 && dy < 0) {
                degrees = -degrees;
            }

            angle = (int) degrees;
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(position, angle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tower tower = (Tower) o;
        return tower.getPosition().equals(this.getPosition()) && tower.getAngle() == this.angle;
    }
}
