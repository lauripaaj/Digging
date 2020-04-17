package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;

import fi.tuni.tiko.digging.util.AnimTools;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_INCLUDING_EDGES;

public class Goblin extends HazardousWalker implements Poolable {

    boolean getsDestroyedByFallingPlayer = true;

    static GameTexture standTexture=(new GameTexture(new Texture("GoblinStand.png")));
    static Texture goblinWalkTexture = new Texture("GoblinWalk.png");
    static Texture goblinFallTexture = new Texture("GoblinFallAnimate.png");

    static Texture goblinZapTexture = new Texture("GoblinZapAnimate.png");

    //strength related to other hazards: 1 will get destroyed by stronger hazards and they will remain
    int hazardStrength=1;

    int checkerEpisode5 = 0;

    float timeUntilChase = 0;
    float timeUntilChaseEnds = 0;


//standTexture = new GameTexture(new Texture("GoblinStand.png"));

    public Goblin(int tilePosY, int tilePosX) {

    //important, sets also texture (see setStandTexture -method)
    //setStandTexture(new GameTexture(new Texture("GoblinStand.png")));
    setStandTexture(standTexture);
    setConcrete(true);
    setWalkingSpeed(1.2f);
    setOriginalFallingSpeed(3.9f);

    setWalkAnimation(new SheetAnimation(goblinWalkTexture, 1, 9, 9, 60));
    setFallAnimation(new SheetAnimation(goblinFallTexture, 1, 15, 5, 60));

    setVanishAnimation(new SheetAnimation(goblinZapTexture, 1, 15, 5, 60));


    rectangle=new Rectangle(1.00f,1.00f, 0.80f, 0.60f);
    putInTilePos(tilePosY, tilePosX);
    }

    @Override
    public int getHazardStrength () {
        return hazardStrength;
    }


    @Override
    public boolean getGetsDestroyedByFallingPlayer () {
        return getsDestroyedByFallingPlayer;
    }

    @Override
    public void vanish () {

        setVanishing(true);
        setConcrete(false);
        setVanishTimeLeft(0.6f);
        setStatus(VANISHING);
    }

    //will be called in MainGames checkHazardHazard-method, related to which hazard:s position
    public void turnAroundAndChangeWalkingDirection(TileBasedObject relatedObject) {
        if (getDirection() == RIGHT) {

            setTargetTilePosX(Math.round(getX()));
            if (getTargetTilePosX()==Math.round(relatedObject.getX()) ) {
                setTargetTilePosX(getTilePosX()-1);
            }
            confirmChangeInTilePosition();
            setStatus(READY);
            //setX(getX()-0.01f);
            //startWalking(LEFT);
            //setDirection(LEFT);
            //setTargetTilePosX(relatedObject.getTilePosX()-1);
            //setTargetTilePosX(getTilePosX());

        } else {
            setTargetTilePosX(Math.round(getX()));
            if (getTargetTilePosX()==Math.round(relatedObject.getX()) ) {
                setTargetTilePosX(getTilePosX()+1);
            }
            confirmChangeInTilePosition();
            setStatus(READY);
            //setX(getX()+0.01f);
            //startWalking(RIGHT);
            //setTargetTilePosX(relatedObject.getTilePosX()+1);
            //setTargetTilePosX(getTilePosX());


        }
        //getWalkAnimation().flipMirror();

    }



