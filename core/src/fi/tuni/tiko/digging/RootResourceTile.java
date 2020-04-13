package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class RootResourceTile extends GameTile implements Pool.Poolable {

    static GameTexture root_resource_tier1_Texture = new GameTexture(new Texture("resourceTile1.png"));
    static GameTexture root_resource_tier2_Texture = new GameTexture(new Texture("resourceTile2.png"));
    static GameTexture root_resource_tier3_Texture = new GameTexture(new Texture("resourceTile3.png"));
    static GameTexture root_resource_cared_for = new GameTexture(new Texture("rootResourcePlus.png"));

    public static final int ROOT_SCORE_TIER1=22;
    public static final int ROOT_SCORE_TIER2=32;
    public static final int ROOT_SCORE_TIER3=42;

    private int resourceScore;

    boolean available;



    public int getResourceScore () {
        return resourceScore;
    }

    public void setResourceScore (int resourceScore) {
        this.resourceScore = resourceScore;
    }

    public RootResourceTile(int locY, int locX) {

        super(locY, locX);

        makeThisTier(1);


        diggable=false;

        setConcrete(true);




        rectangle=new Rectangle(locX, locY, 1.00f, 1.00f);


    }

    public void takeCare() {
        available=false;
        setTexture(root_resource_cared_for);
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

        available=true;

        if (tier == 1) {
            setTexture(root_resource_tier1_Texture);
            resourceScore=ROOT_SCORE_TIER1;
        } else if (tier == 2) {
            setTexture(root_resource_tier2_Texture);
            resourceScore=ROOT_SCORE_TIER2;
        } else if (tier == 3) {
            setTexture(root_resource_tier3_Texture);
            resourceScore=ROOT_SCORE_TIER3;
        } else throw new IllegalArgumentException("Resource tier must be 1, 2 or 3");

    }

    @Override
    public void reset () {

        makeThisTier(1);
        available=true;

        setLocationY(-1);
        setLocationX(-1);
        diggable=false;
        setConcrete(true);

        //getVanishAnimation().resetAnimation();
        setVanishing(false);
        //rectangle out of sight too
        setX(-24);
        setY(-24);

        setOccupied(false);
        root=null;

    }
}
