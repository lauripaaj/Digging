package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Audio {

    Sound tileDigged;
    Sound walk;

    Music music;

    public Audio() {
        tileDigged = Gdx.audio.newSound(Gdx.files.internal("sounds/rock_impact_medium_26.mp3"));
        walk = Gdx.audio.newSound(Gdx.files.internal("sounds/Walk2.mp3"));

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/Loop.mp3"));
        music.setLooping(true);
    }

    public void dispose() {
        tileDigged.dispose();
        walk.dispose();
    }
}


