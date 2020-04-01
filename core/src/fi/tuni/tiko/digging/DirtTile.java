package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import static fi.tuni.tiko.digging.MainGame.dirtTextureTileset;


//muiden kuin animaatioiden ja perustekstuurien flippaaminen ei oikein onnistu tässä 0.04v. eli näiden tilejen ei vielä näillä yrityksillä

public class DirtTile extends GameTile implements Poolable {








    //static GameTexture dirtTexture = new GameTexture(new Texture("tilesets/dirt/dirtTile-46.png"));;

    GameTexture dirtTexture = dirtTextureTileset[46];

    static GameTexture brokenTexture = new GameTexture(new Texture ("dirtTileBroken.png"));
    //static GameTexture dirtTexture ;
    //static GameTexture brokenTexture;

    static Texture dirtTileVanish = new Texture("dirtTileVanish.png");



    public DirtTile(int locY, int locX) {





        super(locY, locX);

        //vissiin oikeasti positionjutut heitetään abstractin GameTilen constructorin kautta mutta en oo varma vielä

        diggable=true;
        connectingTexture = true;
        tiling = true;

        //VAIN TESTIMIELESSÄ, EHDOTTOMASTI TÄMÄ MYÖHEMMIN TRUE
        setConcrete(true);

        //setLocationX(locX);
        //setLocationY(locY);

        setTexture(dirtTexture);

        //hmm locX ja locY pitänee muuttaa tuosta en tiä vielä
        rectangle=new Rectangle(locX, locY, 1.00f, 1.00f);

        setVanishAnimation(new SheetAnimation(dirtTileVanish, 1, 8, 5, 60));

    }

    @Override
    public void startVanishing (Stage currentStage) {
        super.startVanishing(currentStage);

        setTexture(brokenTexture);
        tiling = false;
        connectingTexture = false;

        updateTiles(locationY, locationX, currentStage);

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

        getVanishAnimation().resetAnimation();
        setVanishing(false);
        //rectangle out of sight too
        setX(-24);
        setY(-24);
        setOccupied(false);

        dirtTexture = dirtTextureTileset[46];
        setTexture(dirtTexture);
        connectingTexture = true;
        tiling = true;
        root = null;





    }

    @Override
    public void updateTexture(int tileNumber) {
        //dirtTexture = new GameTexture(new Texture("tilesets/dirt/dirtTile-"+tileNumber+".png"));
        dirtTexture=dirtTextureTileset[tileNumber];
        setTexture(dirtTexture);
    }
}
