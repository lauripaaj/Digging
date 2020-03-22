package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;

public class DescendingDirtTile extends GameTile implements Poolable {

    static GameTexture descendingDirtTexture = new GameTexture(new Texture("descendingDirtTile.png"));
    static GameTexture descendingBrokenTexture = new GameTexture(new Texture ("descendingDirtTileBroken.png"));
    static Texture descendingDirtBeingDescended = new Texture("descendingDirtBeingDescended.png");

    //SheetAnimation fallingDirt = new SheetAnimation(descendingDirtBeingDescended, 1, 8, 8, 60);






    private float descendingSpeed =1.2f;

    private float maxDescendingTimeLeft=0.8f;
    private float descendingTimeLeft=maxDescendingTimeLeft;
    private boolean descending=false;

    public float getDescendingTimeLeft () {
        return descendingTimeLeft;
    }

    public void setDescendingTimeLeft (float descendingTimeLeft) {
        this.descendingTimeLeft = descendingTimeLeft;
    }

    public boolean isDescending () {
        return descending;
    }

    public void setDescending (boolean descending) {
        this.descending = descending;
    }

    public DescendingDirtTile(int locY, int locX) {



        super(locY, locX);

        //vissiin oikeasti positionjutut heitetään abstractin GameTilen constructorin kautta mutta en oo varma vielä

        diggable=true;


        setConcrete(true);

        //setLocationX(locX);
        //setLocationY(locY);

        setTexture(descendingDirtTexture);

        //hmm locX ja locY pitänee muuttaa tuosta en tiä vielä
        rectangle=new Rectangle(locX, locY, 1.00f, 1.00f);

        setVanishAnimation(new SheetAnimation(descendingDirtBeingDescended, 1, 8, 8, 60));

    }

   //@Override
    public void startVanishing(Stage currentStage) {
        super.startVanishing(currentStage);
        setTexture(descendingBrokenTexture);
        //continueVanishing(0.000001f);


        System.out.println("vanished sescending");
    }

    public void startDescending(float delta) {
        descending=true;

        System.out.println("started descending");
        //not the best way to implement this making it continue right away and needing delta as parameter here already
        continueDescending(delta);

    }
    public void continueDescending(float delta) {
        if (descendingTimeLeft > 0) {
            setDescendingTimeLeft(getDescendingTimeLeft()-delta*descendingSpeed);

        }
    }

    public void continueVanishing(float delta) {
        continueVanishAnimation(delta*4.0f);
        if (getVanishTimeLeft()<=0) {
            vanish();
        }
    }

    //this method will be used after the tile is taken from pool
    public void setInPlace(int locY, int locX) {
        setLocationX(locX);
        setLocationY(locY);

        setX(locX);
        setY(locY);

    }

    @Override
    public void reset () {
        setLocationY(-1);
        setLocationX(-1);
        diggable=true;
        setConcrete(true);
        setTexture(descendingDirtTexture);
        //getVanishAnimation().resetAnimation();
        setVanishing(false);
        //rectangle out of sight too
        setX(-24);
        setY(-24);
        descending=false;
        descendingTimeLeft=maxDescendingTimeLeft;
        setOccupied(false);
    }



    /*
    @Override
    public void draw(SpriteBatch batch) {

        if (!isDescending() || descendingTimeLeft <= 0) {
            batch.draw(getGameTextureRegion(), (float) getLocationX(), (float) getLocationY(), getWidth(), getHeight());
        } else {
            batch.draw(fallingDirt.getCurrentFrame(), (float) getLocationX(), (float) getLocationY(), getWidth(), getHeight());
        }

    }

     */

}
