package fi.tuni.tiko.digging;

public abstract class HazardousWalker extends WalkingCreature implements Hazard {

    //added episode and player just to have Goblin have extra difficulty based on episode
    public abstract void updateMovement(GameTile[][] tiles, float delta, int episode, Player player);

    @Override
    public void occupyTile (Stage currentStage) {
        currentStage.tiles[getRawTileY()][getRawTileX()].setOccupied(true);
    }

    @Override
    public void unOccupyTile (Stage currentStage) {
        currentStage.tiles[getRawTileY()][getRawTileX()].setOccupied(false);

    }
}
