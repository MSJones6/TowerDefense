package de.msjones.tower_defense.levels;

import de.msjones.tower_defense.waypoints.Waypoint;
import lombok.Getter;

import java.util.List;

@Getter
public record Level(List<Waypoint> waypointList, List<TargetAppearance> targetAppearanceList) {
}
