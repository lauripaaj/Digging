package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class ScreenHelper {

    private OrthographicCamera camera;
    Player player;

    //amount of pixels to help adjust the screen touching
    private float resoY;
    private float resoX;

    GameTexture playButtonTexture = new GameTexture(new Texture("menus/buttonPlay.png"));
    GameTexture playButtonTexturePressed = new GameTexture(new Texture("menus/buttonPlayPressed.png"));
    GameTexture menuBack = new GameTexture(new Texture("menus/menuPohja.png"));
    GameTexture settingsButtonTexture = new GameTexture(new Texture("menus/settingsUnpressed.png"));
    GameTexture settingsButtonTexturePressed = new GameTexture(new Texture("menus/settingsPressed.png"));



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
}
