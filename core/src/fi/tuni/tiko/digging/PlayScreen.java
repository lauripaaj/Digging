package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_INCLUDING_EDGES;
import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;
import static fi.tuni.tiko.digging.MainGame.UNDIGGABLE_MARGIN;
import static fi.tuni.tiko.digging.Player.DIGGING;
import static fi.tuni.tiko.digging.Player.READY;
import static fi.tuni.tiko.digging.PlayerControls.NOQUEU;
import static fi.tuni.tiko.digging.PlayerControls.TRYDOWN;
import static fi.tuni.tiko.digging.PlayerControls.TRYLEFT;
import static fi.tuni.tiko.digging.PlayerControls.TRYRIGHT;
import static fi.tuni.tiko.digging.PlayerControls.TRYUP;
import static fi.tuni.tiko.digging.ScreenHelper.CAMERACENTER;

public class PlayScreen extends GameScreen {


    private GestureDetector playScreenDetector;

    private ArrayList<MenuButton> buttons;

    private Player player;
    private PlayerControls playerControls;

    private Stage currentStage;

    private ArrayList<Stage> allStages;
    private ArrayList<LevelStats> allLevelStats;

    //private SpriteBatch hudBatch;

    TilePools tilePools;
    HazardPools hazardPools;

    TileAnimationPools tileAnimationPools;

    MenuButton pauseButton;
    //MenuButton pauseButton2;

    GameTexture pauseButtonTexture = new GameTexture(new Texture("menus/buttonPause.png"));
    GameTexture pauseButtonPressedTexture = new GameTexture(new Texture("menus/buttonPausePressed.png"));

    //Viewport gameport;

    int helperWidth;
    int helperHeight;




    public PlayScreen(MainGame mainGame, ScreenHelper screenHelper, PlayScreenHelper playScreenHelper) {
        super(mainGame, screenHelper);

        //hudBatch = new SpriteBatch();

        buttons = new ArrayList<>();

        player = screenHelper.player;
        playerControls=playScreenHelper.playerControls;
        allStages = playScreenHelper.allStages;
        allLevelStats = playScreenHelper.allLevelStats;
        currentStage=allStages.get(0);

        tilePools = currentStage.tilePools;
        hazardPools = currentStage.hazardPools;
        tileAnimationPools = currentStage.tileAnimationPools;

        pauseButton = new MenuButton(pauseButtonTexture, pauseButtonPressedTexture, 1.34f, 1.34f, MAIN_MENU);
        pauseButton.setX(1.0f);
        pauseButton.setY(0.5f);

       // pauseButton2 = new MenuButton(pauseButtonTexture, pauseButtonPressedTexture, 5.34f, 5.34f, MAIN_MENU);

        //System.out.println("pausebutton2getx: "+pauseButton2.getX()+", pausebutton2gety: "+pauseButton2.getY());

        //pauseButton2.setX(3.0f);
        //pauseButton2.setY(-32f);

        buttons.add(pauseButton);


        pressedArea = new Rectangle(-1f, -1f, pressedAreaSize, pressedAreaSize);



        playScreenDetector = new GestureDetector(this);

        //gameport = new ExtendViewport(TILES_IN_ROWS_WITHOUT_EDGES+2*UNDIGGABLE_MARGIN, 155.8f, camera);
        //gameport = new ExtendViewport(TILES_IN_ROWS_WITHOUT_EDGES+2*UNDIGGABLE_MARGIN, 155.8f, camera);
        gameport = new ExtendViewport(TILES_IN_ROWS_WITHOUT_EDGES+2*UNDIGGABLE_MARGIN, 12.8f, camera);

        //gameport.setScreenSize(222, 554);
        //gameport.apply();


        //camera.position.x is starting from 4.5299997

        //camera.position.x--;






    }


    @Override
    public void show () {

        Gdx.input.setInputProcessor(playScreenDetector);
        //gameport.apply();

        //gameport.update(466,12, true);
        //gameport.update(233, 245);


    }

    @Override
    public void render (float delta) {

        //float delta = Gdx.graphics.getDeltaTime();

        //System.out.println(helperHeight);
        //System.out.println(helperWidth);


        //System.out.println(camera.position.x);

        //huom tätä ei ehkä kannata joka renderillä aina
        //cameraplacer.updateCameraPosition(camera, delta, currentStage, player);

        batch.setProjectionMatrix(camera.combined);
        screenHelper.updateCameraPosition(currentStage, gameport, 5, helperWidth, helperHeight);


        /*
        if (player.getTilePosY() % 2 ==1) {
            screenHelper.forceZoom(gameport, helperWidth, helperHeight, -1);

        } else {
            screenHelper.forceUnzoom(gameport, helperWidth, helperHeight);
        }
        */


        clearScreen();

        mainGame.checkSpecialTiles(delta);
        mainGame.checkVanishingTiles(delta);

        //System.out.println(player.getX());
        //System.out.println(player.getY());

        //Not sure if this is the right class to hold checkPlayersUnwantedMovemement()-method or would it be better to be in player?

        //updateHazardOccupations is too resource heavy for phone
        //updateHazardOccupations();

        mainGame.checkDeadHazards();
        mainGame.checkPlayerHazardCollision();
        mainGame.checkHazardHazardCollision();

        mainGame.checkIfHazardsGetTriggeredOrNoticed();


        mainGame.checkHazardMovement(delta);

        mainGame.checkPlayersUnwantedMovement();



        //System.out.println(playerControls.getQueu());


        if ( (player.getStatus() == READY) && (playerControls.getQueu() != NOQUEU) ) {
            playerControls.checkQueu(player, currentStage);
        }

        mainGame.controlPlayer();





        player.updateMovement(delta, currentStage);

        tileAnimationPools.checkResourceGainedAnimations(delta);



















        if (actionActivated) {
            continueActionCountdown(Gdx.graphics.getDeltaTime(), buttons);

            System.out.println("this worked");
            //screenHelper.forceUnzoom(gameport, helperWidth, helperHeight);


        }





		batch.begin();



		batch.draw(mainGame.getFarm(),+0.9f,-4f, 7.4f, 5f);
        currentStage.draw(batch);

		tileAnimationPools.draw(batch);





		player.draw(batch);

        //drawPlayScreenElements();


        //drawPlayScreenElements();
        //screenHelper.switchCameraToUi();

        drawPlayScreenElements();
		batch.end();

		screenHelper.moveCamera();

		//hudBatch.begin();
		//hudBatch.setProjectionMatrix(camera.combined);
		//camera.position.y=-35f;
		//camera.update();
        //pauseButton.draw(hudBatch);
		//hudBatch.end();

        //screenHelper.moveCamera();

    }

