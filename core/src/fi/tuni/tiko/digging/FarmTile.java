package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;

public class FarmTile extends GameTile implements Poolable {

    static GameTexture farmTexture = new GameTexture(new Texture("farmTile.png"));




    public FarmTile(int locY, int locX) {



        super(locY, locX);

        //vissiin oikeasti positionjutut heitetään abstractin GameTilen constructorin kautta mutta en oo varma vielä

        diggable=false;


        setConcrete(false);

        //setLocationX(locX);
        //setLocationY(locY);

        setTexture(farmTexture);

        //hmm locX ja locY pitänee muuttaa tuosta en tiä vielä
        rectangle=new Rectangle(locX, locY, 1.00f, 1.00f);

    }
    /*
    public void draw(SpriteBatch batch) {
        batch.draw(farmTexture, getX(), getY(), getWidth(), getHeight());
    }*/

    @Override
    public void vanish () {

    }
    //this method will be used after the tile is taken from pool
    public void setInPlace(int locY, int locX) {
        setLocationX(locX);
        setLocationY(locY);

        setX(locX);
        setY(locY);

    }

    //pitää miettiä pitääkö tuohon myöhemmin lisätä muuta
    @Override
    public void reset () {
        setTexture(farmTexture);

        setOccupied(false);
        root=null;
    }
}