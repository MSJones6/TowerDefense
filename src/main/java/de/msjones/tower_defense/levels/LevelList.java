package de.msjones.tower_defense.levels;

import de.msjones.tower_defense.objects.Target;
import de.msjones.tower_defense.waypoints.Waypoint;

import java.util.ArrayList;
import java.util.List;

public class LevelList {
    private LevelList(){
        // Do not allow to instantiate this class
    }
    public static List<Level> listOfLevels = List.of(
            initLevel1()
    );

    private static Level initLevel1() {
        List<Waypoint> waypointList = new ArrayList<>();
        waypointList.add(new Waypoint(0, 300));
        waypointList.add(new Waypoint(550, 300));
        waypointList.add(new Waypoint(550, 200));
        waypointList.add(new Waypoint(250, 200));
        waypointList.add(new Waypoint(250, 450));
        waypointList.add(new Waypoint(700, 450));
        waypointList.add(new Waypoint(700, 200));

        List<TargetAppearance> targetAppearanceList = new ArrayList<>();
        Target target = new Target(waypointList);
        target.setSpeed(3);
        for (int i = 0; i < 20; ++i) {
            targetAppearanceList.add(new TargetAppearance(new Target(target), 500));
        }

        return new Level(waypointList, targetAppearanceList);
    }

}
