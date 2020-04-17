package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.PlayerControls.NOQUEU;

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
    public static final int QUITGAME = -1;
    public static final int HIGHSCORE = -2;
    public static final int START_NEW_GAME=-3;

    //public static final int DISABLED=-3;

    public static final int START_NEW_GAME_CONFIRM = 291;
    public static final int RESTART_LEVEL_CONFIRM = 292;
    public static final int ABORT_LEVEL_CONFIRM = 293;
    public static final int QUITGAME_CONFIRM = 294;

    public static final int ABORT_LEVEL=393;
    public static final int RESTART_LEVEL = 392;

    public static final int SET_ENGLISH_ON=551;
    public static final int SET_FINNISH_ON=552;
    public static final int SET_MUSIC_ON=553;
    public static final int SET_MUSIC_OFF=554;
    public static final int SET_SOUNDS_ON=555;
    public static final int SET_SOUNDS_OFF=556;









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
        } else if (actionToPerform == ABORT_LEVEL_CONFIRM) {
            mainGame.questionMenuScreen.setQuestionToAsk(ABORT_LEVEL_CONFIRM);
            mainGame.setScreen(mainGame.questionMenuScreen);
        } else if (actionToPerform == ABORT_LEVEL) {
            if (mainGame.level>1) {
                mainGame.level=mainGame.level-1;
            }
            mainGame.farmLevel=true;
            mainGame.putAllBackIntoPools();
            mainGame.startStage();
            mainGame.setScreen(mainGame.playScreen);
        } else if (actionToPerform == RESTART_LEVEL_CONFIRM) {
            mainGame.questionMenuScreen.setQuestionToAsk(RESTART_LEVEL_CONFIRM);
            mainGame.setScreen(mainGame.questionMenuScreen);
        } else if (actionToPerform == RESTART_LEVEL) {

            for(int i=0; i<15; i++) {
                System.out.println("Level that should start is "+mainGame.level);
            }

            mainGame.putAllBackIntoPools();
            mainGame.startStage();
            mainGame.setScreen(mainGame.playScreen);
            if (!mainGame.farmLevel) {
                mainGame.currentStage.makePlayerStartFromEntrance();
            }

            mainGame.restartLevelAvailable=false;
        } else if (actionToPerform == QUITGAME_CONFIRM) {
            mainGame.questionMenuScreen.setQuestionToAsk(QUITGAME_CONFIRM);
            mainGame.setScreen(mainGame.questionMenuScreen);
        } else if (actionToPerform == QUITGAME) {
            mainGame.dispose();
            Gdx.app.exit();
            System.exit(0);
        } else if (actionToPerform == SET_ENGLISH_ON) {
            mainGame.languageEnglish=true;
            mainGame.getSettingsMenu().langButton.toggleOn();
            System.out.println("english on");
        } else if (actionToPerform == SET_FINNISH_ON) {
            mainGame.languageEnglish=false;
            mainGame.getSettingsMenu().langButton.toggleOff();
            System.out.println("english off");
        } else if (actionToPerform == SET_MUSIC_ON) {
            mainGame.audio.music.play();
            mainGame.musicOn=true;
            mainGame.getSettingsMenu().musicToggle.toggleOn();
        } else if (actionToPerform == SET_MUSIC_OFF) {
            mainGame.musicOn=false;
            mainGame.audio.music.stop();
            mainGame.getSettingsMenu().musicToggle.toggleOff();
        } else if (actionToPerform == SET_SOUNDS_ON) {
            mainGame.soundsOn=true;
            mainGame.getSettingsMenu().soundToggle.toggleOn();
        } else if (actionToPerform == SET_SOUNDS_OFF) {
            mainGame.soundsOn=false;
            mainGame.getSettingsMenu().soundToggle.toggleOff();
        } else if (actionToPerform == HIGHSCORE) {
            mainGame.setScreen(mainGame.highScoreScreen);
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
