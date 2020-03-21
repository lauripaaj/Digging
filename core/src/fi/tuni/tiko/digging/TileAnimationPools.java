package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;

public class TileAnimationPools {

    private ResourceAnimationPool resourceAnimationPool;
    private ArrayList<ResourceGainedAnimation> resourceAnimationList;

    public ResourceAnimationPool getResourceAnimationPool () {
        return resourceAnimationPool;
    }

    public void setResourceAnimationPool (ResourceAnimationPool resourceAnimationPool) {
        this.resourceAnimationPool = resourceAnimationPool;

    }

    public ArrayList<ResourceGainedAnimation> getResourceAnimationList () {
        return resourceAnimationList;
    }

    public void setResourceAnimationList (ArrayList<ResourceGainedAnimation> resourceAnimationList) {
        this.resourceAnimationList = resourceAnimationList;
    }

    public TileAnimationPools (ResourceAnimationPool resourceAnimationPool, ArrayList<ResourceGainedAnimation> resourceAnimationList) {
        this.resourceAnimationPool=resourceAnimationPool;
        this.resourceAnimationList = resourceAnimationList;
    }

    public void checkResourceGainedAnimations(float delta) {

        Iterator<ResourceGainedAnimation> it = resourceAnimationList.iterator();
        while (it.hasNext()) {
            ResourceGainedAnimation aa = it.next();
            if (aa.isPlaying()) {
                aa.updateAnimation(delta);
            } else {
                resourceAnimationPool.free(aa);
                it.remove();
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (int i=0; i<resourceAnimationList.size(); i++) {
            resourceAnimationList.get(i).draw(batch);

        }
    }


}
