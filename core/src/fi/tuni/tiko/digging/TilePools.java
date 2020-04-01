package fi.tuni.tiko.digging;

//NOTE: doesn't contain resourcePool at least now, have to think if it should
public class TilePools {

    private DirtPool dirtPool;
    private StonePool stonePool;
    private BlankPool blankPool;
    private DescendingPool descendingPool;
    private PermanentPool permanentPool;
    private FarmPool farmPool;
    private ResourcePool resourcePool;
    private RootPool rootPool;

    public TilePools(DirtPool dirtPool, StonePool stonePool, BlankPool blankPool, DescendingPool descendingPool, PermanentPool permanentPool, FarmPool farmPool, ResourcePool resourcePool, RootPool rootPool) {
        this.dirtPool = dirtPool;
        this.stonePool = stonePool;
        this.blankPool = blankPool;
        this.descendingPool = descendingPool;
        this.permanentPool = permanentPool;
        this.farmPool = farmPool;
        this.resourcePool = resourcePool;
        this.rootPool = rootPool;
    }

    public DirtPool getDirtPool () {
        return dirtPool;
    }

    public void setDirtPool (DirtPool dirtPool) {
        this.dirtPool = dirtPool;
    }

    public StonePool getStonePool () {
        return stonePool;
    }

    public void setStonePool (StonePool stonePool) {
        this.stonePool = stonePool;
    }

    public BlankPool getBlankPool () {
        return blankPool;
    }

    public void setBlankPool (BlankPool blankPool) {
        this.blankPool = blankPool;
    }

    public DescendingPool getDescendingPool () {
        return descendingPool;
    }

    public void setDescendingPool (DescendingPool descendingPool) {
        this.descendingPool = descendingPool;
    }

    public PermanentPool getPermanentPool () {
        return permanentPool;
    }

    public void setPermanentPool (PermanentPool permanentPool) {
        this.permanentPool = permanentPool;
    }

    public RootPool getRootPool () {
        return rootPool;
    }

    public void setRootPool (RootPool rootPool) {
        this.rootPool = rootPool;
    }

    public FarmPool getFarmPool () {
        return farmPool;
    }

    public void setFarmPool (FarmPool farmPool) {
        this.farmPool = farmPool;
    }

    public ResourcePool getResourcePool () {
        return resourcePool;
    }

    public void setResourcePool (ResourcePool resourcePool) {
        this.resourcePool = resourcePool;
    }

    //all GameTiles are being put back to their pools. It is very important to do this when the
    //stage is reset or finished, since LibGDX Pools do not do this automatically and it would cause
    //pools filling, memory problems or crahses either right away or later

    //some of these tiles will have a reference in ArrayLists like SpecialTileList in Stage, so they must be removed too but this method doesn't do it

    // FARM TILES ARE NOT BEING FREED SINCE THEY DO NOT HAVE THEIR OWN POOL AT LEAST YET
    public void putAllTilesIntoPools (GameTile[][] tiles) {

        for (int x=0; x<tiles[0].length; x++) {
            //bug saying blank tile cant be put in the farm pool is caused by using "a"cheat (gets rid of lots of tiles), will not be in the final game
            farmPool.free((FarmTile)tiles[0][x]);
        }

        //putting all roots in pool BEFORE any tiles are put there, so they can be freed normally
        for (int y=1; y<tiles.length-1; y++) {
            for (int x=0; x<tiles[0].length; x++) {
                if (tiles[y][x].getRoot() != null) {
                    rootPool.free(tiles[y][x].getRoot());
                }
            }
        }


        for(int y=1; y<tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                if (x == 0 || x==tiles[0].length-1 || y==tiles.length-1) {
                    permanentPool.free((PermanentTile)tiles[y][x]);


                } else {
                    if (tiles[y][x] instanceof DirtTile) {
                        dirtPool.free((DirtTile)tiles[y][x]);

                    } else if (tiles[y][x] instanceof StoneTile) {
                        stonePool.free((StoneTile)tiles[y][x]);

                    } else if (tiles[y][x] instanceof BlankTile) {
                        blankPool.free((BlankTile)tiles[y][x]);

                    } else if (tiles[y][x] instanceof DescendingDirtTile) {
                        descendingPool.free((DescendingDirtTile)tiles[y][x]);

                    } else if (tiles[y][x] instanceof ResourceTile) {
                        resourcePool.free((ResourceTile)tiles[y][x]);
                    }

                }
                tiles[y][x]=null;
            }
        }

        //TÄHÄN TARVITAAN ROOTIT
    }
}
