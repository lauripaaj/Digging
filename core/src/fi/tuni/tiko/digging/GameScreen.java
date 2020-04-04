package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public abstract class GameScreen implements Screen, GestureDetector.GestureListener {

    public GameScreen(MainGame mainGame, ScreenHelper screenHelper) {


        this.screenHelper = screenHelper;

        this.mainGame = mainGame;

        batch=mainGame.getBatch();
        camera = screenHelper.getCamera();
    }

    Viewport gameport;



    ArrayList<MenuButton> buttons;

    Rectangle pressedArea;

    ScreenHelper screenHelper;

    OrthographicCamera camera;

    MainGame mainGame;

    SpriteBatch batch;

    boolean isThisTutorialScreen=false;

    public static final int NONE = 0;
    public static final int PLAY = 1;
    public static final int MAIN_MENU = 2;
    public static final int SETTINGS_MENU = 3;
    public static final int HELP_TUTORIAL = 4;
    public static final int HELP_CONTROLS = 5;









    int actionToPerform = NONE;



    boolean actionActivated = false;
    float activationTimeLeft;

    //it's probably better if the game doesn't have to get Gdx.graphics.getWidth() on every frame,
    // so let's initiate it here. It might cause probelms though if screen can be rotated/resized during the
    // game or something like that.

    float screenHeight= Gdx.graphics.getHeight();
    float screenWidth=Gdx.graphics.getWidth();
    float pressedAreaSize = 0.5f;

    public void performAction() {
        if (actionToPerform == SETTINGS_MENU) {
            mainGame.setScreen(mainGame.getSettingsMenu());


        } else if (actionToPerform == MAIN_MENU) {
            mainGame.setScreen(mainGame.getMainMenu());
        } else if (actionToPerform == PLAY) {
            mainGame.setScreen(mainGame.getPlayScreen());
        } else if (actionToPerform == HELP_TUTORIAL) {
            //mainGame.getTutorialScreen().getInfoMessageBox().currentSlide=0;
            screenHelper.setFullTutorial(true);
            mainGame.setScreen(mainGame.getTutorialScreen());

        } else if (actionToPerform == HELP_CONTROLS) {
            screenHelper.setFullTutorial(false);
            mainGame.setScreen(mainGame.getTutorialScreen());
        }

        actionToPerform=NONE;
    }

    public void activateAction(int actionToPerform) {
        this.actionToPerform=actionToPerform;
        actionActivated=true;
        activationTimeLeft=2.0f;
    }

    public void resetAction() {
        actionActivated=false;
        activationTimeLeft=2.0f;
    }

    public boolean isActionActivated() {
        return actionActivated;
    }

    public void continueActionCountdown(float delta, ArrayList<MenuButton> buttons) {
        activationTimeLeft = activationTimeLeft - delta*8.9f;

        if (activationTimeLeft <= 0) {

            resetAction();
            for (int i=0; i<buttons.size(); i++) {
                MenuButton menuButton = buttons.get(i);
                if (menuButton.isPressed()) {
                    menuButton.setPressed(false);
                }
            }
            performAction();

        }
    }
}
