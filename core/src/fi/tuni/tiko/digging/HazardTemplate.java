package fi.tuni.tiko.digging;

//IMPORTANT this should probably be made in a way that further the stage player descends, the more hazards there will be
//OR it could be harder if condition is bad, these chances should be made later


import java.util.ArrayList;

public class HazardTemplate {

    //chance of spike being generated in BLANK tile.
    private int chanceOfSpike;
    //wil have lots of others later
    private int chanceOfGoblin;

    private int chanceOfFallingTrapOnRoof;


    //default constructor
    public HazardTemplate() {
        setChanceOfSpike(15);
        setChanceOfGoblin(15);
        setChanceOfFallingTrapOnRoof(30);
    }

    public HazardTemplate(int episode, int level) {

        float episodeMultiplier;
        if (episode == 1) {
            episodeMultiplier =1;
        } else if (episode ==2) {
            episodeMultiplier = 1.9f;
        } else if (episode ==3) {
            episodeMultiplier = 2.5f;
        } else if (episode ==4) {
            episodeMultiplier = 3.1f;
        } else if (episode ==5) {
            episodeMultiplier = 3.8f;
        } else if (episode ==6) {
            episodeMultiplier = 4.6f;
        } else throw new IllegalArgumentException("episode should be 1-6");

        float levelMultiplier = 1;

        for (int i=0; i<level; i++) {
            levelMultiplier=levelMultiplier+0.22f;
        }

        setChanceOfSpike((int) (15 * (levelMultiplier*episodeMultiplier)));
        setChanceOfGoblin((int) (15 * (levelMultiplier*episodeMultiplier)));
        setChanceOfFallingTrapOnRoof((int) (30 * levelMultiplier*episodeMultiplier));





    }

    public int getChanceOfFallingTrapOnRoof () {
        return chanceOfFallingTrapOnRoof;
    }

    public void setChanceOfFallingTrapOnRoof (int chanceOfFallingTrapOnRoof) {
        this.chanceOfFallingTrapOnRoof = chanceOfFallingTrapOnRoof;
    }

    public int getChanceOfGoblin () {

        return chanceOfGoblin;
    }

    public void setChanceOfGoblin (int chanceOfGoblin) {
        if (chanceOfSpike >= 0 && chanceOfSpike <= 100) {
            this.chanceOfGoblin = chanceOfGoblin;
        }
    }

    public int getChanceOfSpike () {
        return chanceOfSpike;
    }

    public void setChanceOfSpike (int chanceOfSpike) {
        if (chanceOfSpike >= 0 && chanceOfSpike <= 100)
        this.chanceOfSpike = chanceOfSpike;
    }


}
