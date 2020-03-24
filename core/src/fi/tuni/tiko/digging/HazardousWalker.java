package fi.tuni.tiko.digging;

public abstract class HazardousWalker extends WalkingCreature implements Hazard {

    public abstract void updateMovement(GameTile[][] tiles, float delta);

    @Override
    public void occupyTile (Stage currentStage) {
        currentStage.tiles[getRawTileY()][getRawTileX()].setOccupied(true);
    }

    @Override
    public void unOccupyTile (Stage currentStage) {
        currentStage.tiles[getRawTileY()][getRawTileX()].setOccupied(false);

    }
}
