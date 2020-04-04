package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;
import static fi.tuni.tiko.digging.MainGame.UNDIGGABLE_MARGIN;



public class ScreenHelper {

    //if the camera is zoomed by playScreen
    boolean isZoomed=false;

    public static float CAMERACENTER=4.5299997f;

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
    public void updateCameraPosition(Stage currentStage, Viewport gameport, int basedOnThisManyTiles, int helperWidth, int helperHeight) {
        //if basedOnThisManyTiles is 1 or less, it will base it only on player position
        if (basedOnThisManyTiles < 1) {
            basedOnThisManyTiles = 1;
        }

        int y = player.getTilePosY();
        GameTile[][] tiles = currentStage.tiles;

        if (y+basedOnThisManyTiles < tiles.length) {
            int counter = 0;
            //int startingX = 0;



            //int endingX = 0;

            int finalStartingX=TILES_IN_ROWS_WITHOUT_EDGES+3;
            int finalEndingX=0;

            int counterOfCounter =0;


            boolean continues = true;

            for (int yToCheck=y; yToCheck<basedOnThisManyTiles+y; yToCheck++) {

                for (int x = 1; continues ; x++) {
                    if (!(tiles[yToCheck][x] instanceof StoneTile || tiles[yToCheck][x] instanceof PermanentTile) ) {

                        //we're interested in finding the lowest startingX
                        if (x <= finalStartingX) {
                            finalStartingX=x;

                        }

                        continues=false;
                    }

                }

                continues = true;

                for (int x=TILES_IN_ROWS_WITHOUT_EDGES; continues; x--) {
                    if (!(tiles[yToCheck][x] instanceof StoneTile || tiles[yToCheck][x] instanceof PermanentTile) ) {
                        if (x > finalEndingX) {
                            finalEndingX=x;
                        }
                        continues=false;
                        //endingX = x;
                        //System.out.println(endingX);
                    }
                }

                if (counter <= (finalEndingX-finalStartingX+1)) {
                    counter = finalEndingX-finalStartingX+1;
                    counterOfCounter++;
                }

            }
            //tässä tulee vissiin se virhe, jos on yksikin joka sotkee niin sitten sotkee


            if (counterOfCounter > 1) {
                useOnFour(currentStage, gameport, finalStartingX, finalEndingX, counter, helperWidth, helperHeight);

            }


            System.out.println(finalStartingX);

        }










    }



    public void updateCameraPosition(Stage currentStage, Viewport gameport) {




        GameTile[][] tiles = currentStage.tiles;
        int y = player.getTilePosY()-1;
        int counter = 0;
        int startingX = 0;
        int endingX = 0;

        for (int x = 1; startingX == 0 ; x++) {
            if (!(tiles[y][x] instanceof StoneTile || tiles[y][x] instanceof PermanentTile) ) {
                startingX=x;
                //System.out.println(x);
            }

        }

        for (int x=TILES_IN_ROWS_WITHOUT_EDGES; endingX == 0; x--) {
            if (!(tiles[y][x] instanceof StoneTile || tiles[y][x] instanceof PermanentTile) ) {
                endingX = x;
                //System.out.println(endingX);
            }
        }

        counter = endingX-startingX+1;
        System.out.println(counter);

        //useAllTheTime(currentStage, gameport, startingX, endingX, counter);
        //useOnFour(currentStage, gameport, startingX, endingX, counter);




        /*
        if (player.getTilePosY()>=5 && player.getTilePosY() <=12) {
            gameport.setWorldWidth(3.3f);
            gameport.setWorldHeight(5.5f);
            gameport.apply();
        } else {
            gameport.setWorldWidth(TILES_IN_ROWS_WITHOUT_EDGES+2*UNDIGGABLE_MARGIN);
            gameport.setWorldHeight(12.8f);
            gameport.apply();
        }*/
    }
    /*
    public void useAllTheTime(Stage currentStage, Viewport gameport, int startingX, int endingX, int counter) {
        if (counter < 7) {
            gameport.setWorldWidth(counter+2*UNDIGGABLE_MARGIN);
            //gameport.setWorldHeight(10.9714f);
            gameport.setWorldHeight(12.8f/TILES_IN_ROWS_WITHOUT_EDGES*counter);

        } else {
            gameport.setWorldWidth(TILES_IN_ROWS_WITHOUT_EDGES+2*UNDIGGABLE_MARGIN);
            gameport.setWorldHeight(12.8f);
            camera.position.x=CAMERACENTER;

        }
        gameport.apply();

    }*/

