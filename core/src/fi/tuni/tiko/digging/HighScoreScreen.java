package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_INCLUDING_EDGES;
import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;
import static fi.tuni.tiko.digging.MainGame.UNDIGGABLE_MARGIN;

public class HighScoreScreen extends MenuScreen {

    Viewport gameport;

    private BitmapFont font;

    GestureDetector highScoreDetector;

    public HighScoreScreen (MainGame mainGame, ScreenHelper screenHelper) {
        super(mainGame, screenHelper);

        font=mainGame.getFont();

        gameport=new StretchViewport(400 , 800, camera);
        highScoreDetector = new GestureDetector(this);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.39f, 0.39f, 0.39f, 0.19f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void switchToHighScoreCamera() {

        camera.position.y = 0;


        //camera.setToOrtho(true, 1920, 1080);
        //camera.setToOrtho(true);
        camera.update();

    }

    @Override
    public void show () {
        Gdx.input.setInputProcessor(highScoreDetector);
        switchToHighScoreCamera();

    }

    @Override
    public void render (float delta) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        switchToHighScoreCamera();
        //Vector3 tmpCoords = new Vector3(Gdx.input.getX()*4, Gdx.input.getY()*4, 0);
        //camera.project(tmpCoords);


        clearScreen();

        batch.draw(officialBack, -197, -400, 425, 800);

        //font.draw(batch, "Phosphorus: ", 0.1f, 2.1f, 8f, 0, false);

        font.draw(batch, "About High Scores: ", 103f, -367f, 8f, 0, false);
        font.draw(batch, "+ You must complete the", 132f, -297f, 8f, 0, false);
        font.draw(batch, "game to have your entry.", 142f, -262f, 8f, 0, false);
        font.draw(batch, "+ Each level you complete", 142f, -210, 8f, 0, false);
        font.draw(batch, "for the FIRST time will", 122f, -175, 8f, 0, false);
        font.draw(batch, "increase your score.", 103f, -140, 8f, 0, false);
        font.draw(batch, "- Each time getting zapped", 153f, -88f, 8f, 0, false);
        font.draw(batch, "by any hazard decreases it", 153f, -53f, 8f, 0, false);
        font.draw(batch, "- Each time having to use", 132f, -1f, 8f, 0, false);
        font.draw(batch, "'Abort Game' menu option", 152f, 34f, 8f, 0, false);
        font.draw(batch, "will decrease it a little.", 132f, 69f, 8f, 0, false);

        font.draw(batch, "+/- Using 'Restart level',", 122f, 121, 8f, 0, false);
        font.draw(batch, "and everything not mentioned", 176f, 156, 8f, 0 , false);
        font.draw(batch, "has no effect on the score.", 159f, 191, 8f, 0, false);

        //font.draw(batch, "Phosphorus: ", 2, 1, 1, 1,2,0, false);
        //font.draw(batch, "Pasdasd", 2, 2, 1, 1, 222, 2, false);


        batch.end();

    }

    @Override
    public void resize (int width, int height) {
        //gameport.update(1122 , 1122*12);
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
        System.out.println("jea");
        mainGame.setScreen(mainGame.getMainMenu());
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
