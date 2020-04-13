package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;
import static fi.tuni.tiko.digging.MainGame.UNDIGGABLE_MARGIN;
import static fi.tuni.tiko.digging.TutorialScreen.buttonNextPressedTexture;
import static fi.tuni.tiko.digging.TutorialScreen.buttonNextTexture;

public class SingleSlideScreen extends GameScreen {

    SingleSlide singleSlide;
    private GestureDetector slideDetector;
    MenuButton buttonNext;

    static final int LEVELCOMPLETE = 1;
    static final int LEVEL10COMPLETE = 10;
    static final int LEVEL10_AND_EPISODE_COMPLETE = 11;

    int infoToShow=LEVELCOMPLETE;



    public SingleSlideScreen(MainGame mainGame, ScreenHelper screenHelper, SingleSlide singleSlide) {
        super(mainGame, screenHelper);

        isThisTutorialScreen=true;

        slideDetector = new GestureDetector(this);

        buttons = new ArrayList<>();

        buttonNext = new MenuButton(buttonNextTexture,
                buttonNextPressedTexture,
                1.0f, 1.0f, PLAY);
        buttonNext.setX(2.5f);
        buttonNext.setY(55f);

        buttons.add(buttonNext);

        pressedArea=new Rectangle();
        pressedArea.setX(2f);
        pressedArea.setY(-15f);

        pressedArea.setHeight(pressedAreaSize*2.5f);
        pressedArea.setWidth(pressedAreaSize*2.5f);

        gameport = new StretchViewport(TILES_IN_ROWS_WITHOUT_EDGES+2*UNDIGGABLE_MARGIN , 12.8f, camera);
        this.singleSlide = singleSlide;

    }

    @Override
    public void show () {

        Gdx.input.setInputProcessor(slideDetector);

    }

    @Override
    public void render (float delta) {

        batch.setProjectionMatrix(camera.combined);

        if (actionActivated) {
            continueActionCountdown(Gdx.graphics.getDeltaTime(), buttons);
            //System.out.println("tutorial screen action activated");

        }

        batch.begin();

        batch.draw(mainGame.getFarm(),+0.9f,-4f, 7.4f, 5f);
        mainGame.currentStage.draw(batch);
        mainGame.player.draw(batch);

        switch(infoToShow) {
            case LEVELCOMPLETE:
                singleSlide.drawCurrentInfo(batch, buttonNext);
                break;
            case LEVEL10COMPLETE:
                singleSlide.drawLastLevelCompleteInfo(batch, buttonNext);
                break;
            case LEVEL10_AND_EPISODE_COMPLETE:
                singleSlide.drawLastLevelAndEpisodeCompleteInfo(batch, buttonNext);
                break;
        }


        //batch.draw(buttonNextTexture, pressedArea.getX(), pressedArea.getY(), pressedArea.getWidth(), pressedArea.getHeight());




        //batch.draw(infoMessageBox.textureToShow, pressedArea.getX(), pressedArea.getY(), 1.0f, 1.0f);



        batch.end();

        screenHelper.moveCamera();

    }

    public int getInfoToShow () {
        return infoToShow;
    }

    public void setInfoToShow (int infoToShow) {
        this.infoToShow = infoToShow;
    }

    @Override
    public void resize (int width, int height) {
        gameport.update(width, height);
    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void hide () {

    }

    @Override
    public void dispose () {

    }

    @Override
    public boolean touchDown (float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap (float x, float y, int count, int button) {
        return screenHelper.stretchedTap(x, y, count, button, this);
    }

    @Override
    public boolean longPress (float x, float y) {
        return false;
    }

    @Override
    public boolean fling (float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan (float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop (float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom (float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop () {

    }

    @Override
    public void performAction() {
        if (actionToPerform==PLAY) {
            mainGame.setScreen(mainGame.playScreen);

            //mainGame.putAllBackIntoPools();
            //mainGame.currentStage.generateNewMap();

            //screenHelper.playScreenContinues=true;

        }

    }
}
