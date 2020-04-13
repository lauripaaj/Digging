package fi.tuni.tiko.digging;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Iterator;

import fi.tuni.tiko.digging.util.Arrays;

import static fi.tuni.tiko.digging.ResourceTile.RESOURCE_SCORE_TIER1;
import static fi.tuni.tiko.digging.ResourceTile.RESOURCE_SCORE_TIER2;
import static fi.tuni.tiko.digging.ResourceTile.RESOURCE_SCORE_TIER3;
import static fi.tuni.tiko.digging.Root.CLOSES;

/*there might be some unnecessary lines here, since I decided to change the way phosphorus doesn't actually start to get rarer
  until the episode changes and with different mechanics.

  The new idea is that in episode 1 there will be 6 resource tiles each level, and every time you start that same level
  in episode 2 5
  and in episode 3 4

  episode 4-6 will get to deal with roots so that needs to be added later

 */




public class HazardAndResourceRandomizer {

    HazardPools hazardPools;
    short[][] resourceTiers = new short[10][];


    public HazardAndResourceRandomizer (HazardPools hazardPools) {
        this.hazardPools = hazardPools;
        //this could be filled with some algorhitm but it's much easier to follow and check back how it actually works when the lines can be seen right away
        //level 1 has only tier 1 resources, level 10 has 2 tier 2 resources and 4 tier 3 resources. In episode 2 the row 6 has run out, and in episode 3,
        //both rows 5 and 6 have run out, so for example, episode 3 level 10 has only 2 tier 2 resources and 2 tier 3 resources, but nothing else.
        resourceTiers[0] = new short[]{1, 1, 1, 1, 1, 1};
        resourceTiers[1] = new short[]{1, 1, 1, 1, 2, 2};
        resourceTiers[2] = new short[]{1, 1, 1, 2, 2, 2};
        resourceTiers[3] = new short[]{1, 1, 2, 2, 2, 2};
        resourceTiers[4] = new short[]{1, 2, 2, 2, 2, 2};
        resourceTiers[5] = new short[]{1, 2, 2, 2, 2, 3};
        resourceTiers[6] = new short[]{2, 2, 2, 2, 2, 3};
        resourceTiers[7] = new short[]{2, 2, 2, 2, 3, 3};
        resourceTiers[8] = new short[]{2, 2, 2, 3, 3, 3};
        resourceTiers[9] = new short[]{2, 2, 3, 3, 3, 3};
    }


    //goes through map (tiles), checks if the tile is something that can hold a hazard (blank?) and then makes a random check if hazard should be added to
    //hazardList
    public ArrayList<TileBasedObject> addHazards (ArrayList<TileBasedObject> hazardList, GameTile[][] tiles, HazardTemplate hazardTemplate) {

        int chanceOfSpike = hazardTemplate.getChanceOfSpike();
        int chanceOfGoblin = hazardTemplate.getChanceOfGoblin();

        int chanceOfFallingTrap = hazardTemplate.getChanceOfFallingTrapOnRoof();

        //going through "roof" tiles, blank tiles with concrete on top and at least blank on bottom

        for (int y = 1; y < tiles.length; y++) {
            for (int x = 1; x < tiles[0].length - 1; x++) {
                if ((tiles[y][x] instanceof BlankTile) && (tiles[y - 1][x].isConcrete()) && (!tiles[y + 1][x].isConcrete())) {
                    int randomResult = MathUtils.random(1, 100);
                    if (randomResult <= chanceOfFallingTrap) {

                        FallingTrap fallingTrap = hazardPools.getFallingTrapPool().obtain();
                        fallingTrap.putInTilePos(y, x);
                        hazardList.add(fallingTrap);
                    }
                }
            }
        }


        //going through all blank tiles for goblins and spikes etc
        for (int y = 1; y < tiles.length; y++) {
            for (int x = 1; x < tiles[0].length - 1; x++) {
                if (tiles[y][x] instanceof BlankTile) {
                    int randomResult = MathUtils.random(1, 100);
                    if (randomResult <= chanceOfSpike) {

                        Spike spike = hazardPools.getSpikePool().obtain();
                        spike.putInTilePos(y, x);
                        hazardList.add(spike);

                    } else if ((randomResult > chanceOfSpike) && (randomResult <= (chanceOfSpike + chanceOfGoblin))) {

                        Goblin goblin = hazardPools.getGoblinPool().obtain();
                        goblin.putInTilePos(y, x);
                        hazardList.add(goblin);

                    }


                }
            }
        }


        return hazardList;
    }

