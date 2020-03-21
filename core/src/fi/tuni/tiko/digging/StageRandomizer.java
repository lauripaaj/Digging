package fi.tuni.tiko.digging;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_INCLUDING_EDGES;
import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_WITHOUT_EDGES;

public class StageRandomizer {

    //GameTile[][] tiles;

    //näyttäisi tomivan ilman tätä tai inttiä ensin joka palautetaan

    ArrayList<AreaTemplate> areaTemplates = new ArrayList<>();
    /*
    DirtPool dirtPool;
    StonePool stonePool;
    BlankPool blankPool;
    DescendingPool descendingPool;
    */
    TilePools tilePools;



    public StageRandomizer(TilePools tilePools) {
        this.tilePools = tilePools;
    }



    public GameTile[][] areas (MapTemplate mapTemplate) {


        areaTemplates.clear();
        //ArrayList<AreaTemplate> areaTemplates = mapTemplate.getArrayList();
        areaTemplates = mapTemplate.getArrayList();

        //x0 is "farm" row
        //x1-4 are guaranteed all dirt
        //x last one will be all stone so arraySize 6 all in total cause it starts from 1 in counter

        int arraySize = 6;

        /*goes through all previously randomized areas (rows and cols are already generated by now)
        and checks how many rows there are in total, so we will know how many rows there will be
        in the array we will generate afterwards. arraySize will be added to the number since those
        will always be there
         */
        for (int i=0; i < areaTemplates.size(); i++) {
            AreaTemplate at = areaTemplates.get(i);
            arraySize += at.getRows();
        }
        //9 will always be the size of the map at current version hmm. that could also be some static
        //final
        GameTile[][] tiles = new GameTile[arraySize][TILES_IN_ROWS_INCLUDING_EDGES];

        //lisää edgejä lukuunottamatta koko homman stoneksi, edgetkin voisi mutta ehkä parempi pitää erillään jos niitä vaihdetaankin
        for (int y = 1; y< tiles.length-1; y++) {
            for (int x = 1; x < tiles[0].length-1; x++) {
                StoneTile stone = tilePools.getStonePool().obtain();
                tiles[y][x] = stone;
                stone.setInPlace(y,x);
            }

        }

        //there must be a guranteed entrance in the same spot as last areas exit posX.
        //also startingPosX is added to the templates so we know how many stone tiles to generate

        addStartingPosXandExitToAreaTemplates();

        tiles =addStoneEdgesUsingTemplates(tiles);

        tiles =addRandomTiles(tiles, mapTemplate);


        return tiles;


    }

    public GameTile[][] addRandomTiles(GameTile[][] tiles, MapTemplate mapTemplate) {

        int chanceOfBlankTile=mapTemplate.getTerrainTemplate().getChanceOfDirtTileBeingBlank();
        int chanceOfStoneTile=mapTemplate.getTerrainTemplate().getChanceOfDirtTileBeingStone();
        int chanceOfDescendingTile=mapTemplate.getTerrainTemplate().getChanceOfDirtTileBeingDescending();

        for(int y=5; y<tiles.length; y++) {
            for (int x=1; x<tiles[0].length; x++) {
                //GameTile currentTile = tiles[y][x];
                if (tiles[y][x] instanceof DirtTile ) {
                    //System.out.println("found dirtTile");

                    int randomResult=MathUtils.random(1, 100);
                    System.out.println(randomResult);
                    if (randomResult<=chanceOfBlankTile) {
                        System.out.println("blank chance happened");

                        tilePools.getDirtPool().free((DirtTile)tiles[y][x]);

                        BlankTile blank = tilePools.getBlankPool().obtain();
                        tiles[y][x] = blank;
                        blank.setInPlace(y,x);


                    } else if ( (randomResult>chanceOfBlankTile) && (randomResult <= (chanceOfBlankTile+chanceOfStoneTile)) ) {
                        System.out.println("stone chance happened");

                        tilePools.getDirtPool().free((DirtTile)tiles[y][x]);

                        StoneTile stone = tilePools.getStonePool().obtain();
                        tiles[y][x] = stone;
                        stone.setInPlace(y,x);
                        
                    } else if ( (randomResult> (chanceOfBlankTile+chanceOfStoneTile) ) && (randomResult <= (chanceOfBlankTile+chanceOfStoneTile+chanceOfDescendingTile)) ) {
                        System.out.println("descending chance happened");

                        tilePools.getDirtPool().free((DirtTile)tiles[y][x]);

                        DescendingDirtTile descending = tilePools.getDescendingPool().obtain();
                        tiles[y][x] = descending;
                        descending.setInPlace(y,x);



                    }


                }
            }
        }

        return tiles;
    }

