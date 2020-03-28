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



    private ArrayList<MenuButton> buttons;
    private GestureDetector mainMenuDetector;



    //boolean settingsPressed = false;
    boolean playPressed=false;






    private MenuButton settingsButton;










    public MainMenu(MainGame mainGame, ScreenHelper screenHelper) {

        super(mainGame, screenHelper);



        mainMenuDetector = new GestureDetector(this);

        buttons = new ArrayList<>();





        buttons.add(playButton);


        settingsButton = new MenuButton(screenHelper.getSettingsButtonTexture(), screenHelper.getSettingsButtonTexturePressed(), 4.0f, 1.0f, SETTINGS_MENU);


        settingsButton.setX(2.45f);
        settingsButton.setY(screenHelper.player.getY()+3.8f);

        buttons.add(settingsButton);

        pressedArea = new Rectangle(-1f, -1f, pressedAreaSize, pressedAreaSize);


    }

    @Override
    public void show () {

        Gdx.input.setInputProcessor(mainMenuDetector);

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

        screenHelper.moveCamera();

    }

    @Override
    public void resize (int width, int height) {

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

        float adjustedX = screenHelper.screenAdjustedX(x);
        float adjustedY = screenHelper.screenAdjustedY(y);

        System.out.println("AdjustedX: "+ adjustedX+", AdjustedY: "+ adjustedY);



        //adjustedY = screenHelper.flipY(adjustedY);
        adjustedY = screenHelper.adjustToYPosition(adjustedY);
        adjustedX = screenHelper.adjustToXPosition(adjustedX);

        pressedArea.setPosition(adjustedX - pressedAreaSize, adjustedY - pressedAreaSize);

        for (int i=0; i<buttons.size(); i++) {
            MenuButton menuButton = buttons.get(i);
            if (pressedArea.overlaps(menuButton.getRectangle())) {
                menuButton.setPressed(true);

                activateAction(menuButton.getActionToPerform());
            }
        }
        /*
        if (pressedArea.overlaps(settingsButton.getRectangle())) {
            System.out.println("settings pressed");
            settingsButton.setPressed(true);

            activateAction(SETTINGS_MENU);



        } else if (pressedArea.overlaps(playButton.getRectangle())) {
            System.out.println("play pressed");
            playPressed=true;
        }*/




        //System.out.print("x: "+x);
        //System.out.println("y: "+y);

        //System.out.println("Hey hey yea");
        return true;
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
