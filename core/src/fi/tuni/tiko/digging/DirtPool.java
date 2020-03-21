package fi.tuni.tiko.digging;

import com.badlogic.gdx.utils.Pool;

public class DirtPool extends Pool<DirtTile> {

    // The following comment taken from https://www.gamedevelopment.blog/libgdx-object-pooling/
    // constructor with initial object count and max object count
    // max is the maximum of object held in the pool and not the
    // maximum amount of objects that can be created by the pool
    public DirtPool (int init, int max) {
        super(init, max);

    }

    @Override
    protected DirtTile newObject () {
        return new DirtTile(0,0);
    }
}
