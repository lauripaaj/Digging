package fi.tuni.tiko.digging;

import com.badlogic.gdx.utils.Pool;

public class DescendingPool extends Pool<DescendingDirtTile> {

    // The following comment taken from https://www.gamedevelopment.blog/libgdx-object-pooling/
    // constructor with initial object count and max object count
    // max is the maximum of object held in the pool and not the
    // maximum amount of objects that can be created by the pool
    public DescendingPool (int init, int max) {
        super(init, max);

    }

    @Override
    protected DescendingDirtTile newObject () {
        return new DescendingDirtTile(0,0);
    }
}