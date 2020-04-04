package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;


public class Spike extends ImmobileHazard implements Poolable {

    boolean getsDestroyedByFallingPlayer = false;

    static GameTexture spikeTexture = new GameTexture(new Texture("spike.png"));
    static Texture spikeVanishingTexture = new Texture("SpikeVanishing.png");

    //strength related to other hazards: 2 will destroy strength 1 hazards without getting destroyed
    //they will get destroyed in contact with other strength 2:s (fallingTraps)
    int hazardStrength=2;



    public Spike(int tilePosY, int tilePosX) {
        setConcrete(true);
        setTexture(spikeTexture);

        setVanishAnimation(new SheetAnimation(spikeVanishingTexture, 1, 8, 5, 60));


        rectangle=new Rectangle(1.00f,1.00f, 0.8f, 1.00f);
        putInTilePos(tilePosY, tilePosX);

    }



    @Override
    public int getHazardStrength () {
        return hazardStrength;
    }

    @Override
    public boolean getGetsDestroyedByFallingPlayer () {
        return getsDestroyedByFallingPlayer;
    }

    @Override
    public void putInTilePos(int posY, int posX) {
        super.putInTilePos(posY, posX);
        setY( (1.27f-getHeight() )+posY);

    }

    @Override
    //spike needs to have getWidth() adjusted to 1.25f because it's 0.8f wide to help prevent bugs of colliding with player too easily
    //apparently it also needs to be adjusted in x-location a bit because of the changed size
    public void draw(SpriteBatch batch) {
        if (!isVanishing()) {
            batch.draw(gameTextureRegion, getX()-0.1f, getY(), getWidth() * 1.25f, getHeight());
        } else if (!isStatusDead()) {

            batch.draw(getVanishAnimation().getCurrentFrame(), getX()-0.1f, getY(), getWidth()*1.25f, getHeight());

        }
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
