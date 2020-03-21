package fi.tuni.tiko.digging;

import java.util.ArrayList;
import java.util.Iterator;

public class HazardPools {

    GoblinPool goblinPool;
    SpikePool spikePool;
    FallingTrapPool fallingTrapPool;

    public HazardPools(GoblinPool goblinPool, SpikePool spikePool, FallingTrapPool fallingTrapPool) {
        this.goblinPool = goblinPool;
        this.spikePool = spikePool;
        this.fallingTrapPool = fallingTrapPool;
    }

    public FallingTrapPool getFallingTrapPool () {
        return fallingTrapPool;
    }

    public void setFallingTrapPool (FallingTrapPool fallingTrapPool) {
        this.fallingTrapPool = fallingTrapPool;
    }

    public GoblinPool getGoblinPool () {
        return goblinPool;
    }

    public void setGoblinPool (GoblinPool goblinPool) {
        this.goblinPool = goblinPool;
    }

    public SpikePool getSpikePool () {
        return spikePool;
    }

    public void setSpikePool (SpikePool spikePool) {
        this.spikePool = spikePool;
    }

    public void putAllHazardsIntoPool(ArrayList<TileBasedObject> hazardList) {
        Iterator<TileBasedObject> it = hazardList.iterator();
        while (it.hasNext()) {
            TileBasedObject hazard = it.next();
            if (hazard instanceof Goblin) {
                goblinPool.free((Goblin)hazard);
                it.remove();
            } else if (hazard instanceof Spike) {
                spikePool.free((Spike)hazard);
                it.remove();
            } else if (hazard instanceof FallingTrap) {
                fallingTrapPool.free((FallingTrap)hazard);
                it.remove();
            }
        }
    }
}
