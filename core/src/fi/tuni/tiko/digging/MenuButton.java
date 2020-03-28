package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import static fi.tuni.tiko.digging.MenuScreen.SETTINGS_MENU;

public class MenuButton extends GameObject {

    GameTexture gameTexture;
    GameTexture gameTexturePressed;

    int actionToPerform;



    boolean pressed = false;



    public MenuButton(GameTexture gameTexture, GameTexture gameTexturePressed, float width, float height, int actionToPerform) {
        this.gameTexture = gameTexture;
        this.gameTexturePressed = gameTexturePressed;
        setRectangle(new Rectangle(-3.0f, -3.0f, width, height));
        this.actionToPerform=actionToPerform;

    }

    public int getActionToPerform () {
        return actionToPerform;
    }

    public boolean isPressed () {
        return pressed;
    }

    public void setPressed (boolean pressed) {
        this.pressed = pressed;
    }

    public GameTexture getGameTexture () {
        return gameTexture;
    }

    public GameTexture getGameTexturePressed () {
        return gameTexturePressed;
    }

    public void setGameTexture (GameTexture gameTexture) {
        this.gameTexture = gameTexture;
    }

    public void setGameTexturePressed (GameTexture gameTexturePressed) {
        this.gameTexturePressed = gameTexturePressed;
    }

    @Override
    public void draw (SpriteBatch batch) {




        //if (!isVanishing() || getVanishTimeLeft() <= 0) {
        if (pressed == false) {
            batch.draw(gameTexture, getX(), getY(), getWidth(), getHeight());
        } else {

            batch.draw(gameTexturePressed, getX(), getY(), getWidth(), getHeight());

        }

    }
}
