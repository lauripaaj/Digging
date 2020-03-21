package fi.tuni.tiko.digging;

import com.badlogic.gdx.utils.Pool;

public class FallingTrapPool extends Pool<FallingTrap> {

    // The following comment taken from https://www.gamedevelopment.blog/libgdx-object-pooling/
    // constructor with initial object count and max object count
    // max is the maximum of object held in the pool and not the
    // maximum amount of objects that can be created by the pool
    public FallingTrapPool (int init, int max) {
        super(init, max);

    }
    @Override
    protected FallingTrap newObject () {
        return new FallingTrap(0,0);
    }
}