    public GameTile[][] addStoneEdgesUsingTemplates(GameTile[][] tiles) {

        //start from 5 since 1-4 are all dirt and 0 is "farm" tiles
        AreaTemplate aTemplate= areaTemplates.get(0);
        int areaTemplateCounter = 0;
        int areaTemplateRowCounter = 0;

        for (int y=5; y<tiles.length - 1; y++) {
            //for (int x = 1; x < tiles[0].length-1; x++) {


            for (int x = aTemplate.getStartingPosX(); x < aTemplate.getStartingPosX()+aTemplate.getCols(); x++) {

                tilePools.getStonePool().free((StoneTile)tiles[y][x]);

                DirtTile dirt = tilePools.getDirtPool().obtain();
                tiles[y][x]= dirt;
                dirt.setInPlace(y,x);
            }
            areaTemplateRowCounter++;
            //System.out.println("y: "+y);
            //System.out.println("AtemplateRowCounter: "+areaTemplateRowCounter);

            if(areaTemplateRowCounter == aTemplate.getRows()) {
                areaTemplateRowCounter=0;
                //System.out.println("areaTemplateRowCounterFOund");
                areaTemplateCounter++;
                if (areaTemplateCounter < areaTemplates.size() ) {
                    aTemplate = areaTemplates.get(areaTemplateCounter);

                }

            }

        }



        return tiles;

    }

    public void addStartingPosXandExitToAreaTemplates() {

        //this needs to be randomed so there will be some value in the first area.
        int lastAreasExitPosX = MathUtils.random(1,TILES_IN_ROWS_WITHOUT_EDGES+1);

        for (int i=0; i<areaTemplates.size(); i++) {
            AreaTemplate aTemplate = areaTemplates.get(i);
            aTemplate = giveStartingRowAndExitTile(aTemplate , lastAreasExitPosX);

            lastAreasExitPosX=aTemplate.getExitPosX();
            //lastAreasExitPosX=2;
        }


    }




    /*giveStartingRowAndExitTile(...) is just a precaution to make it absolutely sure that there is at least one tile
    where player can dig into another area without hitting a stone block.
    Note: There will be another method (NAME HERE) which will make sure there is a possible route
    of non-permanent tiles that player can take between the areas
     */

    private AreaTemplate giveStartingRowAndExitTile (AreaTemplate at, int lastAreasExit) {
        //int[] toBeReturned = new int[2];
        int howManyTilesInArow = at.getCols();
        int posXstart = -9999;

        /*it will keep randomizing the startingRow as many times as it's needed to make it sure that
        there is possibility to dig from one area to another. (lastAreasExit PosX...)
         */

        while ( (lastAreasExit < posXstart) || (lastAreasExit > posXstart+howManyTilesInArow) ) {
            posXstart = MathUtils.random(1, (1+TILES_IN_ROWS_WITHOUT_EDGES-howManyTilesInArow) );
            System.out.println("posXstart: "+posXstart+", lastAreasExit: "+lastAreasExit);
        }

        //will hold the info of one dirt-tiled x-position, that will be the guaranteed entrance x-position in the next  area
        at.setStartingPosX(posXstart);

        //might need work, but if there will never be impossibility to go to next area just let it be
        at.setExitPosX(MathUtils.random(posXstart,posXstart+howManyTilesInArow-1) );

        //tähän vaan set x johonkin niistä randomeista ei se sen kummempi oo



        return at;
    }
    /*
    public void randomizeTiles(int startingY, int startingX, int yAmount, int xAmount, GameTile[][] tiles) {


        for (int y = startingY; y < yAmount; y++) {
            for (int x = startingX; x < startingX+xAmount; x++) {
                tiles[y][x] = new DirtTile(y, x);
            }
        }


        for (int y = startingY; y < yAmount; y++) {
            for (int x = startingX; x < xAmount; x++) {
                int result = MathUtils.random(1,10);
                if (result == 10) {
                    tiles[y][x] = new PermanentTile(y, x);
                } else if (result == 8 || result ==9) {
                    tiles[y][x] = new BlankTile(y, x);
                }
            }
        }
    }
    */
}