    //horrible coding but this will have to do for now
    public void drawPlayScreenElements() {

            //pauseButton.setY(player.getY()-3.3f);
            //pauseButton.draw(batch);




        if (!pauseButton.isPressed()) {

            if(!screenHelper.isZoomed) {
                batch.draw(pauseButtonTexture, pauseButton.getX()-0f, camera.position.y-6.4f, pauseButton.getWidth(), pauseButton.getHeight());

            } else {
                batch.draw(pauseButtonTexture, camera.position.x-2.55f, camera.position.y-4.58f, pauseButton.getWidth()/7.2f*5.2f, pauseButton.getHeight()/7.2f*5.2f);
            }


        } else {
            if(!screenHelper.isZoomed) {

                batch.draw(pauseButtonPressedTexture, pauseButton.getX() - 0f, camera.position.y - 6.4f, pauseButton.getWidth(), pauseButton.getHeight());
            } else {
                batch.draw(pauseButtonPressedTexture, camera.position.x-2.55f, camera.position.y-4.58f, pauseButton.getWidth()/7.2f*5.2f, pauseButton.getHeight()/7.2f*5.2f);
            }


        }



            //pauseButton.setY(player.getY()-3.2f);
            //if (player.getStatus()==READY) {
            //    pauseButton.draw(batch);
            //}


    }

    @Override
    public void resize (int width, int height) {

        gameport.update(width, height);
        helperWidth=width;
        helperHeight=height;

    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void hide () {
        camera.position.x=CAMERACENTER;

    }

    @Override
    public void dispose () {

    }

    @Override
    public boolean touchDown (float x, float y, int pointer, int button) {
        //Gdx.app.log("touchDown", "Touchdown: x: "+x+", y: "+y);
        return false;
    }

    @Override
    public boolean tap (float x, float y, int count, int button) {

        pressedArea.setPosition(screenHelper.menuAdjustedX(x), screenHelper.menuAdjustedY(y));

        System.out.println("pressedAreaY: "+pressedArea.y);
        System.out.println("pressedAreaX: "+pressedArea.x);

        System.out.println("pauseButtonGetY: "+pauseButton.getY());
        System.out.println("pauseButton.getX: "+pauseButton.getX());

        for (int i=0; i<buttons.size(); i++) {
            MenuButton menuButton = buttons.get(i);
            if (pressedArea.overlaps(menuButton.getRectangle())) {
                menuButton.setPressed(true);

                activateAction(menuButton.getActionToPerform());
            }
        }

        return true;
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



    @Override
    public boolean longPress (float x, float y) {

        if (player.getStatus() == READY) {
            for (int yyy = player.getTilePosY(); yyy < player.getTilePosY() + 7; yyy++) {
                currentStage.tiles[yyy][player.getTilePosX()] = new BlankTile(yyy, player.getTilePosX());
            }
            return true;

        }
        return false;
    }

    @Override
    public boolean fling (float velocityX, float velocityY, int button) {

        if (player.getStatus() == READY) {

            if ((velocityX > Math.abs(velocityY))) {

                playerControls.tryRight(player, currentStage);
                return true;


            } else if ((velocityX < 0) && Math.abs(velocityX) > Math.abs(velocityY)) {

                playerControls.tryLeft(player, currentStage);
                return true;

            } else if ((velocityY > Math.abs(velocityX))) {

                playerControls.tryDown(player, currentStage);
                return true;
            } else {
                playerControls.tryUp(player, currentStage);
                return true;
            }


        } else {
            if ((velocityX > Math.abs(velocityY))) {

                playerControls.setQueu(TRYRIGHT);
            } else if ((velocityX < 0) && Math.abs(velocityX) > Math.abs(velocityY)) {

                playerControls.setQueu(TRYLEFT);

            } else if ((velocityY > Math.abs(velocityX))) {

                playerControls.setQueu(TRYDOWN);
            } else {
                playerControls.setQueu(TRYUP);
            }

            return true;
        }


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


    private void clearScreen() {
        Gdx.gl.glClearColor(0.39f, 0.39f, 0.39f, 0.19f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}