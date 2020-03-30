package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.Player.DIGGING;
import static fi.tuni.tiko.digging.Player.READY;
import static fi.tuni.tiko.digging.PlayerControls.NOQUEU;
import static fi.tuni.tiko.digging.PlayerControls.TRYDOWN;
import static fi.tuni.tiko.digging.PlayerControls.TRYLEFT;
import static fi.tuni.tiko.digging.PlayerControls.TRYRIGHT;
import static fi.tuni.tiko.digging.PlayerControls.TRYUP;

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





    }


    @Override
    public void show () {

        Gdx.input.setInputProcessor(playScreenDetector);

    }

    @Override
    public void render (float delta) {

        //float delta = Gdx.graphics.getDeltaTime();



        //huom tätä ei ehkä kannata joka renderillä aina
        //cameraplacer.updateCameraPosition(camera, delta, currentStage, player);

        batch.setProjectionMatrix(camera.combined);

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

    public void drawPlayScreenElements() {

            //pauseButton.setY(player.getY()-3.3f);
            //pauseButton.draw(batch);

        if (!pauseButton.isPressed()) {
            batch.draw(pauseButtonTexture, pauseButton.getX()-0f, player.getY()-3.3f, pauseButton.getWidth(), pauseButton.getHeight());
        } else {

            batch.draw(pauseButtonPressedTexture, pauseButton.getX()-0f, player.getY()-3.3f, pauseButton.getWidth(), pauseButton.getHeight());

        }



            //pauseButton.setY(player.getY()-3.2f);
            //if (player.getStatus()==READY) {
            //    pauseButton.draw(batch);
            //}


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