package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_INCLUDING_EDGES;
import static fi.tuni.tiko.digging.SingleSlideScreen.LEVEL10COMPLETE;
import static fi.tuni.tiko.digging.SingleSlideScreen.LEVEL10_AND_EPISODE_COMPLETE;
import static fi.tuni.tiko.digging.SingleSlideScreen.LEVELCOMPLETE;

public class Stage {

    public StageSettings stageSettings;

    //Spike spike;

    //it accepts any tileBasedObjects but it should only contain those that implement Hazard
    ArrayList <TileBasedObject> hazardList = new ArrayList<>();

    ArrayList <GameTile> specialTileList = new ArrayList<>();

    ArrayList <GameTile> vanishingTileList = new ArrayList<>();

    ArrayList <ResourceTile> resourceTileList = new ArrayList<>();

    ArrayList <RootResourceTile> rootResourceTileList = new ArrayList<>();

    LevelStats levelStats;

    SingleSlide singleSlide;

    MainGame mainGame;

    boolean episodeComplete = false;

    int[][] passages;


    /*
    DirtPool dirtPool;
    StonePool stonePool;
    BlankPool blankPool;
    DescendingPool descendingPool;
    PermanentPool permanentPool;

     */

    TilePools tilePools;
    HazardPools hazardPools;

    TileAnimationPools tileAnimationPools;



    /*perhaps easier to handle these if they have their own number in addition to ArraysList index.
     (first level has id 1, second has 2 etc)

     farm level (or test level) could be 0
     */
    private int id;

    //some graphical info in start, maybe some other guidance if this is the first level that something is gonna happen
    private boolean firstTimeVisit;

    private final int MIN_CONDITION = 1;
    private final int MAX_CONDITION = 10;

    ResourceUI resourceUI;

    //many areas are narrower but they cant be wider than this
    //private final int LEVELWIDTH = 7;

    //private float gravity = 0.1f;


    /*
    these could have some other numbers as well. point is, the further game advances, more resources
    there will be (but also more dangers). And the more player gathers resources from one certain level
    less there will be generated them in the future when player starts that SAME level,
      SO, everytime player collects resource, resourcelevel in this stage will be diminished a bit
     */
    private final int MINRESOURCELEVEL = 20;
    private final int MAXRESOURCELEVEL = 10000;

    EntranceTile entranceTile;


    private int resourceLevel;

    //private int resourcesCollectedThisRun;
    private int totalResourcesCollected;

    private GameTexture backGroundTexture;

    boolean[][] levelsPassed;


    GameTile[][] tiles;

    /*tiles(the whole level) consists from one or more tileAreas that have each
    some unique attributes, like width. + what else? ... resources & hazards generated?

    not sure if this is right way to do this, maybe tileArea should be its own class

    nvm tileAreas should be used only when map is generated and after that maybe it doesnt matter
     */
    //GameTile[][] tileArea;


    //oikeasti tätä tarvitaan vain constructorissa tai metodissa kun se luodaan eli ei tarvetta varata muistia korjaan myöhemmin
    //private ArrayList<GameTile[][]> tileAreas;

    //for example 1 = really bad condition, 5 = perfect
    private int condition;

    public int getTotalResourcesCollected () {
        return totalResourcesCollected;
    }

    public void setTotalResourcesCollected (int totalResourcesCollected) {


        this.totalResourcesCollected = totalResourcesCollected;
        if (totalResourcesCollected >= 2550) {
            totalResourcesCollected = 2550;

            if (episodeComplete == false) {
                episodeComplete=true;
            }

        }





        resourceUI.updateLinesToDraw(totalResourcesCollected, 1);




    }
    /*
    public int getResourcesCollectedThisRun () {
        return resourcesCollectedThisRun;
    }

    public void setResourcesCollectedThisRun (int resourcesCollectedThisRun) {
        this.resourcesCollectedThisRun = resourcesCollectedThisRun;
    }*/

    //not to be used except in start of game
    public Stage() {
        tiles = new GameTile[0][0];

        //generateNewMap();
    }

