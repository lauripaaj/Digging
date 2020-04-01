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
        setChanceOfFallingTrapOnRoof(33);
    }

    public HazardTemplate(int episode, int level) {

        float episodeMultiplier;
        if (episode == 1) {
            episodeMultiplier =1;
        } else if (episode ==2) {
            episodeMultiplier = 1.5f;
        } else if (episode ==3) {
            episodeMultiplier = 2f;
        } else if (episode ==4) {
            episodeMultiplier = 2.88f;
        } else if (episode ==5) {
            episodeMultiplier = 2.55f;
        } else if (episode ==6) {
            episodeMultiplier = 2.22f;
        } else throw new IllegalArgumentException("episode should be 1-6");

        float levelMultiplier = 1;

        for (int i=0; i<level; i++) {
            levelMultiplier=levelMultiplier+0.05f;
        }

        setChanceOfSpike(15 * (int) (levelMultiplier*episodeMultiplier));
        setChanceOfGoblin(15 * (int) (levelMultiplier*episodeMultiplier));
        setChanceOfFallingTrapOnRoof(33 * (int) (levelMultiplier*episodeMultiplier));





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
