package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PermanentTile extends GameTile implements Poolable {

    static GameTexture permanentTexture = new GameTexture (new Texture("permanentTile.png"));

    public PermanentTile (int locY, int locX) {
        super(locY, locX);

        diggable=false;
        setConcrete(true);

        setTexture(permanentTexture);

        rectangle=new Rectangle(locX, locY, 1.00f, 1.00f);
    }

    public void vanish() {
        Gdx.app.log("asd", "Vanishing permanent tile? this is not supposed to happen");
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


