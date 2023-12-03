package de.msjones.tower_defense.levels;

import de.msjones.tower_defense.waypoints.Waypoint;

import java.util.List;


public record Level(List<Waypoint> waypointList, List<TargetAppearance> targetAppearanceList) {
}
