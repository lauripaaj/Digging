package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


import fi.tuni.tiko.digging.util.AnimTools;

public class SheetAnimation {

    private Texture sheet;
    private Animation<TextureRegion> animation;
    private TextureRegion currentFrame;
    private float stateTime = 0.0f;

    private int rows;
    private int cols;

    //FramesPart divided by FramesTotal = Frame Duration. FramesTotal should always be 60fps?
    private int framesPart;
    private int framesTotal;

    public Texture getSheet () {
        return sheet;
    }

    public void setSheet (Texture sheet) {
        this.sheet = sheet;
    }

    public int getRows () {
        return rows;
    }

    public void setRows (int rows) {
        this.rows = rows;
    }

    public int getCols () {
        return cols;
    }

    public void setCols (int cols) {
        this.cols = cols;
    }

    public int getFramesPart () {
        return framesPart;
    }

    public void setFramesPart (int framesPart) {
        this.framesPart = framesPart;
    }

    public int getFramesTotal () {
        return framesTotal;
    }

    public void setFramesTotal (int framesTotal) {
        this.framesTotal = framesTotal;
    }

    // PITÄÄ VIELÄ LISÄTÄ TRUE TAI FALSE CONTINOUS VAI EI
    public SheetAnimation(Texture sheet, int rows, int cols, int framesPart, int framesTotal) {
        setSheet(sheet);

        setRows(rows);
        setCols(cols);

        setFramesPart(framesPart);
        setFramesTotal(framesTotal);

        createAnimation();

    }

    public TextureRegion getCurrentFrame () {
        return currentFrame;
    }

    public void resetAnimation() {
        stateTime=0.0f;
    }

    public void createAnimation() {

        TextureRegion[][] tmp = TextureRegion.split(
                sheet,
                sheet.getWidth() / cols,
                sheet.getHeight() / rows
        );

        TextureRegion[] frames = AnimTools.from2Dto1D(tmp, rows, cols);



        animation = new Animation<TextureRegion> ((float)framesPart/ (float) framesTotal, frames);

        AnimTools.flipUpwards(animation);


    }

    public void flipMirror() {

        AnimTools.flip(animation);

    }

    public void continueAnimationOnce(float delta) {
        stateTime += delta;

        currentFrame = animation.getKeyFrame(stateTime, false);


    }

    public void continueAnimation(float delta) {
        stateTime += delta;

        currentFrame = animation.getKeyFrame(stateTime, true);


    }

    public void dispose() {
        sheet.dispose();



    }
}
