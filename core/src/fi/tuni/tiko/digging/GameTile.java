package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameTile extends TileBasedObject {


    boolean diggable;
    //boolean concrete;
    boolean connectingTexture = false;
    boolean tiling = false;

    /* boolean occupied determines
    is there a hazard already currently in this tile. 2 Hazards shouldn't be able to be in the same tile
    at the same time. What this means is that hazards who are walking should stop walking when coming
    next to an occupied tile, and
    - if goblin(working title) gets hit by Falling Hazard, or it falls
    into spike hazard, the goblin will get annihilated.
    - if falling trap falls on spike trap, both of them will be removed from the game
     */
    private boolean occupied = false;

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


    public boolean isOccupied () {
        return occupied;
    }

    public void setOccupied (boolean occupied) {
        this.occupied = occupied;
    }

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
    @Override
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

    public void unoccupyCurrentTile(Stage currentStage) {

    }

    public void startVanishing (Stage currentStage) {

        unoccupyCurrentTile(currentStage);

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


    // Calls the tile to update texture based on nearby tiles
    public void updateTile(Stage currentStage) {
        boolean top = currentStage.hasConnectingTexture(locationY -1, locationX);
        boolean bot = currentStage.hasConnectingTexture(locationY +1, locationX);
        boolean left = currentStage.hasConnectingTexture(locationY, locationX -1);
        boolean right = currentStage.hasConnectingTexture(locationY, locationX +1);

        boolean topRight = currentStage.hasConnectingTexture(locationY -1, locationX +1) && top && right;
        boolean botRight = currentStage.hasConnectingTexture(locationY +1, locationX +1) && bot && right;
        boolean topLeft = currentStage.hasConnectingTexture(locationY -1, locationX -1) && top && left;
        boolean botLeft = currentStage.hasConnectingTexture(locationY +1, locationX -1) && bot && left;

        int total = (topLeft ? 1 : 0) + (top ? 2 : 0) + (topRight ? 4 : 0) + (left ? 8 : 0) + (right ? 16 : 0) + (botLeft ? 32 : 0) + (bot ? 64 : 0) + (botRight ? 128 : 0);

        int updateTo;

        switch (total) {
            case 2: updateTo = 1; break;
            case 8: updateTo = 2; break;
            case 10: updateTo = 3; break;
            case 11: updateTo = 4; break;
            case 16: updateTo = 5; break;
            case 18: updateTo = 6; break;
            case 22: updateTo = 7; break;
            case 24: updateTo = 8; break;
            case 26: updateTo = 9; break;
            case 27: updateTo = 10; break;
            case 30: updateTo = 11; break;
            case 31: updateTo = 12; break;
            case 64: updateTo = 13; break;
            case 66: updateTo = 14; break;
            case 72: updateTo = 15; break;
            case 74: updateTo = 16; break;
            case 75: updateTo = 17; break;
            case 80: updateTo = 18; break;
            case 82: updateTo = 19; break;
            case 86: updateTo = 20; break;
            case 88: updateTo = 21; break;
            case 90: updateTo = 22; break;
            case 91: updateTo = 23; break;
            case 94: updateTo = 24; break;
            case 95: updateTo = 25; break;
            case 104: updateTo = 26; break;
            case 106: updateTo = 27; break;
            case 107: updateTo = 28; break;
            case 120: updateTo = 29; break;
            case 122: updateTo = 30; break;
            case 123: updateTo = 31; break;
            case 126: updateTo = 32; break;
            case 127: updateTo = 33; break;
            case 208: updateTo = 34; break;
            case 210: updateTo = 35; break;
            case 214: updateTo = 36; break;
            case 216: updateTo = 37; break;
            case 218: updateTo = 38; break;
            case 219: updateTo = 39; break;
            case 222: updateTo = 40; break;
            case 223: updateTo = 41; break;
            case 248: updateTo = 42; break;
            case 250: updateTo = 43; break;
            case 251: updateTo = 44; break;
            case 254: updateTo = 45; break;
            case 255: updateTo = 46; break;
            default: updateTo = 47;
        }
        updateTexture(updateTo);
        System.out.println("called tile update");
    }
    public void updateTiles(int fromY, int fromX, Stage currentStage) {
        for (int i = -2; i < 2; i++) {
            for (int k = -2; k < 2; k++) {
                try {
                    if (currentStage.tiles[fromY + i][fromX + k].tiling) {
                        currentStage.tiles[fromY + i][fromX + k].updateTile(currentStage);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {

                }

            }
        }
        System.out.println("should update tiles");
    }
    public void updateTexture(int tileNumber) {System.out.println("failed tile update");}


}