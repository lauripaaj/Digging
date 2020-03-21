package fi.tuni.tiko.digging;

import com.badlogic.gdx.utils.Pool;

public class StonePool extends Pool<StoneTile> {
    // The following comment taken from https://www.gamedevelopment.blog/libgdx-object-pooling/
    // constructor with initial object count and max object count
    // max is the maximum of object held in the pool and not the
    // maximum amount of objects that can be created by the pool
    public StonePool (int init, int max) {
        super(init, max);

    }

    @Override
    protected StoneTile newObject () {
        return new StoneTile(0,0);
    }
}
