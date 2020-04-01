package fi.tuni.tiko.digging;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

import fi.tuni.tiko.digging.util.Arrays;

import static fi.tuni.tiko.digging.ResourceTile.RESOURCE_SCORE_TIER1;
import static fi.tuni.tiko.digging.ResourceTile.RESOURCE_SCORE_TIER2;
import static fi.tuni.tiko.digging.ResourceTile.RESOURCE_SCORE_TIER3;


public class HazardAndResourceRandomizer {

    HazardPools hazardPools;

    public HazardAndResourceRandomizer(HazardPools hazardPools) {
        this.hazardPools=hazardPools;
    }




    //goes through map (tiles), checks if the tile is something that can hold a hazard (blank?) and then makes a random check if hazard should be added to
    //hazardList
    public ArrayList<TileBasedObject> addHazards(ArrayList<TileBasedObject> hazardList, GameTile[][] tiles, HazardTemplate hazardTemplate ) {

        int chanceOfSpike=hazardTemplate.getChanceOfSpike();
        int chanceOfGoblin=hazardTemplate.getChanceOfGoblin();

        int chanceOfFallingTrap=hazardTemplate.getChanceOfFallingTrapOnRoof();

        //going through "roof" tiles, blank tiles with concrete on top and at least blank on bottom

        for (int y=1; y<tiles.length; y++) {
            for (int x=1; x<tiles[0].length-1; x++) {
                if ( (tiles[y][x] instanceof BlankTile) && (tiles[y-1][x].isConcrete() ) && (!tiles[y+1][x].isConcrete() )) {
                    int randomResult = MathUtils.random(1,100);
                    if (randomResult <= chanceOfFallingTrap) {

                        FallingTrap fallingTrap = hazardPools.getFallingTrapPool().obtain();
                        fallingTrap.putInTilePos(y, x);
                        hazardList.add(fallingTrap);
                    }
                }
            }
        }


        //going through all blank tiles for goblins and spikes etc
        for (int y=1; y<tiles.length; y++) {
            for (int x=1; x<tiles[0].length-1; x++) {
                if (tiles[y][x] instanceof BlankTile) {
                    int randomResult = MathUtils.random(1,100);
                    if (randomResult <= chanceOfSpike) {

                        Spike spike = hazardPools.getSpikePool().obtain();
                        spike.putInTilePos(y, x);
                        hazardList.add(spike);

                    } else if (( randomResult > chanceOfSpike) && (randomResult <= (chanceOfSpike+chanceOfGoblin)) ) {

                        Goblin goblin = hazardPools.getGoblinPool().obtain();
                        goblin.putInTilePos(y, x);
                        hazardList.add(goblin);

                    }


                }
             }
        }



        return hazardList;
    }

    //there might be a way to do this with less iterating, but I couldn't find a way to iterate through used dirt Tiles from the dirtPool, only
    //those that'd be free, so tiles-array will be iterated instead

    public ArrayList<ResourceTile> addResourceTiles (ArrayList<ResourceTile> resourceTileList, GameTile[][] tiles, ResourcePool resourcePool, LevelStats levelStats, DirtPool dirtPool) {

        //right now there will be just 1 tier3 and 1 tier2 and everything else tier1 resources, later there must be some kind of method to make
        //nice random way to generate all this

        int amountOfResourcesToBeGenerated = levelStats.getResourceRichnessDuringRuns();

        //this should be done before but just to make it sure it's clear before this...
        resourceTileList.clear();

        resourceTileList = addResourceTilesWithoutPositions(resourceTileList, resourcePool, amountOfResourcesToBeGenerated);


        int dirtTileCounter=0;

        //we will check how many possible places there is for these resources to go into
        for (int y=1; y<tiles.length; y++) {
            for (int x=1; x<tiles[0].length-1; x++) {
                if (tiles[y][x] instanceof DirtTile) {
                    dirtTileCounter++;
                }
            }
        }

        /*We will keep randomizing new numbers, for as long as they will all be unique. They will be the places for resource tiles
         */
        int[] chosenTiles = doTheLottery(dirtTileCounter, resourceTileList.size());

        System.out.println(Arrays.toString(chosenTiles));
        Arrays.sort(chosenTiles);
        System.out.println(Arrays.toString(chosenTiles));

        //we will put each tile on its own place based on the newly generated sorted list
        boolean continues = true;
        for (int y=1, i=0, dirtCounter=0 ; y<tiles.length && continues; y++ ) {
            for (int x = 1; x < tiles[0].length-1 && continues; x++) {
                if (tiles[y][x] instanceof DirtTile) {

                    System.out.println("["+y+"]["+x+"]");

                    dirtCounter++;
                    if (chosenTiles[i] == dirtCounter-1) {
                        //we will do this to make resourceTile have root that was earlier in dirtTile that is to be replaced
                        Root temp = null;
                        if (tiles[y][x].getRoot() != null) {
                            temp=tiles[y][x].getRoot();
                        }

                        dirtPool.free((DirtTile)tiles[y][x]);

                        ResourceTile resourceTile = resourceTileList.get(i);
                        tiles[y][x] = resourceTile;
                        resourceTile.setInPlace(y,x);

                        if (temp != null) {
                            tiles[y][x].setRoot(temp);
                        }

                        System.out.println("Added "+i);

                        i++;
                        if (i==chosenTiles.length) {
                            continues=false;
                        }




                    }

                }
            }
        }




        /*

        int[] freeDirtPlaces = new int[dirtTileCounter];
        int[] chosenDirtPlaces = new int[resourceTileList.size()];

        for (int i=0; i<freeDirtPlaces.length; i++) {
            freeDirtPlaces[i]=i;
        }

        for (int i=0; i<resourceTileList.size(); i++) {
            int randomedPlace = MathUtils.random(0, freeDirtPlaces.length);
            if (!Arrays.contains(randomedPlace, freeDirtPlaces)) {
                Arrays.removeIndex(freeDirtPlaces, randomedPlace);

            }
        }
        */







        return resourceTileList;
    }

