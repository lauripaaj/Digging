package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;
import static fi.tuni.tiko.digging.MainGame.UNDIGGABLE_MARGIN;

public class TutorialScreen extends GameScreen {



    private GestureDetector tutorialDetector;
    private InfoMessageBox infoMessageBox;

    private Viewport gameport;

    public static final int TUTORIALBACK = 24;
    public static final int TUTORIALNEXT = 25;

    MenuButton buttonNext;
    MenuButton buttonBack;




    public TutorialScreen (MainGame mainGame, ScreenHelper screenHelper, InfoMessageBox infoMessageBox) {
        super(mainGame, screenHelper);

        buttons = new ArrayList<>();

        pressedArea=new Rectangle();
        pressedArea.setX(2f);
        pressedArea.setY(-15f);

        pressedArea.setHeight(1.0f);
        pressedArea.setWidth(1.0f);

        isThisTutorialScreen=true;


        tutorialDetector = new GestureDetector(this);

        this.infoMessageBox = infoMessageBox;

        buttonNext = new MenuButton(new GameTexture(new Texture("menus/buttonNext.png")),
                new GameTexture(new Texture("menus/buttonNextPressed.png")),
                        1.0f, 1.0f, TUTORIALNEXT);

        buttonNext.setX(5.2f);

        buttonBack = new MenuButton(new GameTexture(new Texture("menus/buttonBack.png")),
                new GameTexture(new Texture("menus/buttonBackPressed.png")),
                1.0f, 1.0f, TUTORIALBACK);

        buttonBack.setX(2.6f);

        buttons.add(buttonNext);
        buttons.add(buttonBack);


        gameport = new StretchViewport(TILES_IN_ROWS_WITHOUT_EDGES+2*UNDIGGABLE_MARGIN , 12.8f, camera);



    }

    @Override
    public void show () {
        Gdx.input.setInputProcessor(tutorialDetector);

        if (screenHelper.isFullTutorial()) {
            infoMessageBox.currentSlide=0;
            // only controls are shown
        } else {
            infoMessageBox.currentSlide=infoMessageBox.getBoxSize()-1;
        }

    }

    @Override
    public void render (float delta) {

        batch.setProjectionMatrix(camera.combined);





        if (actionActivated) {
            continueActionCountdown(Gdx.graphics.getDeltaTime(), buttons);
            System.out.println("tutorial screen action activated");

        }






        batch.begin();

        batch.draw(mainGame.getFarm(),+0.9f,-4f, 7.4f, 5f);
        mainGame.currentStage.draw(batch);
        mainGame.player.draw(batch);

        infoMessageBox.drawCurrentInfo(batch, buttonBack, buttonNext);



        //batch.draw(infoMessageBox.textureToShow, pressedArea.getX(), pressedArea.getY(), 1.0f, 1.0f);



        batch.end();

        screenHelper.moveCamera();

    }





    @Override
    public void performAction() {
        if (actionToPerform == TUTORIALNEXT) {
            if (infoMessageBox.currentSlide == infoMessageBox.getBoxSize()-1) {
                activateAction(PLAY);
            }
            infoMessageBox.nextSlide();

        } else if (actionToPerform == TUTORIALBACK) {
            if (infoMessageBox.currentSlide == 0) {
                activateAction(MAIN_MENU);
            }
            infoMessageBox.previousSlide();
        } else if (actionToPerform==PLAY) {

            mainGame.setScreen(mainGame.getPlayScreen());
        } else if (actionToPerform==MAIN_MENU) {
            mainGame.setScreen(mainGame.getMainMenu());
        }

    }

    @Override
    public void resize (int width, int height) {
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

        System.out.println("button Next X: "+buttonNext.getX()+", button Next, y: "+buttonNext.getY());
        System.out.print("x: "+x);
        System.out.println(", y: "+y);

        float stretchAdjustedY=screenHelper.stretchAdjustedY(y);
        float menuadjustedX=screenHelper.menuAdjustedX(x);

        System.out.print("Menuadj x: "+menuadjustedX);
        System.out.println(", Menuadj y: "+stretchAdjustedY);

        pressedArea.setX(menuadjustedX+pressedAreaSize);
        pressedArea.setY(stretchAdjustedY-4f+mainGame.player.getY());

        for (int i=0; i<buttons.size(); i++) {
            MenuButton menuButton = buttons.get(i);


            if (pressedArea.overlaps(menuButton.getRectangle())) {
                //menuButton.setPressed(true);
                menuButton.setPressed(true);
                activateAction(menuButton.getActionToPerform());

                System.out.println("pitäisi olla nappi painettu!");

                // activateAction(menuButton.getActionToPerform());
            }
        }


        return true;

        //return screenHelper.customTap(x, y, count, button, this);
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

    public InfoMessageBox getInfoMessageBox () {
        return infoMessageBox;
    }

    public void setInfoMessageBox (InfoMessageBox infoMessageBox) {
        this.infoMessageBox = infoMessageBox;
    }
}
