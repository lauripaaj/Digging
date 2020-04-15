package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_INCLUDING_EDGES;
import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;

public class QuestionMenuScreen extends MenuScreen {

    GestureDetector questionMenuDetector;

    GameTexture yesButtonTexture = new GameTexture(new Texture("menus/yes.png"));
    GameTexture yesButtonPressedTexture = new GameTexture(new Texture("menus/yesPressed.png"));

    GameTexture noButtonTexture = new GameTexture(new Texture("menus/no.png"));
    GameTexture noButtonPressedTexture = new GameTexture(new Texture("menus/noPressed.png"));

    GameTexture questionBackground = new GameTexture(new Texture("menus/questionBackground.png"));
    GameTexture questionQuit = new GameTexture(new Texture("menus/questionQuit.png"));
    GameTexture questionAbort = new GameTexture(new Texture("menus/questionAbort.png"));
    GameTexture questionRestart = new GameTexture(new Texture("menus/questionRestart.png"));
    GameTexture questionStartNew = new GameTexture(new Texture("menus/questionStart.png"));
    GameTexture questionEpisode3 = new GameTexture(new Texture("menus/questionEpisode3.png"));


    MenuButton yesButton;
    MenuButton noButton;

    int questionToAsk;

    public QuestionMenuScreen (MainGame mainGame, ScreenHelper screenHelper) {
        super(mainGame, screenHelper);

        questionMenuDetector = new GestureDetector(this);

        yesButton = new MenuButton(yesButtonTexture, yesButtonPressedTexture, 1.5f, 1.5f, NONE);
        noButton = new MenuButton(noButtonTexture, noButtonPressedTexture, 1.5f, 1.5f, MAIN_MENU);


        buttons = new ArrayList<>();

        buttons.add(yesButton);


        buttons.add(noButton);

        buttons.add(mainGame.getMainMenu().helpButton);
        buttons.add(mainGame.getMainMenu().quitGameButton);
        buttons.add(mainGame.getMainMenu().highScoreButton);



        yesButton.setX(5.3f);
        yesButton.setY(-51.4f);

        noButton.setX(2.2f);
        noButton.setY(-51.4f);

        buttons.add(yesButton);
        buttons.add(noButton);

        pressedArea = new Rectangle(-1f, -1f, pressedAreaSize, pressedAreaSize);

        pressedArea.setX(-1.15f);
        pressedArea.setY(-15f);

        //pressedArea.setHeight(1.0f);
        //pressedArea.setWidth(1.0f);





    }



    @Override
    public void show () {

        Gdx.input.setInputProcessor(questionMenuDetector);
        screenHelper.switchCameraToMenu();

    }
    @Override
    public void render (float delta) {

        batch.setProjectionMatrix(camera.combined);



        if (actionActivated) {
            continueActionCountdown(Gdx.graphics.getDeltaTime(), buttons);

        }






        batch.begin();

        drawBackgroundAssets();

        drawQuestionAssets(questionToAsk);

        screenHelper.drawButtons(buttons, batch);


        batch.end();

        //screenHelper.moveCamera();
        screenHelper.switchCameraToMenu();
        //camera.update();

    }

    public void setQuestionToAsk(int questionToAsk) {
        this.questionToAsk=questionToAsk;

        if (questionToAsk==ABORT_LEVEL_CONFIRM) {
            yesButton.setActionToPerform(ABORT_LEVEL);
        } else if (questionToAsk==QUITGAME_CONFIRM) {
            yesButton.setActionToPerform(QUITGAME);
        } else if (questionToAsk==START_NEW_GAME_CONFIRM) {
            yesButton.setActionToPerform(START_NEW_GAME);
        } else if (questionToAsk==RESTART_LEVEL_CONFIRM) {
            yesButton.setActionToPerform(RESTART_LEVEL);
        }


    }

    public void drawQuestionAssets(int question) {

        batch.draw(questionBackground, 1f, -54.5f, TILES_IN_ROWS_WITHOUT_EDGES, TILES_IN_ROWS_WITHOUT_EDGES-2f);

        float qX=1f;
        float qY=-54f;
        float qWidth=TILES_IN_ROWS_WITHOUT_EDGES;
        float qHeight=TILES_IN_ROWS_WITHOUT_EDGES/3;

        if (questionToAsk == QUITGAME_CONFIRM) {
            batch.draw(questionQuit, qX, qY, qWidth, qHeight);
        } else if (questionToAsk == ABORT_LEVEL_CONFIRM) {
            batch.draw(questionAbort, qX, qY, qWidth, qHeight);
        } else if (questionToAsk == RESTART_LEVEL_CONFIRM) {
            batch.draw(questionRestart, qX, qY, qWidth, qHeight);
        }

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

        /*
        pressedArea.setPosition(screenHelper.menuAdjustedX(x), screenHelper.menuAdjustedY(y)-60f);

        for (int i=0; i<buttons.size(); i++) {
            MenuButton menuButton = buttons.get(i);
            if (pressedArea.overlaps(menuButton.getRectangle())) {
                menuButton.setPressed(true);

                activateAction(menuButton.getActionToPerform());
            }
        }

        return true;
        */
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


}
