package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class ScreenHelper {

    // will be used only in tutorialScreen
    private boolean fullTutorial=true;

    private OrthographicCamera camera;
    Player player;

    //coordinates of game
    private float resoY;
    private float resoX;

    GameTexture playButtonTexture = new GameTexture(new Texture("menus/buttonPlay.png"));
    GameTexture playButtonTexturePressed = new GameTexture(new Texture("menus/buttonPlayPressed.png"));
    GameTexture menuBack = new GameTexture(new Texture("menus/menuPohja.png"));
    GameTexture settingsButtonTexture = new GameTexture(new Texture("menus/settingsUnpressed.png"));
    GameTexture settingsButtonTexturePressed = new GameTexture(new Texture("menus/settingsPressed.png"));

    GameTexture helpButtonTexture = new GameTexture(new Texture("menus/buttonHelp.png"));
    GameTexture helpButtonTexturePressed = new GameTexture(new Texture("menus/buttonHelpPressed.png"));



    public GameTexture getPlayButtonTexture () {
        return playButtonTexture;
    }

    public GameTexture getPlayButtonTexturePressed () {
        return playButtonTexturePressed;
    }

    public GameTexture getSettingsButtonTexture () {
        return settingsButtonTexture;
    }

    public GameTexture getSettingsButtonTexturePressed () {
        return settingsButtonTexturePressed;
    }

    public GameTexture getMenuBack () {
        return menuBack;
    }

    public GameTexture getHelpButtonTexture () {
        return helpButtonTexture;
    }

    public GameTexture getHelpButtonTexturePressed () {
        return helpButtonTexturePressed;
    }

    //player is needed for camera so it's possible to have the position adjusted according to
    //players pos y, (menu windows will be drawn based on that too)
    public ScreenHelper(OrthographicCamera camera, Player player, float resoX, float resoY) {
        this.camera=camera;
        this.player = player;
        this.resoX= resoX;
        this.resoY= resoY;
    }

    public OrthographicCamera getCamera () {
        return camera;
    }

    public void moveCamera() {
        //System.out.println(player.getY());
        camera.position.y = player.getY()+3f;
        camera.update();

    }

    public boolean isFullTutorial () {
        return fullTutorial;
    }

    public void setFullTutorial (boolean fullTutorial) {
        this.fullTutorial = fullTutorial;
    }

    public void switchCameraToUi() {
        camera.position.y = -30f;
        camera.update();
    }

    public void switchCameraToMenu() {
        camera.position.y = -52f;
        camera.update();
    }

    public void drawButtons(ArrayList<MenuButton> buttons, SpriteBatch batch) {



        for (int i=0; i<buttons.size(); i++) {
            MenuButton button = buttons.get(i);
            button.draw(batch);
        }

    }

    public float screenAdjustedX (float x) {
        float screenWidth = Gdx.graphics.getWidth();
        float xPercentage = x / screenWidth;

        return xPercentage * resoX;


    }

    public float screenAdjustedY (float y) {
        float screenHeight = Gdx.graphics.getHeight();

        float yPercentage = y / screenHeight;

        return yPercentage * resoY;
    }

    public float adjustToYPosition (float y) {
        System.out.println(resoY);
        //return y-3.3f;
        return y-0.2578f*resoY;
    }

    public float adjustToXPosition (float x) {
        return x+1.0f;
    }


    public float flipY (float y) {

        float halfway = resoY /2;

        float newY = 0;

        if (y <= halfway) {
            newY = halfway + (halfway-y);
        } else {
            newY = halfway - (halfway-y);
        }

        return newY;
    }

    public float stretchAdjustedY (float y) {
        float yPercentage = y / Gdx.graphics.getHeight();
        return yPercentage * 12.8f;

    }

    public float menuAdjustedX (float x) {


        float xPercentage = x / Gdx.graphics.getWidth();

        return xPercentage * resoX;


    }

    public float menuAdjustedY (float y) {

        float yPercentage = y / Gdx.graphics.getHeight();
        return yPercentage * resoY;
    }

    public float getResoY () {
        return resoY;
    }



    public float getResoX () {
        return resoX;
    }


    public boolean customTap(float x, float y, int count, int button, GameScreen gameScreen) {


        Rectangle pressedArea = gameScreen.pressedArea;
        ArrayList <MenuButton> buttons = gameScreen.buttons;

        System.out.print("x: "+x);
        System.out.println(", y: "+y);

        System.out.println(menuAdjustedX(x));
        System.out.println(menuAdjustedY(y));



        //float adjustedX = screenHelper.screenAdjustedX(x);
        //float adjustedY = screenHelper.screenAdjustedY(y);

        //System.out.println("AdjustedX: "+ adjustedX+", AdjustedY: "+ adjustedY);



        //adjustedY = screenHelper.flipY(adjustedY);
        //adjustedY = screenHelper.adjustToYPosition(adjustedY);
        //adjustedX = screenHelper.adjustToXPosition(adjustedX);

        //pressedArea.setPosition(adjustedX - pressedAreaSize, adjustedY - pressedAreaSize);

        //pressedArea.setPosition(screenHelper.menuAdjustedX(x), screenHelper.menuAdjustedY(y)-60f+(0.057f*screenHelper.getResoY()));
        if (!gameScreen.isThisTutorialScreen) {
            pressedArea.setPosition(menuAdjustedX(x), menuAdjustedY(y)-60f);
        } else {
            pressedArea.setPosition(menuAdjustedX(x), menuAdjustedY(y)-4f);
        }


        //bad way to do this, but in an attempt to make this work on different screen resolutions, ArrayList is used in help to
        //try to figure out which button was the one that was attempted to be pressed the most

        ArrayList<MenuButton> buttonsPressed = new ArrayList<>();

        for (int i=0; i<buttons.size(); i++) {
            MenuButton menuButton = buttons.get(i);


            if (pressedArea.overlaps(menuButton.getRectangle())) {
                //menuButton.setPressed(true);
                buttonsPressed.add(menuButton);

                // activateAction(menuButton.getActionToPerform());
            }
        }

        //rectangle probably messes up buttons from the up rather than from the down, at least in lower screen, so
        //we decide the button with lowest y-coordinate was the one player tried to press
        MenuButton buttonThatWasPressed = null;


        for (int i=0; i < buttonsPressed.size(); i++) {
            if (buttonThatWasPressed==null) {
                buttonThatWasPressed=buttonsPressed.get(i);
            } else {
                if (buttonsPressed.get(i).getY() > buttonThatWasPressed.getY()) {
                    buttonThatWasPressed=buttonsPressed.get(i);
                }
            }
        }
        if (buttonThatWasPressed != null) {
            buttonThatWasPressed.setPressed(true);
            gameScreen.activateAction(buttonThatWasPressed.getActionToPerform());
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






        //System.out.println("Hey hey yea");
        return true;

    }


}