    public ArrayList<RootResourceTile> addRootResourceTiles (ArrayList<RootResourceTile> rootResourceTileList, GameTile[][] tiles, RootResourcePool rootResourcePool, DirtPool dirtPool, MainGame mainGame) {
        rootResourceTileList.clear();

        int[][] passages = mainGame.currentStage.passages;

        rootResourceTileList = addRootResourceTilesWithoutPositions(rootResourceTileList, rootResourcePool, mainGame.episode, mainGame.level);

        //let's try to do this a bit wiser way than the addResourceTiles method below
        ArrayList<GameTile> possiblePlaces = new ArrayList();
        for (int y = 5; y < tiles.length - 1; y++) {
            for (int x = 1; x < tiles[0].length - 1; x++) {


                //we use passage array to our advantage, so the new rootResourceTile that is undiggable cant block the path
                if (tiles[y][x].root != null && tiles[y][x].root.getStatus() == CLOSES && (passages[y][x] == 0)) {
                    possiblePlaces.add(tiles[y][x]);
                }


            }
        }

        /*there might be a case where there is not enough possible places for root tiles. In that case we have to just use them all
        and remove the rootResourceTiles from the list that do not have space
         */
        if (rootResourceTileList.size() > possiblePlaces.size()) {

            int rootsToRemove = rootResourceTileList.size() - possiblePlaces.size();


            //if this sometimes causes a crash, maybe better use that iterator-version
            Iterator<RootResourceTile> it = rootResourceTileList.iterator();
            while (it.hasNext()) {
                RootResourceTile current = it.next();
                System.out.println("going through removeListroot");
                if (rootsToRemove>0) {
                    rootResourcePool.free(current);

                    rootsToRemove--;
                    it.remove();

                }


            }
            /*
            for (int i = 0; i < rootsToRemove; i++) {
                System.out.println("Roots removing frome list");
                rootResourcePool.free(rootResourceTileList.get(i));
                rootResourceTileList.remove(i);
            }*/
        }

        //now that it's certain that there's enough places we can put them in their random places
        int[] chosenTiles = new int[1];
        if (possiblePlaces.size() > rootResourceTileList.size()) {
            chosenTiles = doTheLottery(possiblePlaces.size(), rootResourceTileList.size());
        } else {
            System.out.println("Filling the chosenTIles without randoming");
            //in case there is no randoming needed, we will fill this chosenTiles with just index numbers
            chosenTiles = new int[rootResourceTileList.size()];
            for (int i=0; i<chosenTiles.length; i++) {
                chosenTiles[i]=i;
            }
        }



        Arrays.sort(chosenTiles);
        ArrayList<GameTile> tilesToBePooled = new ArrayList();

        for (int i = 0; i < chosenTiles.length; i++) {
            int xRoot = possiblePlaces.get(chosenTiles[i]).getRawTileX();
            int yRoot = possiblePlaces.get(chosenTiles[i]).getRawTileY();

            //we will move the Root object from the tile to the resourceTile that will replace the other tile
            Root tempRoot = tiles[yRoot][xRoot].root;
            tiles[yRoot][xRoot].root = null;
            //we can't free them to pool just yet, since there would be a reference existing them afterwards in the possiblePlaces arrayList
            tilesToBePooled.add(tiles[yRoot][xRoot]);
            tiles[yRoot][xRoot] = null;

            //RootResourceTile tileToBeAdded = rootResourceTileList.get(i);
            tiles[yRoot][xRoot] = rootResourceTileList.get(i);
            ((RootResourceTile)tiles[yRoot][xRoot]).setInPlace(yRoot, xRoot);
            tiles[yRoot][xRoot].root = tempRoot;

            //we will have to remove possible hazards right now
            ArrayList<TileBasedObject> hazardList = mainGame.currentStage.hazardList;
            Iterator<TileBasedObject> it = hazardList.iterator();


            while (it.hasNext()) {
                TileBasedObject hazard = it.next();
                if (hazard.getTilePosX()==xRoot && hazard.getTilePosY()==yRoot) {
                    if (hazard instanceof Goblin) {
                        mainGame.getHazardPools().getGoblinPool().free( (Goblin)hazard);

                    } else if (hazard instanceof Spike) {
                        mainGame.getHazardPools().getSpikePool().free( (Spike)hazard);
                    } else if (hazard instanceof FallingTrap) {
                        mainGame.getHazardPools().getFallingTrapPool().free( (FallingTrap)hazard);
                    }
                it.remove();
                }
            }

        }

        //let's kill all the references so we can free those possiblePlaces that were added to tilesToBePooled
        possiblePlaces.clear();

        Iterator<GameTile> it = tilesToBePooled.iterator();
        while (it.hasNext()) {
            GameTile tile = it.next();
            if (tile instanceof DirtTile) {
                dirtPool.free((DirtTile) (tile));
            } else if (tile instanceof BlankTile) {
                mainGame.getTilePools().getBlankPool().free((BlankTile) tile);
            } else if (tile instanceof DescendingDirtTile) {
                mainGame.getTilePools().getDescendingPool().free((DescendingDirtTile) tile);
            }
            it.remove();

        }

        return rootResourceTileList;
    }

    //there might be a way to do this with less iterating, but I couldn't find a way to iterate through used dirt Tiles from the dirtPool, only
    //those that'd be free, so tiles-array will be iterated instead

