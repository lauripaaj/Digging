package fi.tuni.tiko.digging;

import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.Iterator;

public class ResourcePool extends Pool<ResourceTile> {


    // The following comment taken from https://www.gamedevelopment.blog/libgdx-object-pooling/
    // constructor with initial object count and max object count
    // max is the maximum of object held in the pool and not the
    // maximum amount of objects that can be created by the pool
    public ResourcePool (int init, int max) {
        super(init, max);

    }

    @Override
    protected ResourceTile newObject () {
        return new ResourceTile(0,0);
    }






}
