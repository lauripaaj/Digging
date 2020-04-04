package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_INCLUDING_EDGES;
import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;
import static fi.tuni.tiko.digging.MainGame.UNDIGGABLE_MARGIN;


public abstract class MenuScreen extends GameScreen {


    public MenuScreen(MainGame mainGame, ScreenHelper screenHelper) {
        super(mainGame, screenHelper);


        gameport = new StretchViewport(TILES_IN_ROWS_WITHOUT_EDGES+2*UNDIGGABLE_MARGIN , 12.8f, camera);

        playButton = new MenuButton(screenHelper.getPlayButtonTexture(), screenHelper.getPlayButtonTexturePressed(), 1.24f, 1.24f, PLAY);

        playButton.setX(6.17f);
        playButton.setY(-56f);

        backButton = new MenuButton(backButtonTexture, backButtonTexturePressed, 1.8f, 1.8f, MAIN_MENU);
        backButton.setX(2.0f);
        backButton.setY(-49.6f);







    }

    GameTexture officialBack = new GameTexture(new Texture("menus/Background.png"));


    MenuButton playButton;
    MenuButton backButton;

    GameTexture backButtonTexture = new GameTexture(new Texture("menus/buttonBack.png"));
    GameTexture backButtonTexturePressed = new GameTexture(new Texture("menus/buttonBackPressed.png"));







    public void drawBackgroundAssets() {
        batch.draw(officialBack, 0, -60f, TILES_IN_ROWS_INCLUDING_EDGES, 15f);
        batch.draw(screenHelper.getMenuBack(), 1.6f, -56f, TILES_IN_ROWS_WITHOUT_EDGES-1.2f, 9f);
    }



}
