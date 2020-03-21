package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ResourceGainedAnimation extends SheetAnimation implements Poolable {

    private float x;
    private float y;

    private float playingTimeLeft;

    boolean isPlaying=false;

    public ResourceGainedAnimation(Texture sheet, int rows, int cols, int framesPart, int framesTotal) {
        super(sheet, rows, cols, framesPart, framesTotal);

    }


    public ResourceGainedAnimation() {
        super(new Texture("resourceGained.png"), 1, 10, 10, 60);
    }

    public void startAnimation() {
        isPlaying=true;
        playingTimeLeft=1.9f;


    }

    public void updateAnimation(float delta) {
        float speed=delta*1f;

        setY(getY()-speed*0.5f);

        continueAnimationOnce(speed);

        //System.out.println("continuing Animation, timeleft: "+playingTimeLeft+" x: "+x+", y:"+y);

        playingTimeLeft -= speed;

        if (playingTimeLeft < 0) {
            isPlaying=false;
        }
    }

    @Override
    public void reset () {

        resetAnimation();
        isPlaying=false;

    }

    public float getX () {
        return x;
    }

    public void setX (float x) {
        this.x = x;
    }

    public float getY () {
        return y;
    }

    public void setY (float y) {
        this.y = y;
    }

    public boolean isPlaying () {
        return isPlaying;
    }

    public void setPlaying (boolean playing) {
        isPlaying = playing;
    }

    public void draw(SpriteBatch batch) {


        if (isPlaying) {
            batch.draw(getCurrentFrame(), getX(), getY(), 1.0f, 1.0f);
        }
    }
}
