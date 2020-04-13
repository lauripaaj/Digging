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
            setChanceOfDirtTileBeingBlank(30);
            setChanceOfDirtTileBeingStone(5);
            setChanceOfDirtTileBeingDescending(48);
        } else if (customNumber==2) {
            //stone place
            setChanceOfDirtTileBeingBlank(35);
            setChanceOfDirtTileBeingStone(25);
            setChanceOfDirtTileBeingDescending(8);

        } else if (customNumber==10) {
            setChanceOfDirtTileBeingBlank(36);
            setChanceOfDirtTileBeingStone(11);
            setChanceOfDirtTileBeingDescending(20);
        }
        else if (customNumber==11) {
            setChanceOfDirtTileBeingBlank(37);
            setChanceOfDirtTileBeingStone(7);
            setChanceOfDirtTileBeingDescending(45);
        } else if (customNumber==12) {
            setChanceOfDirtTileBeingBlank(39);
            setChanceOfDirtTileBeingStone(23);
            setChanceOfDirtTileBeingDescending(15);
        }


    }
}
