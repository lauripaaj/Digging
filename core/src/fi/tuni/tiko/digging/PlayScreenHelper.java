package fi.tuni.tiko.digging;

import java.util.ArrayList;

/*will contain stuff only needed in PlayScreen. This class is used in PlayScreen constructor to help
move large amount of useful stuff created in MainGame to be used in PlayScreen
 */
public class PlayScreenHelper {

    PlayerControls playerControls;

    ArrayList<Stage> allStages;
    ArrayList<LevelStats> allLevelStats;



    public PlayScreenHelper (PlayerControls playerControls, ArrayList<Stage> allStages, ArrayList<LevelStats> allLevelStats) {

        this.playerControls = playerControls;
        this.allStages = allStages;
        this.allLevelStats = allLevelStats;


    }



}
