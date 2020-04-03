package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class InfoMessageBox {

    static GameTexture pergament = new GameTexture(new Texture("paper.png"));
    static GameTexture cellphone = new GameTexture(new Texture("handCellphone.png"));


    static GameTexture info_game1 = new GameTexture(new Texture("teksti1.png"));
    static GameTexture info_game2 = new GameTexture(new Texture("teksti2.png"));


    static GameTexture info_level1 = new GameTexture(new Texture("tekstiLevel1.png"));
    static GameTexture info_level2 = new GameTexture(new Texture("tekstiLevel2.png"));
    static GameTexture info_level3 = new GameTexture(new Texture("tekstiLevel3.png"));


    static GameTexture info_swipe = new GameTexture(new Texture("tekstiSwipe.png"));

    GameTexture textureToShow;
    GameTexture background;

    OrthographicCamera camera;
    SpriteBatch batch;

    float background_y;
    float background_x;
    float message_y;
    float message_x;



    //General gameInfo
    ArrayList<GameTexture> topBox = new ArrayList<>();

    //Current goal
    ArrayList<GameTexture> middleBox = new ArrayList<>();

    //Controls
    ArrayList<GameTexture> bottomBox = new ArrayList<>();

    int currentSlide=0;


    public InfoMessageBox(OrthographicCamera camera, SpriteBatch batch) {

        fillEarlyGameInfo();

        bottomBox.add(info_swipe);

        this.camera=camera;
        this.batch=batch;



    }

    public void fillEarlyGameInfo() {
        topBox.clear();
        topBox.add(info_game1);
        topBox.add(info_game2);

        middleBox.clear();
        middleBox.add(info_level1);
        middleBox.add(info_level2);
        middleBox.add(info_level3);
    }

    public void nextSlide() {

        int numberOfSlides = topBox.size()+middleBox.size()+bottomBox.size();

        if (currentSlide < numberOfSlides-1) {
            currentSlide++;
        } else {

        }
    }

    public void previousSlide() {

        if (currentSlide > 0) {
            currentSlide--;
        }

    }

    public int getBoxSize() {
        return topBox.size()+middleBox.size()+bottomBox.size();
    }





    public void drawCurrentInfo(SpriteBatch batch, MenuButton backButton, MenuButton nextButton) {




        for (int i=0; i<topBox.size()+middleBox.size()+bottomBox.size(); i++) {
            if (currentSlide<topBox.size()) {
                background = pergament;
                background_y= camera.position.y-6.42f;
                background_x= 1.12f;

                message_y= background_y+0.2f;
                message_x= background_x+0.72f;

                textureToShow=(topBox.get(currentSlide));

                backButton.setY(camera.position.y-1.7f);
                nextButton.setY(camera.position.y-1.7f);



            } else if (currentSlide<topBox.size()+middleBox.size()) {
                background = pergament;
                background_y=camera.position.y-2.02f;
                background_x = 1.12f;

                message_y= background_y+0.2f;
                message_x= background_x+0.72f;

                textureToShow=(middleBox.get(currentSlide-topBox.size()));

                backButton.setY(camera.position.y-3f);
                nextButton.setY(camera.position.y-3f);
            } else {
                background = cellphone;
                background_y=camera.position.y-0.03f;
                background_x=1.12f;

                message_y= background_y+0.2f;
                message_x= background_x+3.72f;

                textureToShow=(bottomBox.get(currentSlide-(topBox.size()+middleBox.size())));

                backButton.setY(camera.position.y-1.5f);
                nextButton.setY(camera.position.y-1.5f);
            }

        }

        batch.draw(background, background_x, background_y, 7.0f, 4.5f);
        batch.draw(textureToShow, message_x, message_y, 5.5f, 4.5f);

        //if(currentSlide != 0) {
            backButton.draw(batch);
        //}
        nextButton.draw(batch);
    }


}
