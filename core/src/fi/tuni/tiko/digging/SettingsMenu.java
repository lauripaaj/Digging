package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_INCLUDING_EDGES;
import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;

public class SettingsMenu extends MenuScreen {


    GameTexture controlsButtonTexture = new GameTexture(new Texture("menus/buttonControls.png"));
    GameTexture controlsButtonPressedTexture = new GameTexture(new Texture("menus/buttonControlsPressed.png"));

    GameTexture languagesText = new GameTexture(new Texture("menus/language.png"));
    GameTexture musicText = new GameTexture(new Texture("menus/music.png"));
    GameTexture soundsText = new GameTexture(new Texture("menus/sounds.png"));

    GameTexture languageToggleEn = new GameTexture(new Texture("menus/toggledEn.png"));
    GameTexture languageToggleFin = new GameTexture(new Texture("menus/toggledFin.png"));
    GameTexture toggledOn = new GameTexture(new Texture("menus/toggleOn.png"));
    GameTexture toggledOff = new GameTexture(new Texture("menus/toggleOff.png"));

    //ArrayList<MenuButton> buttons;
    MenuButton langButton;
    MenuButton soundToggle;
    MenuButton musicToggle;
    MenuButton buttonControls;


    GestureDetector settingsMenuDetector;


    public SettingsMenu (MainGame mainGame, ScreenHelper screenHelper) {
        super(mainGame, screenHelper);

        settingsMenuDetector = new GestureDetector(this);


        buttons = new ArrayList<>();

        buttons.add(playButton);


        buttons.add(backButton);

        buttonControls = new MenuButton(controlsButtonTexture, controlsButtonPressedTexture, 1.5f, 1.5f, HELP_CONTROLS);
        buttonControls.setX(5.3f);
        buttonControls.setY(-49.4f);

        buttons.add(buttonControls);

        langButton = new MenuButton(languageToggleEn, languageToggleFin, 2f, 1.1f, SET_ENGLISH_ON, SET_FINNISH_ON);
        langButton.setX(4.8f);
        langButton.setY(-54.5f);

        buttons.add(langButton);

        musicToggle = new MenuButton(toggledOn, toggledOff, 1.1f, 0.6f, SET_MUSIC_ON, SET_MUSIC_OFF);
        musicToggle.setX(4.8f);
        musicToggle.setY(-53.25f);

        buttons.add(musicToggle);

        soundToggle = new MenuButton(toggledOn, toggledOff, 1.1f, 0.6f, SET_SOUNDS_ON, SET_SOUNDS_OFF);
        soundToggle.setX(4.8f);
        soundToggle.setY(-52.25f);

        buttons.add(soundToggle);



        pressedArea = new Rectangle(-1f, -1f, pressedAreaSize/5, pressedAreaSize/7);

        pressedArea.setX(-1.15f);
        pressedArea.setY(-15f);

        //pressedArea.setHeight(1.0f);
        //pressedArea.setWidth(1.0f);





    }


    @Override
    public void show () {

        Gdx.input.setInputProcessor(settingsMenuDetector);
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

        drawTextTextures(batch);

        screenHelper.drawButtons(buttons, batch);


        batch.end();

        //screenHelper.moveCamera();
        screenHelper.switchCameraToMenu();
        //camera.update();

    }

    public void drawTextTextures(SpriteBatch batch) {
        float x = 2.35f;


        batch.draw(languagesText, x, -54.35f, 2.25f, 0.75f);
        batch.draw(musicText, x, -53.35f, 2.25f, 0.75f);
        batch.draw(soundsText, x, -52.35f, 2.25f, 0.75f);
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
