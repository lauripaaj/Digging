package fi.tuni.tiko.digging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Audio {

    Sound tileDigged;
    Sound walk;

    public Audio() {
        tileDigged = Gdx.audio.newSound(Gdx.files.internal("sounds/rock_impact_medium_26.mp3"));
        walk = Gdx.audio.newSound(Gdx.files.internal("sounds/Walk.mp3"));
    }

    public void dispose() {
        tileDigged.dispose();
        walk.dispose();
    }
}


