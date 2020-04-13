package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;

import static fi.tuni.tiko.digging.DirtTile.brokenTexture;

public class ResourceTile extends GameTile implements Poolable {

    static GameTexture resource_tier1_Texture = new GameTexture(new Texture("resourceTile1.png"));
    static GameTexture resource_tier2_Texture = new GameTexture(new Texture("resourceTile2.png"));
    static GameTexture resource_tier3_Texture = new GameTexture(new Texture("resourceTile3.png"));

    static Texture resourceTileVanish = new Texture("dirtTileVanish.png");

    public static final int RESOURCE_SCORE_TIER1=15;
    public static final int RESOURCE_SCORE_TIER2=28;
    public static final int RESOURCE_SCORE_TIER3=40;

    private int resourceScore;



    public int getResourceScore () {
        return resourceScore;
    }

    public void setResourceScore (int resourceScore) {
        this.resourceScore = resourceScore;
    }



    public ResourceTile(int locY, int locX) {

        super(locY, locX);

        makeThisTier(1);


        diggable=true;

        setConcrete(true);




        rectangle=new Rectangle(locX, locY, 1.00f, 1.00f);

        setVanishAnimation(new SheetAnimation(resourceTileVanish, 1, 8, 5, 60));
    }

    @Override
    public void startVanishing (Stage currentStage) {
        super.startVanishing(currentStage);

        setTexture(brokenTexture);

    }

    //this method will be used after the tile is taken from pool
    public void setInPlace(int locY, int locX) {
        setLocationX(locX);
        setLocationY(locY);

        setX(locX);
        setY(locY);

    }

    //this method will also be used after the tile is taken from pool
    public void makeThisTier(int tier) {

        if (tier == 1) {
            setTexture(resource_tier1_Texture);
            resourceScore=RESOURCE_SCORE_TIER1;
        } else if (tier == 2) {
            setTexture(resource_tier2_Texture);
            resourceScore=RESOURCE_SCORE_TIER2;
        } else if (tier == 3) {
            setTexture(resource_tier3_Texture);
            resourceScore=RESOURCE_SCORE_TIER3;
        } else throw new IllegalArgumentException("Resource tier must be 1, 2 or 3");

    }




    @Override
    public void reset () {

        makeThisTier(1);

        setLocationY(-1);
        setLocationX(-1);
        diggable=true;
        setConcrete(true);

        getVanishAnimation().resetAnimation();
        setVanishing(false);
        //rectangle out of sight too
        setX(-24);
        setY(-24);

        setOccupied(false);
        root=null;

    }

}
