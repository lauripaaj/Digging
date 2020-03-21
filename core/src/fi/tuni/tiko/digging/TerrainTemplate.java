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
}
