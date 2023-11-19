package de.msjones.tower_defense.objects;

import de.msjones.tower_defense.levels.Level;
import de.msjones.tower_defense.waypoints.Waypoint;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Target {
    @Getter
    private double x;
    @Getter
    private double y;
    @Setter
    @Getter
    private double speed;
    private List<Waypoint> waypointList;
    private int currentWaypoint = 0;
    @Getter
    private boolean finished = false;

    public Target(List<Waypoint> waypointList) {
        this.waypointList = waypointList;
        this.x = waypointList.get(0).getX();
        this.y = waypointList.get(0).getY();
        this.speed = 5;
    }

    public Target(Target t) {
        this.waypointList = t.waypointList;
        this.x = waypointList.get(0).getX();
        this.y = waypointList.get(0).getY();
        this.speed = t.speed;
    }

    public void updatePosition() {
        if (currentWaypoint < waypointList.size()) {
            Waypoint targetWaypoint = waypointList.get(currentWaypoint);
            // Prüfen, ob das Objekt noch Wegpunkte zu bewegen hat
            double targetX = targetWaypoint.getX();
            double targetY = targetWaypoint.getY();

            double moveX = Math.min(speed, Math.abs(targetX - x));
            double moveY = Math.min(speed, Math.abs(targetY - y));

            // Objekt in Richtung des nächsten Wegpunkts bewegen
            if (x < targetX) {
                x += moveX;
            } else if (x > targetX) {
                x -= moveX;
            }

            if (y < targetY) {
                y += moveY;
            } else if (y > targetY) {
                y -= moveY;
            }

            if (x == targetX && y == targetY) {
                ++currentWaypoint;
            }
        } else {
            speed = 0;
            finished = true;
        }
    }
}