    public Stage (MainGame mainGame) {
        this.tilePools = mainGame.getTilePools();
        this.hazardPools = mainGame.getHazardPools();
        this.levelStats=mainGame.getAllLevelsStats().get(0);
        this.tileAnimationPools=mainGame.getTileAnimationPools();
        this.singleSlide=mainGame.singleSlide;
        this.totalResourcesCollected=mainGame.totalResourcesCollected;
        this.resourceUI = mainGame.resourceUI;
        this.entranceTile = mainGame.entranceTile;

        this.mainGame = mainGame;

        firstTimeVisit = true;

        tiles = new GameTile[0][0];


        this.levelsPassed=mainGame.levelsPassed;


        this.backGroundTexture=mainGame.backGroundTexture;
    }

    /*
    public Stage(int id, int noOfAreas, int condition, int resourceTemplate, TilePools tilePools, HazardPools hazardPools, LevelStats levelStats, TileAnimationPools tileAnimationPools, int totalResourcesCollected, GameTexture backGroundTexture, boolean[][] levelsPassed) {

        this.tilePools = tilePools;
        this.hazardPools = hazardPools;
        this.levelStats=levelStats;
        this.tileAnimationPools=tileAnimationPools;

        this.totalResourcesCollected=totalResourcesCollected;

        this.levelsPassed=levelsPassed;
        firstTimeVisit = true;

        tiles = new GameTile[0][0];

        this.backGroundTexture=backGroundTexture;





        //gravity = 0.01f;

    }*/
/*
    public float getGravity () {
        return gravity;
    }

    public void setGravity (float gravity) {
        this.gravity = gravity;
    }


*/
    public int getId() {
        return id;
    }

