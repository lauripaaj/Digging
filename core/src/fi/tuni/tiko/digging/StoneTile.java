package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool.Poolable;


//StoneTiles differ from EdgeTiles in a way that StoneTiles will never be in edges. There might be some other differential too, not sure yet.
public class StoneTile extends PermanentTile implements Poolable {

    public StoneTile(int locY, int locX) {
        super(locY, locX);
    }


    public void vanish() {
        Gdx.app.log("asd", "Vanishing Stone tile? this is not supposed to happen");
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
        diggable=false;
        setConcrete(true);
        setTexture(permanentTexture);
        //getVanishAnimation().resetAnimation();
        //setVanishing(false);
        //rectangle out of sight too
        setX(-24);
        setY(-24);







    }

}