    //makes 5tiles+margins width camera
    public void  useOnFour(Stage currentStage, Viewport gameport, int startingX, int endingX, int counter, int helperWidth, int helperHeight) {
        //in this case there will be zoom
        if (counter <= 4) {


            //TÄMÄ KORVATAAN TARGETILLA
            gameport.setWorldWidth(5+2*UNDIGGABLE_MARGIN);





            //gameport.setWorldHeight(12.8f/TILES_IN_ROWS_WITHOUT_EDGES*5);

            //gameport.setWorldHeight((float)helperHeight/TILES_IN_ROWS_WITHOUT_EDGES*5);



            float multiplier = (float)helperHeight / (float) helperWidth;



            //TÄMÄ KORVATAAN TARGETILLA
            gameport.setWorldHeight(((TILES_IN_ROWS_WITHOUT_EDGES+2*UNDIGGABLE_MARGIN)*multiplier)/TILES_IN_ROWS_WITHOUT_EDGES*5);


            //gameport.setWorldHeight()

            if(startingX == 1 || startingX == 2) {
                //TÄMÄ KORVATAAN TARGETILLA
                camera.position.x=CAMERACENTER-1f;
            } else if (startingX == 4 || startingX == 5) {
                //TÄMÄ KORVATAAN TARGETILLA
                camera.position.x=CAMERACENTER+1f;;
            } else {
                //TÄMÄ KORVATAAN TARGETILLA
                camera.position.x=CAMERACENTER;
            }
            isZoomed=true;

        } else {
            //TÄMÄ KORVATAAN TARGETILLA
            gameport.setWorldWidth(TILES_IN_ROWS_WITHOUT_EDGES+2*UNDIGGABLE_MARGIN);
            //TÄMÄ KORVATAAN TARGETILLA
            gameport.setWorldHeight(12.8f);
            //TÄMÄ KORVATAAN TARGETILLA
            camera.position.x=CAMERACENTER;
            //TÄMÄ KORVATAAN TARGETILLA
            gameport.update(helperWidth, helperHeight);

            isZoomed=false;

        }
        gameport.apply();




    }


    //for testing purposes at least
    public void forceZoom(Viewport gameport, int helperWidth, int helperHeight, int cameraModifier) {


        gameport.setWorldWidth(5+2*UNDIGGABLE_MARGIN);





        //gameport.setWorldHeight(12.8f/TILES_IN_ROWS_WITHOUT_EDGES*5);

        //gameport.setWorldHeight((float)helperHeight/TILES_IN_ROWS_WITHOUT_EDGES*5);



        float multiplier = (float)helperHeight / (float) helperWidth;

        camera.position.x=CAMERACENTER+cameraModifier;

        //TÄMÄ KORVATAAN TARGETILLA
        gameport.setWorldHeight(((TILES_IN_ROWS_WITHOUT_EDGES+2*UNDIGGABLE_MARGIN)*multiplier)/TILES_IN_ROWS_WITHOUT_EDGES*5);
        gameport.apply();

        isZoomed=true;
    }

    public void forceUnzoom(Viewport gameport, int helperWidth, int helperHeight) {
        //TÄMÄ KORVATAAN TARGETILLA
        gameport.setWorldWidth(TILES_IN_ROWS_WITHOUT_EDGES+2*UNDIGGABLE_MARGIN);
        //TÄMÄ KORVATAAN TARGETILLA
        gameport.setWorldHeight(12.8f);
        //TÄMÄ KORVATAAN TARGETILLA
        camera.position.x=CAMERACENTER;
        //TÄMÄ KORVATAAN TARGETILLA
        gameport.update(helperWidth, helperHeight);

        isZoomed=false;
    }


}
