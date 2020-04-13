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
    public void updateMovement (GameTile[][] tiles, float delta) {



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
        }

        if (getStatus() == READY) {


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
            if (getStatus() == READY) {

                if(getTilePosX()==0 || getTilePosX()==TILES_IN_ROWS_INCLUDING_EDGES) {
                    setStatus(DEAD);
                } else if ((tiles[getTilePosY()][getTilePosX()-1].isConcrete() == false) && (tiles[getTilePosY()][getTilePosX()-1].isOccupied() == false) && tiles[getTilePosY()+1][getTilePosX()-1].isConcrete() == true) {
                    int randomResult = MathUtils.random(1,160);
                    if (randomResult <= 1) {
                        startWalking(LEFT);
                    }
                }


            }
            if (getStatus() == READY) {
                //to fight some bugs, still not sure how they end up inside stone tile in the first place
                if(getTilePosX()==0 || getTilePosX()==TILES_IN_ROWS_INCLUDING_EDGES) {
                    setStatus(DEAD);
                } else

                if ((tiles[getTilePosY()][getTilePosX()+1].isConcrete() == false) && (tiles[getTilePosY()][getTilePosX()+1].isOccupied() == false) && tiles[getTilePosY()+1][getTilePosX()+1].isConcrete() == true) {
                    int randomResult = MathUtils.random(1,160);
                    if (randomResult <= 1) {
                        startWalking(RIGHT);
                    }

                }



            }




            //tähän vielä ne random-jutut


        }
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
