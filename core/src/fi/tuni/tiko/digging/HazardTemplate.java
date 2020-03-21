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
        setChanceOfSpike(20);
        setChanceOfGoblin(20);
        setChanceOfFallingTrapOnRoof(50);
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