    public ArrayList<ResourceTile> addResourceTiles (ArrayList<ResourceTile> resourceTileList, GameTile[][] tiles, ResourcePool resourcePool, DirtPool dirtPool, MainGame mainGame) {

        //right now there will be just 1 tier3 and 1 tier2 and everything else tier1 resources, later there must be some kind of method to make
        //nice random way to generate all this


        //int amountOfResourcesToBeGenerated = levelStats.getResourceRichnessDuringRuns();


        //this should be done before but just to make it sure it's clear before this...
        resourceTileList.clear();

        resourceTileList = addResourceTilesWithoutPositions(resourceTileList, resourcePool, mainGame.episode, mainGame.level);


        int dirtTileCounter = 0;

        //we will check how many possible places there is for these resources to go into
        for (int y = 6; y < tiles.length; y++) {
            for (int x = 1; x < tiles[0].length - 1; x++) {
                if (tiles[y][x] instanceof DirtTile) {
                    dirtTileCounter++;
                }
            }
        }

        /*We will keep randomizing new numbers, for as long as they will all be unique. They will be the places for resource tiles
         */
        int[] chosenTiles = doTheLottery(dirtTileCounter, resourceTileList.size());

        //System.out.println(Arrays.toString(chosenTiles));
        Arrays.sort(chosenTiles);
        //System.out.println(Arrays.toString(chosenTiles));

        //we will put each tile on its own place based on the newly generated sorted list
        boolean continues = true;
        for (int y = 6, i = 0, dirtCounter = 0; y < tiles.length && continues; y++) {
            for (int x = 1; x < tiles[0].length - 1 && continues; x++) {
                if (tiles[y][x] instanceof DirtTile) {

                    System.out.println("[" + y + "][" + x + "]");

                    dirtCounter++;
                    if (chosenTiles[i] == dirtCounter - 1) {
                        //we will do this to make resourceTile have root that was earlier in dirtTile that is to be replaced
                        Root temp = null;
                        if (tiles[y][x].getRoot() != null) {
                            temp = tiles[y][x].getRoot();
                        }

                        dirtPool.free((DirtTile) tiles[y][x]);

                        ResourceTile resourceTile = resourceTileList.get(i);
                        tiles[y][x] = resourceTile;
                        resourceTile.setInPlace(y, x);

                        if (temp != null) {
                            tiles[y][x].setRoot(temp);
                        }

                        //System.out.println("Added " + i);

                        i++;
                        if (i == chosenTiles.length) {
                            continues = false;
                        }


                    }

                }
            }
        }


        return resourceTileList;
    }

    private int[] doTheLottery (int dirtTileCounter, int numberOfResourceTiles) {

        System.out.println("Starting the lotter, "+dirtTileCounter+", "+numberOfResourceTiles);

        int[] availableDirtPlaces = new int[dirtTileCounter];
        int[] chosenDirtPlaces = new int[numberOfResourceTiles];

        for (int i = 0; i < dirtTileCounter; i++) {
            availableDirtPlaces[i] = i;
        }

        for (int i = 0; i < numberOfResourceTiles; i++) {
            boolean continues = false;
            while (!continues) {
                int lottoIndex = MathUtils.random(0, availableDirtPlaces.length - 1);
                if (!Arrays.contains(availableDirtPlaces[lottoIndex], chosenDirtPlaces)) {
                    chosenDirtPlaces[i] = availableDirtPlaces[lottoIndex];
                    Arrays.removeIndex(availableDirtPlaces, lottoIndex);
                    continues = true;
                }
            }
        }
        System.out.println("reached the end of lottery");
        return chosenDirtPlaces;

    }

    private ArrayList<ResourceTile> addResourceTilesWithoutPositions (ArrayList<ResourceTile> resourceTileList, ResourcePool resourcePool, int episode, int level) {

        //right now there will be just 1 tier3 and 1 tier2 and everything else tier1 resources, later there must be some kind of method to make
        //nice random way to generate all this

        //short noOfTier3 = 0;
        //short noOfTier2 = 0;
        //short noOfTier1 = 0;


        //this is the abandoned idea, putting it in comment brackets
        /*

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

         */






        //case of episode 1
        int modifier = 0;
        if (episode == 2) {
            modifier = -1;
        } else if (episode == 3) {
            modifier = -2;
        }


        for (int i = 0; i < 6 + modifier; i++) {
            ResourceTile resourceTile = resourcePool.obtain();
            resourceTile.makeThisTier(resourceTiers[level - 1][i]);
            resourceTileList.add(resourceTile);

        }



    /*
            //adding tier3:s to the list
            for (int i = 0; i < noOfTier3; i++) {
                ResourceTile resourceTile = resourcePool.obtain();
                resourceTile.makeThisTier(3);
                resourceTileList.add(resourceTile);
            }
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
        }*/

        return resourceTileList;
    }


    private ArrayList<RootResourceTile> addRootResourceTilesWithoutPositions (ArrayList<RootResourceTile> rootResourceTileList, RootResourcePool rootResourcePool, int episode, int level) {




        int modifier = 0;

        if (episode == 5) {
            modifier = -1;
        } else if (episode == 6) {
            modifier = -2;
        }

        for (int i = 0; i < 6 + modifier; i++) {
            RootResourceTile rootResourceTile = rootResourcePool.obtain();
            rootResourceTile.makeThisTier(resourceTiers[level - 1][i]);
            rootResourceTileList.add(rootResourceTile);

        }

        return rootResourceTileList;


    }
}
