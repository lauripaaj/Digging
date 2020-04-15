package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_INCLUDING_EDGES;
import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;




public class MainMenu extends MenuScreen {

    //this should probably be removed later, not sure yet what I'm doing :D
    //GameTexture menuBackTexture = new GameTexture(new Texture("menus/testiPohja.png"));

    GameTexture startNewGame = new GameTexture(new Texture("menus/startNewGame.png"));
    GameTexture startNewGamePressed = new GameTexture(new Texture("menus/startNewGamePressed.png"));

    GameTexture restartLevel = new GameTexture(new Texture("menus/tryAgain.png"));
    GameTexture restartLevelPressed = new GameTexture(new Texture("menus/tryAgainPressed.png"));
    GameTexture restartLevelDisabled = new GameTexture(new Texture("menus/tryAgainDisabled.png"));

    GameTexture abortLevel = new GameTexture(new Texture("menus/abortLevel.png"));
    GameTexture abortLevelPressed = new GameTexture(new Texture("menus/abortLevelPressed.png"));

    GameTexture quitGame = new GameTexture(new Texture("menus/quit.png"));
    GameTexture quitGamePressed = new GameTexture(new Texture("menus/quitPressed.png"));

    GameTexture highScore = new GameTexture(new Texture("menus/buttonHighscore.png"));
    GameTexture highScorePressed = new GameTexture(new Texture("menus/buttonHighscorePressed.png"));


    private GestureDetector mainMenuDetector;



    //boolean settingsPressed = false;
    //boolean playPressed=false;






    MenuButton settingsButton;
    MenuButton helpButton;
    private MenuButton startNewGameButton;
    private MenuButton restartLevelButton;
    private MenuButton abortLevelButton;
    MenuButton quitGameButton;
    MenuButton highScoreButton;











    public MainMenu(MainGame mainGame, ScreenHelper screenHelper) {

        super(mainGame, screenHelper);



        mainMenuDetector = new GestureDetector(this);

        buttons = new ArrayList<>();





        buttons.add(playButton);


        settingsButton = new MenuButton(screenHelper.getSettingsButtonTexture(), screenHelper.getSettingsButtonTexturePressed(), 4.0f, 1.0f, SETTINGS_MENU);
        settingsButton.setX(2.35f);
        settingsButton.setY(-50.8f);

        startNewGameButton = new MenuButton(startNewGame, startNewGamePressed, 4.0f, 1.0f, START_NEW_GAME_CONFIRM);
        startNewGameButton.setX(2.35f);
        startNewGameButton.setY(-51.9f);

        restartLevelButton = new MenuButton(restartLevel, restartLevelPressed, restartLevelDisabled, 4.0f, 1.0f, RESTART_LEVEL_CONFIRM);
        restartLevelButton.setX(2.35f);
        restartLevelButton.setY(-53.0f);

        abortLevelButton = new MenuButton(abortLevel, abortLevelPressed, 4.0f, 1.0f, ABORT_LEVEL_CONFIRM);
        abortLevelButton.setX(2.35f);
        abortLevelButton.setY(-54.1f);

        quitGameButton = new MenuButton(quitGame, quitGamePressed, 1.5f, 1.5f, QUITGAME_CONFIRM);
        quitGameButton.setX(2.15f);
        quitGameButton.setY(-49.4f);

        highScoreButton = new MenuButton(highScore, highScorePressed, 1.5f, 1.5f, HIGHSCORE);
        highScoreButton.setX(3.725f);
        highScoreButton.setY(-49.4f);





        buttons.add(settingsButton);
        buttons.add(startNewGameButton);
        buttons.add(restartLevelButton);
        buttons.add(abortLevelButton);
        buttons.add(quitGameButton);
        buttons.add(highScoreButton);

        helpButton = new MenuButton(screenHelper.getHelpButtonTexture(), screenHelper.getHelpButtonTexturePressed(), 1.5f, 1.5f, HELP_TUTORIAL);

        helpButton.setX(5.3f);
        helpButton.setY(-49.4f);
        buttons.add(helpButton);



        pressedArea = new Rectangle(-24f, -0.5f, pressedAreaSize/7, pressedAreaSize/5);


    }

    @Override
    public void show () {

        Gdx.input.setInputProcessor(mainMenuDetector);

        //settingsButton.setY(screenHelper.player.getY()+3.8f);
        //playButton.setY(screenHelper.player.getY()+3.8f);

        screenHelper.switchCameraToMenu();

        if (mainGame.restartLevelAvailable) {
           restartLevelButton.enable();
        } else {
            restartLevelButton.disable();
        }

        //System.out.println("switched to main menu");

    }

    @Override
    public void render (float delta) {

        batch.setProjectionMatrix(camera.combined);

        if (actionActivated) {
            continueActionCountdown(Gdx.graphics.getDeltaTime(), buttons);

         }






        batch.begin();

        //batch.draw(menuBackTexture.getTexture(), 0, screenHelper.player.getY()-5f, screenWidth, screenHeight);

        drawBackgroundAssets();




        screenHelper.drawButtons(buttons, batch);


        batch.end();

        screenHelper.switchCameraToMenu();
        //camera.update();

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
}
