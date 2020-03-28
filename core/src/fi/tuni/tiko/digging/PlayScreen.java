package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.Player.READY;
import static fi.tuni.tiko.digging.PlayerControls.NOQUEU;
import static fi.tuni.tiko.digging.PlayerControls.TRYDOWN;
import static fi.tuni.tiko.digging.PlayerControls.TRYLEFT;
import static fi.tuni.tiko.digging.PlayerControls.TRYRIGHT;
import static fi.tuni.tiko.digging.PlayerControls.TRYUP;

public class PlayScreen extends GameScreen {

    private GestureDetector playScreenDetector;

    private Player player;
    private PlayerControls playerControls;

    private Stage currentStage;

    private ArrayList<Stage> allStages;
    private ArrayList<LevelStats> allLevelStats;

    TilePools tilePools;
    HazardPools hazardPools;

    TileAnimationPools tileAnimationPools;



    public PlayScreen(MainGame mainGame, ScreenHelper screenHelper, PlayScreenHelper playScreenHelper) {
        super(mainGame, screenHelper);
        player = screenHelper.player;
        playerControls=playScreenHelper.playerControls;
        allStages = playScreenHelper.allStages;
        allLevelStats = playScreenHelper.allLevelStats;
        currentStage=allStages.get(0);

        tilePools = currentStage.tilePools;
        hazardPools = currentStage.hazardPools;
        tileAnimationPools = currentStage.tileAnimationPools;


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

























		batch.begin();

		batch.draw(mainGame.getFarm(),+0.9f,-4f, 7.4f, 5f);
		currentStage.draw(batch);

		tileAnimationPools.draw(batch);



		player.draw(batch);


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
        //Gdx.app.log("touchDown", "Touchdown: x: "+x+", y: "+y);
        return false;
    }

    @Override
    public boolean tap (float x, float y, int count, int button) {
        return false;
    }

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