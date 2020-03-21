package fi.tuni.tiko.digging;

import com.badlogic.gdx.math.MathUtils;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;

public class AreaTemplate {

    private int MIN_ROWS = 5;
    private int MAX_ROWS = 10;

    private int rows;

    private int MIN_COLS = 3;
    private int MAX_COLS = TILES_IN_ROWS_WITHOUT_EDGES;

    private int cols;


    /* these 2 will be initalized later in StageRandomizer class, so they will
    be in relation with other areas in the mapTemplate
     */
    private int startingPosX;
    private int exitPosX;

    public int getStartingPosX () {
        return startingPosX;
    }

    public void setStartingPosX (int startingPosX) {
        this.startingPosX = startingPosX;
    }

    public int getExitPosX () {
        return exitPosX;
    }

    public void setExitPosX (int exitPosX) {
        this.exitPosX = exitPosX;
    }

    //int entranceX;
    //int exitX;


    public int getRows () {
        return rows;
    }

    public int getCols () {
        return cols;
    }

    public void setRows (int rows) {
        if (rows >= MIN_ROWS && rows <= MAX_ROWS) {
            this.rows = rows;
        } else throw new IllegalArgumentException ("Rows must be between "+MIN_ROWS+" and "+MAX_ROWS);

    }

    public void setCols (int cols) {


        if (cols >= MIN_COLS && cols <= MAX_COLS) {
            this.cols = cols;
        } else throw new IllegalArgumentException ("Cols must be between "+MIN_COLS+" and "+MAX_COLS);
        this.cols = cols;




    }

    //constructor that makes the area as random as possible
    public AreaTemplate() {
        rows=(MathUtils.random(MIN_ROWS, MAX_ROWS));
        cols=(MathUtils.random(MIN_COLS, MAX_COLS));

    }

    //constructor that makes template gives EXACT number of rows and cols
    public AreaTemplate(int rows, int cols) {
        setCols(cols);
        setRows(rows);
    }

    public AreaTemplate(int randomMinRows, int randomMaxRows, int randomMinCols, int randomMaxCols) {
        if (randomMinRows < MIN_ROWS || randomMinRows > MAX_ROWS || randomMaxRows < MIN_ROWS || randomMaxRows > MAX_ROWS) {
            throw new IllegalArgumentException ("Rows random scale points must be between "+MIN_ROWS+" and "+MAX_ROWS);
        } else if (randomMaxRows < randomMinRows) {
            int temp = randomMaxRows;
            randomMaxRows=randomMinRows;
            randomMinRows=temp;
        }
        if (randomMinCols < MIN_COLS || randomMinCols > MAX_COLS || randomMaxCols < MIN_COLS || randomMaxCols > MAX_COLS) {
            throw new IllegalArgumentException ("Cols random scale points must be between "+MIN_COLS+" and "+MAX_COLS);
        } else if (randomMaxCols < randomMinCols) {
            int temp = randomMaxCols;
            randomMaxCols=randomMinCols;
            randomMinCols=temp;
        }

        setCols(MathUtils.random(randomMinCols, randomMaxCols));
        setRows(MathUtils.random(randomMinRows, randomMaxRows));

    }


}
