package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;


public class Spike extends ImmobileHazard implements Poolable {

    boolean getsDestroyedByFallingPlayer = false;

    static GameTexture spikeTexture = new GameTexture(new Texture("spike.png"));



    public Spike(int tilePosY, int tilePosX) {
        setConcrete(true);
        setTexture(spikeTexture);

        setVanishAnimation(new SheetAnimation(new Texture("SpikeVanishing.png"), 1, 8, 5, 60));


        rectangle=new Rectangle(1.00f,1.00f, 0.8f, 1.00f);
        putInTilePos(tilePosY, tilePosX);

    }





    @Override
    public boolean getGetsDestroyedByFallingPlayer () {
        return getsDestroyedByFallingPlayer;
    }





    @Override
    public void reset () {
        setConcrete(true);
        setVanishing(false);
        setTexture(spikeTexture);
        getVanishAnimation().resetAnimation();
        setStatusDead(false);

    }
}
