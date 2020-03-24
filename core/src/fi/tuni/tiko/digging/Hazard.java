package fi.tuni.tiko.digging;

public interface Hazard {

    boolean getGetsDestroyedByFallingPlayer();

    void vanish();

    void occupyTile(Stage currentStage);
    void unOccupyTile(Stage currentStage);


    //hazardStrentgths: 1=Goblin (gets destroyed in contact with spike and fallingTile)
    //                  2=Spike & FallingTile (destroying each other)
    int getHazardStrength();



}
