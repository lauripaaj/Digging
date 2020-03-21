package fi.tuni.tiko.digging.util;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimTools {

    public static TextureRegion[] from2Dto1D(TextureRegion[][] unmodified, int rows, int cols) {
        TextureRegion[] modified = new TextureRegion[rows*cols];

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                modified[index++] = unmodified[i][j];
            }
        }
        return modified;
    }

    public static void flip(Animation<TextureRegion> animation) {
        TextureRegion[] regions = animation.getKeyFrames();
        for(TextureRegion r : regions) {
            r.flip(true, false);
        }
    }

    public static void flipUpwards(Animation<TextureRegion> animation) {
        TextureRegion[] regions = animation.getKeyFrames();
        for(TextureRegion r : regions) {
            r.flip(false, true);
        }
    }








}
