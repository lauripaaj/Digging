package fi.tuni.tiko.digging;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool.Poolable;

/*Root is attribute in GameTiles, when its null, there is no root there

 */

//REMEMBER TO CHECK TILEPOOLS PUT ALL TILES BACK TO METHOD, ROOT THINGS MUST BE IN THERE TOO
public class Root extends GameObject implements Poolable {

    public static final int CONTINUES = 0;
    public static final int CLOSES = 1;
    //place where player can treat the plant
    public static final int CLOSES_PLUS = 2;

    public static final boolean ROOT_LEFT = false;
    public static final boolean ROOT_RIGHT = true;


    private int status;
    private boolean direction;

    static GameTexture rootTexture = new GameTexture(new Texture("rootContinous.png"));
    static GameTexture rootTextureClosed = new GameTexture(new Texture("rootClosing.png"));
    static GameTexture rootTexturePlus = new GameTexture(new Texture("rootClosingPlus.png"));

    static GameTexture rootTextureFlipped = new GameTexture(new Texture("rootContinous.png"), true);
    static GameTexture rootTextureClosedFlipped = new GameTexture(new Texture("rootClosing.png"), true);
    static GameTexture rootTexturePlusFlipped = new GameTexture(new Texture("rootClosingPlus.png"), true);






    public Root() {

        setConcrete(false);
        setTexture(rootTexture);
        direction = ROOT_RIGHT;
    }


    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
        if (status == CONTINUES) {
            setTexture(rootTexture);
        } else if (status == CLOSES) {
            setTexture(rootTextureClosed);
        } else {
            setTexture(rootTexturePlus);
        }
    }



    public void updateTexture() {
        if (direction == ROOT_LEFT) {

            if (status==CONTINUES) {
                setTexture(rootTexture);
            } else if (status == CLOSES) {
                setTexture(rootTextureClosed);
            } else {
                setTexture(rootTexturePlus);
            }
        } else {
            if (status==CONTINUES) {
                setTexture(rootTextureFlipped);
            } else if (status == CLOSES) {
                setTexture(rootTextureClosedFlipped);
            } else {
                setTexture(rootTexturePlusFlipped);
            }
        }
    }

    public void flipDirection() {

        if (direction == ROOT_LEFT) {
            direction = ROOT_RIGHT;


        } else {
            direction = ROOT_LEFT;

        }
        updateTexture();



    }

    public boolean isDirection () {
        return direction;
    }

    public void setDirection (boolean direction) {
        this.direction = direction;
    }

    @Override
    public void reset () {
        setStatus(CONTINUES);

        if (direction == ROOT_LEFT) {
            flipDirection();
        }
    }
}