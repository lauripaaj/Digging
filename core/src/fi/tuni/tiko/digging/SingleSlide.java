package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static fi.tuni.tiko.digging.InfoMessageBox.pergament;

//this class is used to show a single message, that can be just confirmed. At least used when completing a level for the first time
public class SingleSlide {

    int episode = 0;
    int level = 0;



    GameTexture textureToShow;
    GameTexture textureLevel10;
    GameTexture textureLevel10episode;

    GameTexture background;


    GameTexture[] numbers;

    OrthographicCamera camera;

    public SingleSlide(OrthographicCamera camera, GameTexture[] numbers) {
        background=pergament;
        textureToShow = new GameTexture(new Texture("levelComplete.png"));
        textureLevel10 = new GameTexture(new Texture("level10complete.png"));
        textureLevel10episode = new GameTexture(new Texture("level10completeAll.png"));

        this.camera=camera;
        this.numbers=numbers;

    }

    public void updateSlide(int episode, int level) {
        this.episode=episode;
        this.level=level;

    }

    public void drawCurrentInfo(SpriteBatch batch, MenuButton nextButton) {

        float nWidth=0.3f;
        float nHeight=0.6f;




        batch.draw(background, 1.12f, camera.position.y-2.02f, 7.0f, 7.5f);
        batch.draw(textureToShow, 1.89f, camera.position.y-0.5f, 5.5f, 4.5f);
        batch.draw(numbers[episode], 3.72f, camera.position.y+1.52f, nWidth, nHeight);
        batch.draw(numbers[11], 3.72f+nWidth, camera.position.y+1.52f, nWidth, nHeight);
        batch.draw(numbers[level], 3.72f+nWidth*2, camera.position.y+1.52f, nWidth, nHeight);
        nextButton.setY(camera.position.y-2.3f);
        nextButton.draw(batch);
    }

    public void drawLastLevelCompleteInfo(SpriteBatch batch, MenuButton nextButton) {
        batch.draw(background, 1.12f, camera.position.y-2.02f, 7.0f, 7.5f);
        batch.draw(textureLevel10, 1.89f, camera.position.y-0.5f, 5.5f, 4.5f);
        nextButton.setY(camera.position.y-2.3f);
        nextButton.draw(batch);

    }
    public void drawLastLevelAndEpisodeCompleteInfo(SpriteBatch batch, MenuButton nextButton) {
        batch.draw(background, 1.12f, camera.position.y-2.02f, 7.0f, 7.5f);
        batch.draw(textureLevel10episode, 1.89f, camera.position.y-0.5f, 5.5f, 4.5f);
        nextButton.setY(camera.position.y-2.3f);
        nextButton.draw(batch);

    }
}