    private int[] doTheLottery (int dirtTileCounter, int numberOfResourceTiles) {

        int[] availableDirtPlaces = new int[dirtTileCounter];
        int[] chosenDirtPlaces = new int[numberOfResourceTiles];

        for (int i = 0; i<dirtTileCounter; i++) {
            availableDirtPlaces[i]=i;
        }

        for (int i=0; i<numberOfResourceTiles; i++) {
            boolean continues=false;
            while (!continues) {
                int lottoIndex = MathUtils.random(0,availableDirtPlaces.length-1);
                if (!Arrays.contains(availableDirtPlaces[lottoIndex], chosenDirtPlaces)) {
                    chosenDirtPlaces[i]=availableDirtPlaces[lottoIndex];
                    Arrays.removeIndex(availableDirtPlaces, lottoIndex);
                    continues=true;
                }
            }
        }

        return chosenDirtPlaces;

    }

    private ArrayList<ResourceTile> addResourceTilesWithoutPositions (ArrayList<ResourceTile> resourceTileList, ResourcePool resourcePool, int amountOfResourcesToBeGenerated) {

        //right now there will be just 1 tier3 and 1 tier2 and everything else tier1 resources, later there must be some kind of method to make
        //nice random way to generate all this

        short noOfTier3 = 0;
        short noOfTier2 = 0;
        short noOfTier1 = 0;

        if (amountOfResourcesToBeGenerated > (RESOURCE_SCORE_TIER3 + RESOURCE_SCORE_TIER2 + (RESOURCE_SCORE_TIER1 * 2))) {

            System.out.println("there will be one tier3, one tier 2, and rest tier 1");

            amountOfResourcesToBeGenerated = amountOfResourcesToBeGenerated - RESOURCE_SCORE_TIER3;
            noOfTier3++;

            amountOfResourcesToBeGenerated = amountOfResourcesToBeGenerated - RESOURCE_SCORE_TIER2;
            noOfTier2++;

            noOfTier1=(short)(amountOfResourcesToBeGenerated/RESOURCE_SCORE_TIER1);

        } else {

            System.out.println("there will be only tier 1 ones");

            noOfTier1=(short)(amountOfResourcesToBeGenerated/RESOURCE_SCORE_TIER1);
        }

        //adding tier3:s to the list
        for (int i=0; i<noOfTier3; i++) {
            ResourceTile resourceTile = resourcePool.obtain();
            resourceTile.makeThisTier(3);
            resourceTileList.add(resourceTile);
         }
        //adding tier2:s to the list
        for (int i=0; i<noOfTier2; i++) {
            ResourceTile resourceTile = resourcePool.obtain();
            resourceTile.makeThisTier(2);
            resourceTileList.add(resourceTile);
        }
        //adding tier1:s to the list
        for (int i=0; i<noOfTier1; i++) {
            ResourceTile resourceTile = resourcePool.obtain();
            resourceTile.makeThisTier(1);
            resourceTileList.add(resourceTile);
        }

        return resourceTileList;
    }
}
