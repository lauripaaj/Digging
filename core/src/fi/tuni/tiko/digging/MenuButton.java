package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

//import static fi.tuni.tiko.digging.GameScreen.DISABLED;
import static fi.tuni.tiko.digging.GameScreen.NONE;
import static fi.tuni.tiko.digging.MenuScreen.SETTINGS_MENU;

public class MenuButton extends GameObject {

    GameTexture gameTexture;
    GameTexture gameTexturePressed;
    GameTexture gameTextureDisabled;
    GameTexture gameTextureToggledOff;

    int actionToPerform;

    int actionToPerformToggleOn;
    int actionToPerformToggleOff;


    boolean pressed = false;
    boolean enabled = true;

    boolean isThisToggleButton=false;
    boolean toggledOff=false;


    public MenuButton(GameTexture gameTexture, GameTexture gameTexturePressed, float width, float height, int actionToPerform) {
        this.gameTexture = gameTexture;
        this.gameTexturePressed = gameTexturePressed;
        setRectangle(new Rectangle(-3.0f, -3.0f, width, height));
        this.actionToPerform=actionToPerform;

    }

    //another constructor for a button, that can be set to disabled mode
    public MenuButton(GameTexture gameTexture, GameTexture gameTexturePressed, GameTexture gameTextureDisabled, float width, float height, int actionToPerform) {
        this.gameTexture = gameTexture;
        this.gameTexturePressed = gameTexturePressed;
        this.gameTextureDisabled = gameTextureDisabled;
        setRectangle(new Rectangle(-3.0f, -3.0f, width, height));
        this.actionToPerform=actionToPerform;

    }

    //constructor for toggleButtons, (sounds, music & language too)
    public MenuButton(GameTexture toggleOn, GameTexture toggleOff, float width, float height, int actionToPerformOn, int actionToPerformOff) {
        isThisToggleButton=true;
        gameTexture = toggleOn;
        gameTextureToggledOff=toggleOff;

        setRectangle(new Rectangle(-3.0f, -3.0f, width, height));

        actionToPerformToggleOff=actionToPerformOff;
        actionToPerformToggleOn = actionToPerformOn;

        actionToPerform=actionToPerformToggleOff;
    }

    public void toggleOn() {

            toggledOff = false;
            actionToPerform=actionToPerformToggleOff;

        }

    public void toggleOff() {
        toggledOff=true;
        actionToPerform=actionToPerformToggleOn;
    }


    public void enable() {
        enabled=true;
    }

    public void disable() {
        enabled=false;
        actionToPerform=NONE;
    }


    public int getActionToPerform () {
        return actionToPerform;
    }

    public void setActionToPerform (int actionToPerform) {
        this.actionToPerform = actionToPerform;
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

        if (!isThisToggleButton) {

            if (!enabled) {
                batch.draw(gameTextureDisabled, getX(), getY(), getWidth(), getHeight());
            }

            else {
                if (pressed == false) {
                    batch.draw(gameTexture, getX(), getY(), getWidth(), getHeight());
                } else {

                    batch.draw(gameTexturePressed, getX(), getY(), getWidth(), getHeight());

                }
            }

        //toggleButton to draw
        } else {
            if (toggledOff) {
                batch.draw(gameTextureToggledOff, getX(), getY(), getWidth(), getHeight());
            } else {
                batch.draw(gameTexture, getX(), getY(), getWidth(), getHeight());
            }
        }



    }
}
