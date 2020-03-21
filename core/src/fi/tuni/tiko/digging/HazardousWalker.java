package fi.tuni.tiko.digging;

public abstract class HazardousWalker extends WalkingCreature implements Hazard {

    public abstract void updateMovement(GameTile[][] tiles, float delta);
}
