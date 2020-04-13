package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class EntranceTile extends GameTile {

    static GameTexture entrance = new GameTexture(new Texture("entrance.png"));

    public EntranceTile(int posY, int posX) {


        super(posY, posX);
        connectingTexture=true;
        diggable=false;
        setConcrete(false);

        setTexture(entrance);
        rectangle=new Rectangle(posX, posY, 1.00f, 1.00f);

    }

    public void setInPlace(int locY, int locX) {
        setLocationX(locX);
        setLocationY(locY);

        setX(locX);
        setY(locY);

    }
}
