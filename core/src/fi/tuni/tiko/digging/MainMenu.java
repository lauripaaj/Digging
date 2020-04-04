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




    private GestureDetector mainMenuDetector;



    //boolean settingsPressed = false;
    //boolean playPressed=false;






    private MenuButton settingsButton;
    private MenuButton helpButton;










    public MainMenu(MainGame mainGame, ScreenHelper screenHelper) {

        super(mainGame, screenHelper);



        mainMenuDetector = new GestureDetector(this);

        buttons = new ArrayList<>();





        buttons.add(playButton);


        settingsButton = new MenuButton(screenHelper.getSettingsButtonTexture(), screenHelper.getSettingsButtonTexturePressed(), 4.0f, 1.0f, SETTINGS_MENU);


        settingsButton.setX(2.45f);
        settingsButton.setY(-50.8f);

        buttons.add(settingsButton);

        helpButton = new MenuButton(screenHelper.getHelpButtonTexture(), screenHelper.getHelpButtonTexturePressed(), 1.65f, 1.65f, HELP_TUTORIAL);

        helpButton.setX(5.2f);
        helpButton.setY(-49.4f);
        buttons.add(helpButton);



        pressedArea = new Rectangle(-24f, -0.5f, pressedAreaSize, pressedAreaSize);


    }

    @Override
    public void show () {

        Gdx.input.setInputProcessor(mainMenuDetector);

        //settingsButton.setY(screenHelper.player.getY()+3.8f);
        //playButton.setY(screenHelper.player.getY()+3.8f);

        screenHelper.switchCameraToMenu();

        System.out.println("switched to main menu");

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
