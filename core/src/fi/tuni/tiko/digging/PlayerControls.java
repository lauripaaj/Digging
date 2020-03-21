package fi.tuni.tiko.digging;

import static fi.tuni.tiko.digging.MainGame.TILES_IN_ROWS_INCLUDING_EDGES;

import static fi.tuni.tiko.digging.Player.LEFT;
import static fi.tuni.tiko.digging.Player.READY;
import static fi.tuni.tiko.digging.Player.RIGHT;

public class PlayerControls {

    //remember to update checkQueue-method below if you add possible queu actions
    public static int NOQUEU = 0;
    public static int TRYRIGHT = 6;
    public static int TRYLEFT = 4;
    public static int TRYUP = 8;
    public static int TRYDOWN = 2;



    int queu = NOQUEU;

    public int getQueu () {
        return queu;
    }

    public void setQueu (int queu) {
        this.queu = queu;

    }

    public void checkQueu(Player player, Stage currentStage) {
        if (queu==TRYRIGHT) {
            tryRight(player, currentStage);
            System.out.println("tryingtosetrightque");
        } else if (queu==TRYLEFT) {
            tryLeft(player, currentStage);
        } else if (queu==TRYDOWN) {
            tryDown(player, currentStage);
        } else if (queu==TRYUP) {
            tryUp(player, currentStage);
        }

        queu=NOQUEU;
        System.out.println("setQueuTo0");

    }

/*
public void controlPlayer(Player player, Stage stage) {
    int targetX = player.getTilePosX();
    int targetY = player.getTilePosY();


}*/

    public void tryRight(Player player, Stage currentStage) {
        int targetX = player.getTilePosX()+1;
        int targetY = player.getTilePosY();




        if (currentStage.getTile(targetY, targetX).isConcrete() == false) {

            //we will go through hazard list to see if attacking would be the right thing to do
            //IMPORTANT targetTilePos might be needed to adjust later (if the player attacks even
            //though enemy is far away becoming in that position much later

            for (TileBasedObject hazard : currentStage.hazardList) {
                if ( ( targetX==hazard.getTilePosX() && targetY==hazard.getTilePosY() )
                ||   ( targetX==hazard.getTargetTilePosX() && targetY==hazard.getTilePosY() )
                        //this will try to help attacking in 2 tiles away in advance, we'll see if it works as intended
                ||   ( ((targetX+1)==hazard.getTargetTilePosX() && targetY==hazard.getTilePosY()) && (hazard.getTargetTilePosX() != hazard.getTilePosX()) )
                ) {
                    if (!hazard.isVanishing()) {
                        player.startAttacking(RIGHT);
                    }

                }
            }
            //checking if player didnt start to attack so they are still in ready status
            if (player.getStatus()==READY) {
                player.startWalking(RIGHT);
            }


        } else if (currentStage.getTile(targetY, targetX).isDiggable() ) {
            player.startBreaking(RIGHT);
            //currentStage.getTile(targetY, targetX).vanish();
            currentStage.getTile(targetY, targetX).startVanishing(currentStage);
            System.out.println("VANISHED RIGHT");
            if (currentStage.getTile(targetY, targetX) instanceof ResourceTile) {
                doResourceGainedThings((ResourceTile)currentStage.getTile(targetY, targetX), currentStage.tileAnimationPools, currentStage);
            }
        }

    }

    public void tryLeft(Player player, Stage currentStage) {
        int targetX = player.getTilePosX()-1;
        int targetY = player.getTilePosY();


        if (currentStage.getTile(targetY, targetX).isConcrete() == false) {
            for (TileBasedObject hazard : currentStage.hazardList) {
                if ( ( targetX==hazard.getTilePosX() && targetY==hazard.getTilePosY() )
                        ||   ( targetX==hazard.getTargetTilePosX() && targetY==hazard.getTilePosY() )
                        //this will try to help attacking in 2 tiles away in advance, we'll see if it works as intended
                        ||   ( (targetX-1)==hazard.getTargetTilePosX() && targetY==hazard.getTilePosY() && (hazard.getTargetTilePosX() != hazard.getTilePosX()))
                ) {

                    if (!hazard.isVanishing()) {
                        player.startAttacking(LEFT);
                    }

                }
            }
            //checking if player didnt start to attack so they are still in ready status
            if (player.getStatus()==READY) {
                player.startWalking(LEFT);
            }
        } else if (currentStage.getTile(targetY, targetX).isDiggable() ) {
            player.startBreaking(LEFT);
            currentStage.getTile(targetY, targetX).startVanishing(currentStage);
            System.out.println("VANISHEDLEFT");
            if (currentStage.getTile(targetY, targetX) instanceof ResourceTile) {
                doResourceGainedThings((ResourceTile)currentStage.getTile(targetY, targetX), currentStage.tileAnimationPools, currentStage);
            }


        }

    }

    public void tryDown(Player player, Stage currentStage) {
        int targetX = player.getTilePosX();
        int targetY = player.getTilePosY()+1;

        if (currentStage.getTile(targetY, targetX).isDiggable() ) {
            player.startDigging();
            System.out.println("started to dig");
            //currentStage.getTile(targetY, targetX).vanish();
            currentStage.getTile(targetY, targetX).startVanishing(currentStage);
            if (currentStage.getTile(targetY, targetX) instanceof ResourceTile) {
                doResourceGainedThings((ResourceTile)currentStage.getTile(targetY, targetX), currentStage.tileAnimationPools, currentStage);
            }

        }
    }

    public void tryUp(Player player, Stage currentStage) {
        int targetX = player.getTilePosX();
        int targetY = player.getTilePosY()-1;

        if(targetY > 0) {
            if (currentStage.getTile(targetY, targetX).isDiggable() &&
                    currentStage.getTile( targetY, targetX).isConcrete() ) {
                player.startBreakingRoof();
                System.out.println("breaking roof");
                currentStage.getTile(targetY, targetX).startVanishing(currentStage);
                if (currentStage.getTile(targetY, targetX) instanceof ResourceTile) {
                    doResourceGainedThings((ResourceTile)currentStage.getTile(targetY, targetX), currentStage.tileAnimationPools, currentStage);
                }

            }
        }
    }

    //for testing purposes, not supposed to stay in the final version
    public void cheatDown(Player player, Stage currentStage) {
        for (int y = player.getTilePosY(); y<player.getTilePosY()+7; y++) {
            currentStage.tiles[y][player.getTilePosX()]=new BlankTile(y, player.getTilePosX() );
        }
    }

    public void doResourceGainedThings(ResourceTile resourceTile, TileAnimationPools tileAnimationPools, Stage currentStage) {
        ResourceGainedAnimation resourceGainedAnimation = tileAnimationPools.getResourceAnimationPool().obtain();
        resourceGainedAnimation.setX(resourceTile.getX());
        resourceGainedAnimation.setY(resourceTile.getY());
        resourceGainedAnimation.startAnimation();

        tileAnimationPools.getResourceAnimationList().add(resourceGainedAnimation);

        currentStage.setResourcesCollectedThisRun(currentStage.getResourcesCollectedThisRun()+resourceTile.getResourceScore());
        currentStage.setTotalResourcesCollected(currentStage.getTotalResourcesCollected()+resourceTile.getResourceScore());

        System.out.println("Resources collected during current run: "+currentStage.getResourcesCollectedThisRun());
        System.out.println("Resources collected in total: "+currentStage.getTotalResourcesCollected());


    }




}
