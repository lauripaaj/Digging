package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.OrthographicCamera;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_INCLUDING_EDGES;
import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;
import static fi.tuni.tiko.digging.MainGame.UNDIGGABLE_MARGIN;

//DOESNT WORK SHOULD NOT BE USED

public class Cameraplacer {

    float targetPosX;
    float posX;

    int startingTile;

    float targetSizeInTiles;
    float sizeInTiles;

    public void updateCameraPosition (OrthographicCamera camera, float delta, Stage currentStage, Player player) {

        int[] startAndEndPositions = new int[2];
        startAndEndPositions = getStartAndEndPositions(currentStage, player);

        int startingTile=startAndEndPositions[0];
        int endingTile=startAndEndPositions[1];

        int tilesCameraWillCover=endingTile-startingTile;
        float cameraRatio = 1f / TILES_IN_ROWS_WITHOUT_EDGES * tilesCameraWillCover;

        camera.setToOrtho(true, cameraRatio * TILES_IN_ROWS_WITHOUT_EDGES + 2 * UNDIGGABLE_MARGIN, (12.8f * cameraRatio));

        posX=(float)(startingTile+endingTile)/2;

        camera.position.x = 1.03f + posX;


    }


    public int[] getStartAndEndPositions (Stage currentStage, Player player) {

        int rowsToCheck = 6;
        int playerPosY = player.getTilePosY();
        if ((playerPosY + rowsToCheck) >= currentStage.tiles.length) {
            rowsToCheck = currentStage.tiles.length - playerPosY;
        }


        int startingX = 8;
        int endingX = 1;


        for (int y = playerPosY; y < rowsToCheck; y++) {

            for (int x = 1; (x < TILES_IN_ROWS_INCLUDING_EDGES - 1); x++) {
                if (!(currentStage.tiles[y][x] instanceof StoneTile)) {
                    if (startingX > x) {
                        startingX = x;
                    }
                    if (endingX < x) {
                        endingX = x;
                    }
                }
            }
        }

        int[] toBeReturned = new int[2];
        toBeReturned[0]=startingX;
        toBeReturned[1]=endingX;
        return toBeReturned;
    }
}
