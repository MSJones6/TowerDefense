package de.msjones.tower_defense.levels;

import de.msjones.tower_defense.objects.Target;
import lombok.Data;

@Data
public class TargetAppearance {
    private Target target;
    private int delay;

    public TargetAppearance(Target target, int delay){
        this.target = target;
        this.delay = delay;
    }

}