    @Override
    public void updateMovement (GameTile[][] tiles, float delta, int episode, Player player) {



        if (getStatus() == VANISHING) {
            boolean actionContinues=true;
            if (getVanishTimeLeft() > 0) {
                continueVanishAnimation(delta);
            } else {
                actionContinues=false;
            }
            if (!actionContinues) {
                setStatus(DEAD);
            }
        //in case goblin is not vanishing and episode is 3 or 4, AND player has descended past goblin, the descending dirt tile will activate
        } else {
            if (episode >= 3 && episode <=4 && player.getTilePosY() > getTilePosY()) {
                if (tiles[getTilePosY()+1][getTilePosX()] instanceof DescendingDirtTile) {
                    DescendingDirtTile tile = (DescendingDirtTile)tiles[getTilePosY()+1][getTilePosX()];
                    if (!tile.isDescending()) {
                        tile.startDescending(delta);
                    }
                }
            /*in case goblin is not vanishing and episode is 5 or 6 and player has descended past goblin, and there is another tile right under the descending tile
                or in case there is no another tile, but a player beneath it, the descending tile will activate */
            } else if (episode >= 5 && player.getTilePosY() > getTilePosY()) {
                boolean startsDescending=false;

                if ( tiles[getTilePosY()+1][getTilePosX()] instanceof DescendingDirtTile ) {


                    if ( (tiles[getTilePosY()+2][getTilePosX()]).isConcrete() ) {
                        startsDescending=true;
                    } else {


                        //we will have to make a checker, so all this won't be checked every frame since it freezes the game
                        if (checkerEpisode5 == 0) {
                            boolean continues = true;
                            checkerEpisode5 = 120;
                            System.out.println("THIS Checker thing starts");
                            for (int yy=getTilePosY()+2, originalY=yy; continues || (yy != tiles.length); yy++) {
                                if ( (player.getTargetTilePosY()==yy) && (player.getTargetTilePosX() == getTilePosX()) ) {
                                    startsDescending = true;
                                    continues = false;
                                } else if (yy>player.getTilePosY() || (originalY+8==yy) ){
                                    continues=false;
                                }
                            }

                        } else checkerEpisode5--;



                    }

                }
                if (startsDescending) {

                    DescendingDirtTile tile = (DescendingDirtTile)tiles[getTilePosY()+1][getTilePosX()];
                    if (!tile.isDescending()) {
                        tile.startDescending(delta);
                    }





                }
            }
        }

        if (getStatus() == READY || getStatus() == PREPARING_TO_CHASE_LEFT || getStatus() == PREPARING_TO_CHASE_RIGHT || getStatus()==WALKING) {
            if(getTilePosX()==0 || getTilePosX()==TILES_IN_ROWS_INCLUDING_EDGES-1) {
                setStatus(DEAD);
            } else


            //TÄRKEÄÄ tää vaatii tarkemman checkin!
            if (tiles[getTilePosY() + 1][getTilePosX()].isConcrete() == false) {
                putInTilePos(getTilePosY(), getTilePosX());
                int amountOfTilesToFall = 1;
                boolean continues = true;
                for (int yy = getTilePosY() + 2; yy < tiles.length && continues; yy++) {
                    if (tiles[yy][getTilePosX()].isConcrete() == false) {
                        amountOfTilesToFall++;
                    } else {
                        continues = false;
                    }
                }
                startFalling(amountOfTilesToFall);
                //System.out.println("Goblin started to FALL " + amountOfTilesToFall + " tiles.");
            }
            //getStatus might have changed so this must be in a new loop even though it's the same condition


            if (getTilePosX() == 0 || getTilePosX() == TILES_IN_ROWS_INCLUDING_EDGES - 1) {
                setStatus(DEAD);
                //this is where reacting to player is implemented! NEW!
            }
        }

        //checking if player is still there, and if they are doing nothing, only this time counts for goblin starting to attack them
        if (getStatus() == PREPARING_TO_CHASE_LEFT) {
            if (player.getTilePosX() < getTilePosX() && player.getTilePosY()==getTilePosY()) {
                timeUntilChase = timeUntilChase - delta;
                if (timeUntilChase <= 0) {
                    startWalking(LEFT);
                    for (int i = 0; i < 15; i++) {
                        //System.out.println("CHASED LEFT");
                    }
                }
            } else {
                timeUntilChaseEnds=timeUntilChaseEnds-delta;
                if (timeUntilChaseEnds <= 0) {
                    setStatus(READY);
                    //System.out.println("giving up the chase left");
                }
            }

        } else if (getStatus() == PREPARING_TO_CHASE_RIGHT) {
            if (player.getTilePosX() > getTilePosX() && player.getTilePosY()==getTilePosY()) {
                timeUntilChase = timeUntilChase-delta;
                if (timeUntilChase <= 0) {
                    startWalking(RIGHT);
                    for(int i=0; i < 15; i++) {
                        //System.out.println("CHASED RIGHT");
                    }
                }

            } else {
                timeUntilChaseEnds=timeUntilChaseEnds-delta;
                if (timeUntilChaseEnds <= 0) {
                    setStatus(READY);
                    //System.out.println("giving up the chase right");
                }
            }

        }


        if (getStatus()== READY) {


            if (player.getTilePosX() == getTilePosX() - 1 && player.getTilePosY() == getTilePosY()) {
                timeUntilChase = 4f / episode;
                timeUntilChaseEnds = 2f;
                setStatus(PREPARING_TO_CHASE_LEFT);
                //System.out.println("preparing chase left");
            } else if (player.getTilePosX() == getTilePosX() + 1 && player.getTilePosY() == getTilePosY()) {
                timeUntilChase = 4f / episode;
                timeUntilChaseEnds = 2f;
                setStatus(PREPARING_TO_CHASE_RIGHT);
                //System.out.println("preparing chase right");
            } else if (((tiles[getTilePosY()][getTilePosX() - 1].isConcrete() == false) && (tiles[getTilePosY()][getTilePosX() - 1].isOccupied() == false) &&
                    (tiles[getTilePosY() + 1][getTilePosX() - 1].isConcrete() == true || (episode >= 5 && player.getTilePosY() > getTilePosY())))) {
                int randomResult = MathUtils.random(1, (170 - (10 * episode)));
                if (randomResult <= 1) {
                    startWalking(LEFT);
                }


            }


            //to fight some bugs, still not sure how they end up inside stone tile in the first place
            if (getTilePosX() == 0 || getTilePosX() == TILES_IN_ROWS_INCLUDING_EDGES - 1) {
                setStatus(DEAD);
            } else if (((tiles[getTilePosY()][getTilePosX() + 1].isConcrete() == false) && (tiles[getTilePosY()][getTilePosX() + 1].isOccupied() == false)
            )
                    &&

                    ((tiles[getTilePosY() + 1][getTilePosX() + 1].isConcrete() == true) || (episode >= 5 && player.getTilePosY() > getTilePosY()))) {
                int randomResult = MathUtils.random(1, (170 - (10 * episode)));
                if (randomResult <= 1) {
                    startWalking(RIGHT);
                }

            }


        }             //tähän vielä ne random-jutut




        //} else if (getStatus() == FALLING) {
            if (getStatus() == FALLING) {
            boolean actionContinues=true;



            if (getTargetGameObjectPosY() <= getY() ) {
                actionContinues=false;
            }
            if (actionContinues) {
                continueFalling(delta);
                checkIfNeedToFallEvenMore(tiles);
            } else {
                setStatus(READY);
                setFallingSpeed(getOriginalFallingSpeed());
                confirmChangeInTilePosition();
            }
            } else if (getStatus() == WALKING) {
                boolean actionContinues=true;

                if (getDirection() == RIGHT && getTargetGameObjectPosX() <= getX() ) {
                    if ( (tiles[getTilePosY()][getTargetTilePosX()+1].isConcrete() == false) && (tiles[getTilePosY()][getTargetTilePosX()+1].isOccupied() == false) &&
                            tiles[getTilePosY()+1][getTargetTilePosX()+1].isConcrete() == true) {

                        putInTilePos(getTargetTilePosY(), getTargetTilePosX());
                        setTargetTilePosX(getTargetTilePosX() + 1);
                        setTargetGameObjectPosX(getTargetGameObjectPosX()+1);
                    } else {
                        actionContinues = false;
                    }


                }
                if (getDirection() == LEFT && getTargetGameObjectPosX() >= getX() ) {
                    if ((tiles[getTilePosY()][getTargetTilePosX() - 1].isConcrete() == false) && (tiles[getTilePosY()][getTargetTilePosX() - 1].isOccupied() == false) &&
                            tiles[getTilePosY() + 1][getTargetTilePosX() - 1].isConcrete() == true) {

                        putInTilePos(getTargetTilePosY(), getTargetTilePosX());
                        setTargetTilePosX(getTargetTilePosX() - 1);
                        setTargetGameObjectPosX(getTargetGameObjectPosX() - 1);
                    } else {
                        actionContinues = false;
                    }
                }

                if (actionContinues) {
                    continueWalking(delta);

                } else {
                    setStatus(READY);
                    confirmChangeInTilePosition();
                }
            }

    }



    //Remember to use putInTilePosition -method when using it again
    @Override
    public void reset () {



        setConcrete(true);
        setWalkingSpeed(1.2f);
        setOriginalFallingSpeed(3.9f);

        getWalkAnimation().resetAnimation();
        getFallAnimation().resetAnimation();
        getVanishAnimation().resetAnimation();

        setStatus(READY);
        setVanishing(false);

        setX(-24);
        setY(-24);


    }
}
