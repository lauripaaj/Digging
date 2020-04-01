package fi.tuni.tiko.digging;

public class TerrainTemplate {

    private int chanceOfDirtTileBeingBlank;
    private int chanceOfDirtTileBeingStone;
    private int chanceOfDirtTileBeingDescending;


    public int getChanceOfDirtTileBeingBlank () {
        return chanceOfDirtTileBeingBlank;
    }

    public void setChanceOfDirtTileBeingBlank (int chanceOfDirtTileBeingBlank) {
        this.chanceOfDirtTileBeingBlank = chanceOfDirtTileBeingBlank;
    }

    public int getChanceOfDirtTileBeingStone () {
        return chanceOfDirtTileBeingStone;
    }

    public void setChanceOfDirtTileBeingStone (int chanceOfDirtTileBeingStone) {
        this.chanceOfDirtTileBeingStone = chanceOfDirtTileBeingStone;
    }

    public int getChanceOfDirtTileBeingDescending() {
        return chanceOfDirtTileBeingDescending;
    }

    public void setChanceOfDirtTileBeingDescending(int chanceOfDirtTileBeingDescending) {
        this.chanceOfDirtTileBeingDescending = chanceOfDirtTileBeingDescending;
    }



    public TerrainTemplate() {
        setChanceOfDirtTileBeingBlank(30);
        setChanceOfDirtTileBeingStone(8);
        setChanceOfDirtTileBeingDescending(10);

    }

    public TerrainTemplate(int customNumber) {
        //descending dirt place
        if (customNumber==1) {
            setChanceOfDirtTileBeingBlank(20);
            setChanceOfDirtTileBeingStone(5);
            setChanceOfDirtTileBeingDescending(48);
        } else if (customNumber==2) {
            //stone place
            setChanceOfDirtTileBeingBlank(15);
            setChanceOfDirtTileBeingStone(25);
            setChanceOfDirtTileBeingDescending(8);

        }

    }
}
