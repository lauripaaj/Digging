package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameTile extends TileBasedObject {


    boolean diggable;
    //boolean concrete;

    /*
    Location, coordinates of the tile in "map"(GameTile[][] tiles). first tile in top left will have
    LocationY 0, locatoinX 0, the next to the right of it will be locationY 0, locationX 1 etc.
     */
    int locationY;
    int locationX;

    //public boolean isConcrete() {
    //    return concrete;
    //}

    //public void setConcrete(boolean concrete) {
    //    this.concrete = concrete;
    //}

    public int getLocationY () {
        return locationY;
    }

    public int getLocationX () {
        return locationX;
    }

    public void setLocationY (int locationY) {
        this.locationY = locationY;
    }

    public void setLocationX (int locationX) {
        this.locationX = locationX;
    }

    public boolean isDiggable () {
        return diggable;
    }

    public void setDiggable (boolean diggable) {
        this.diggable = diggable;
    }

    public GameTile (int locY, int locX) {
        setLocationX(locX);
        setLocationY(locY);
    }



    //public GameTile() {
    //}

    public void draw (SpriteBatch batch) {




        //if (!isVanishing() || getVanishTimeLeft() <= 0) {
        if (!isVanishing() || getVanishTimeLeft() <= 0) {
            batch.draw(getGameTextureRegion(), (float) getLocationX(), (float) getLocationY(), getWidth(), getHeight());
        } else {

            batch.draw(getVanishAnimation().getCurrentFrame(), (float) getLocationX(), (float) getLocationY(), getWidth(), getHeight());

        }

    }

    //tyhmä nimi täytyy muuttaa tämän toimintaa
    public void normalizeSize () {

    }

    public void vanish() {
        System.out.println("vanished");
        setConcrete(false);


    }


    public void startVanishing (Stage currentStage) {
        setVanishing(true);
        //setConcrete(false);
        setVanishTimeLeft(0.3f);
        currentStage.vanishingTileList.add(this);
        //getVanishAnimation().createAnimation();
        //for some reason this doesn't work without starting it already here. probably vanishTimeLeft should be put in constructor to prevent this or change checkVanishingTiles()method
        continueVanishing(0.0001f);
    }

    public void continueVanishing(float delta) {

        setVanishTimeLeft(getVanishTimeLeft()-delta*0.5f);
        getVanishAnimation().continueAnimationOnce(delta*1.2f);

        if (getVanishTimeLeft() <= 0) {
            vanish();
        }

    }


}