    public boolean hasConnectingTexture(int y, int x) {
        if (y >= 0 && y < tiles.length && x >= 0 && x < tiles[y].length) return tiles[y][x].connectingTexture;
        else return true;
    }



    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        if ((condition >= MIN_CONDITION) && (condition <= MAX_CONDITION)) {
            this.condition = condition;
        } else
            throw new IllegalArgumentException("Condition must be between " + MIN_CONDITION + " and" + MAX_CONDITION + ".");
    }

    /*
    Can be used to either add or reduce condition. Will not let the condition go below or above the finals MINCONDITION and MAXCONDITION.

    Important: Remember to use negative value in parameter when you want to reduce the condition.
     */
    public void modifyCondition(int amount) {
        if ((condition - amount) < MIN_CONDITION) {
            condition = MIN_CONDITION;
        } else if ((condition + amount) > MAX_CONDITION) {
            condition = MAX_CONDITION;
        } else {
            setCondition(getCondition() + amount);
        }
    }

    //REMEMBER THERE ARE STILL NEW TEMPLATES AND RANDOMIZERS WHICH might not be good to create everytime, or maybe they are since they will get wiped
    //after this method.
    public void generateNewMap() {

        mainGame.restartLevelAvailable=true;

        if (firstTimeVisit) {
            firstTimeVisit = false;
            doFirstTimeVisitStuff();
        }

        stageSettings = new StageSettings(mainGame.episode, mainGame.level);

        //this will be later made better now just for testing

        // IMPORTANT: NOT SURE IF THIS SHOULD BE CREATED AGAIN EVERY TIME
        StageRandomizer stageRandomizer = new StageRandomizer(tilePools);
        HazardAndResourceRandomizer hazardAndResourceRandomizer = new HazardAndResourceRandomizer(hazardPools);





        tiles = stageRandomizer.areas(stageSettings.getMapTemplate());
        //nää 57,9 myöhemmin varmaan generate newMapin argumenttinä paitsi ei 9, ehkä amount of areas saa nähdä
        //tiles = new GameTile[57][9];

        fillEdgeTiles();



        //randomizeTiles(1, 1, 55, 7);
        //randomizeTiles()

        //toistaiseksi ainakin FarmTilet tulee nyt aina uusiksi
        fillFarmTiles();

        stageRandomizer.addEnding(tiles, tilePools, entranceTile);



        //hazardList.clear();
        hazardList = hazardAndResourceRandomizer.addHazards(hazardList, tiles, stageSettings.getMapTemplate().getHazardTemplate() );



        //tähän väliin check
        stageRandomizer.forceLevelToBePassable(this, mainGame);



        tiles = stageRandomizer.addRoots(tiles);



        //specialTileList.clear();
        addSpecialTilesInList();

        //vanishingTileList.clear();

        if (mainGame.episode <= 3) {
            resourceTileList = hazardAndResourceRandomizer.addResourceTiles(resourceTileList, tiles, tilePools.getResourcePool(), tilePools.getDirtPool(), mainGame);

        } else {
            rootResourceTileList = hazardAndResourceRandomizer.addRootResourceTiles(rootResourceTileList, tiles, tilePools.getRootResourcePool(), tilePools.getDirtPool(), mainGame);
        }

        //resourcesCollectedThisRun=0;



        for (int i = 0; i < tiles.length; i++) {
            for (int k = 0; k < tiles[i].length; k++) {
                if (tiles[i][k].tiling) {
                    tiles[i][k].updateTile(this);
                }
            }
        }



        mainGame.player.setTilePosY(0);
        mainGame.player.setTilePosX(4);

        mainGame.player.setTargetTilePosY(0);
        mainGame.player.setTargetTilePosX(4);

        mainGame.player.confirmChangeInTilePosition();

        //spike = new Spike(3,3);



        //randomizeTiles(1,1,56,7);


    }

    public void doFirstTimeVisitStuff() {

    }

    public void addSpecialTilesInList() {
        for (int y=1; y<tiles.length-1; y++) {
            for (int x =1; x<tiles[0].length-1; x++) {
                if (tiles[y][x] instanceof DescendingDirtTile) {
                    specialTileList.add(tiles[y][x]);
                }
            }
        }
    }


    //puts FarmTiles on top, and 4 rows of dirtTiles under them
    public void fillFarmTiles() {
        for (int x=0; x < TILES_IN_ROWS_INCLUDING_EDGES; x++) {
            FarmTile farm = tilePools.getFarmPool().obtain();
            tiles[0][x] = farm;
            /*
            if (mainGame.level==1) {
                farm.setInPlace(0,x);
            } else {
                farm.setStoneFarmInPlace(0,x);
            }*/
            if (mainGame.farmLevel==true) {
                farm.setInPlace(0,x);
            } else {
                farm.setStoneFarmInPlace(0,x);
            }


        }

        for (int y=1; y<5; y++) {
            for (int x=1; x<TILES_IN_ROWS_INCLUDING_EDGES-1; x++) {
                DirtTile dirt = tilePools.getDirtPool().obtain();
                tiles[y][x]= dirt;
                dirt.setInPlace(y,x);

            }


        }
    }

    //fills edges for the whole table, IMPORTANT, during areas, fillAreaEdges(...) should be used instead
    public void fillEdgeTiles() {
        for (int y=0; y<tiles.length; y++) {
            for (int x=0; x<tiles[0].length; x++) {
                if (x==0 || x==tiles[0].length-1 || y==tiles.length-1) {
                    PermanentTile permanent = tilePools.getPermanentPool().obtain();
                    tiles[y][x] = permanent;
                    permanent.setInPlace(y,x);
                }
            }
        }
    }
    /*
    public void fillAreaEdges(int leftMargin, int rightMargin) {

    }
    */

    /*
    //needs to take account different areas later
    public void randomizeTiles(int startingY, int startingX, int yAmount, int xAmount) {

        //tiles = new GameTile[yAmount][xAmount];

        //this will be done first always i guess, later we will change some of these



        for (int y = startingY; y < yAmount; y++) {
            for (int x = startingX; x < startingX+xAmount; x++) {
                DirtTile dirt = dirtPool.obtain();
                tiles[y][x]= dirt;
                dirt.setInPlace(y,x);
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
    }*/

    //public void breakTile(GameTile tile) {
    //    tile.setConcrete(false);
    //}

    //there could be some exception handling but maybe java's own null-pointer-exception is enough
    public GameTile getTile(int y, int x) {
        return tiles[y][x];
    }

    public void proceedToNextLevel(PlayScreen playScreen) {





            mainGame.level++;
            mainGame.farmLevel=false;


        if (levelsPassed[mainGame.episode-1][mainGame.level-2]==false) {
            levelsPassed[mainGame.episode-1][mainGame.level-2]=true;

            //System.out.println(mainGame.episode);
            //System.out.println(mainGame.level);

            setTotalResourcesCollected(getTotalResourcesCollected()+25*(mainGame.level-1));

            //mainGame.screenHelper.playScreenContinues=false;
            singleSlide.updateSlide(mainGame.episode, mainGame.level-1);
            mainGame.singleSlideScreen.setInfoToShow(LEVELCOMPLETE);
            mainGame.setScreen(mainGame.singleSlideScreen);


        }


        if (mainGame.level==11) {

            mainGame.farmLevel=true;

            if (totalResourcesCollected < 2550) {
                mainGame.singleSlideScreen.setInfoToShow(LEVEL10COMPLETE);
            } else {
                mainGame.singleSlideScreen.setInfoToShow(LEVEL10_AND_EPISODE_COMPLETE);
                mainGame.episode++;
            }

            mainGame.setScreen(mainGame.singleSlideScreen);
            mainGame.level=1;
        }

        startNextLevel();




    }

    public void startNextLevel() {
        mainGame.putAllBackIntoPools();
        generateNewMap();

        if (mainGame.level != 1) {

            makePlayerStartFromEntrance();

        }


    }

    public void makePlayerStartFromEntrance() {

        mainGame.player.setTargetTilePosY(3);
        mainGame.player.setTargetTilePosX(4);
        mainGame.player.confirmChangeInTilePosition();


        tilePools.getDirtPool().free((DirtTile)tiles[3][4]);
        tiles[3][4]=tilePools.getBlankPool().obtain();
        ((BlankTile)tiles[3][4]).setInPlace(3,4);
        ((BlankTile)tiles[3][4]).setEntranceTexture();

    }



    /*
    //get tile from the position (y, x), note starts from 0, not 1
    //ehkä turha metodi jos ei mappi oma classinsa pitää miettiä
    public GameTile getTile(int row, int col) {
        //could have own exception checking

        return tiles[row][col];
    }
    public void drawTile(SpriteBatch batch, int row, int col) {

        GameTile currentTile = getTile(row, col);


    }

    */

    public void draw(SpriteBatch batch) {

        for (int y = 1; y < tiles.length + 8; y = y + 8) {
            for (int x = -2; x < 10; x = x + 4)
                batch.draw(backGroundTexture, (float) x, (float) y, 4f, 8f);
        }


        for (int i = 0; i < hazardList.size(); i++) {
            TileBasedObject hazard = hazardList.get(i);
            if (hazard instanceof Spike) {
                hazard.draw(batch);
            }

        }

        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                tiles[y][x].draw(batch);


                /*
                if (spike.getTilePosY()==y && spike.getTilePosX()==x) {
                    spike.draw(batch);
                }
                */
            }
        }

        for (int i = 0; i < hazardList.size(); i++) {
            TileBasedObject hazard = hazardList.get(i);
            if (!(hazard instanceof Spike)) {
                hazard.draw(batch);
            }


        }

        //for testing purposes
        /*
        for (int y=0; y<tiles.length-1; y++) {
            for (int x=0; x<9; x++) {
                if (passages[y][x]==1) {
                    batch.draw(mainGame.numbers[1], x, y, 0.5f, 0.5f);
                } else if (passages[y][x]==2) {
                    batch.draw(mainGame.numbers[2], x, y, 0.5f, 0.5f);
                } else if (passages[y][x]==3) {
                    batch.draw(mainGame.numbers[3], x, y, 0.5f, 0.5f);
                } else if (passages[y][x]==4) {
                    batch.draw(mainGame.numbers[4], x, y, 0.5f, 0.5f);
                }
            }
        }*/
    }

    public void dispose() {

        for (int i=0; i<hazardList.size(); i++) {
            TileBasedObject hazard = hazardList.get(i);
            hazard.dispose();
        }
        for (int y=0; y<tiles.length; y++) {
            for (int x =0; x<tiles[0].length; x++) {
                tiles[y][x].dispose();
            }
        }
    }
}
