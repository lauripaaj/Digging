package fi.tuni.tiko.digging;

import com.badlogic.gdx.utils.Pool;

public class RootResourcePool extends Pool<RootResourceTile> {
    // The following comment taken from https://www.gamedevelopment.blog/libgdx-object-pooling/
    // constructor with initial object count and max object count
    // max is the maximum of object held in the pool and not the
    // maximum amount of objects that can be created by the pool
    public RootResourcePool (int init, int max) {
        super(init, max);

    }

    @Override
    protected RootResourceTile newObject () {
        return new RootResourceTile(0,0);
    }
}


