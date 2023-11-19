package de.msjones.tower_defense.levels;

import de.msjones.tower_defense.waypoints.Waypoint;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Level {
    @Getter
    private List<Waypoint> waypointList;
    @Getter
    private List<TargetAppearance> targetAppearanceList;

    public Level(List<Waypoint> waypointList, List<TargetAppearance> targetAppearanceList) {
        this.waypointList = waypointList;
        this.targetAppearanceList = targetAppearanceList;
    }
}
