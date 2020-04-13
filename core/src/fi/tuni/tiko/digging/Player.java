package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


//voisi tehdä niin että implementoi interfacen "walking" tms, jossa hoidetaan liikkuminen(ei ohjausta vaan liikkuminen myös vihollisille saman interfacen kautta), ehkä tippuminen samassa hmm
public class Player extends Creature {


    /*varmaan parempi seurata playerin positiota tällaisen kautta jos se aina voi olla vaan tilen keskellä muulloin kuin liikkeessä
    tai toimiessa. Pitää vaan pitää huolta että tämä tulee aina updatettua metodeissa

    ja että tarkistaessa esim RefreshTilePos() tää saadaan koordinaateista jollain kaavalla.

    En nyt oo varma miten tämä kannattaisi olio-ohjelmallisesti tehdä, että Stagen koko level
    on aina playerin käytössä ja ajan tasalla. Lähetetäänkö viite Stageen joka ikisellä render-framella
    tälle, tekeekö se ohjelmasta raskaan vai eikö se kuormita lainkaan

*/
    //hmm pitää miettiä tää koko juttu että tuleeko kaikille mammaleille tms ainakin nää, ei ehkä kaikille gameobjecteille


    GameTexture texture;

    private int stageCurrentlyIn;




    private SheetAnimation walkAnimation;
    private SheetAnimation digAnimation;
    private SheetAnimation breakAnimation;
    private SheetAnimation breakRoofAnimation;
    private SheetAnimation fallAnimation;

    private SheetAnimation attackAnimation;
    private SheetAnimation attackUpwardsAnimation;

    public static int READY = 0;
    public static int FALLING = 1;
    public static int WALKING = 2;
    public static int BREAKING = 1123;
    public static int BREAKINGROOF = 1125;
    public static int DIGGING = 1124;
    public static int ATTACKING = 1128;

    private int status = READY;

    private float breakingRemaining;

    private float maximumAttackForthAndBackMove = 0.80f;
    private float attackForthAndBackMoveRemaining;
    private float attackWidth=1.0f;
    private float normalWidth=0.98f;


    //private float gravityPull = 1.0f;


    private float attackRange = 1.0f;

    private float attackSpeed = 3.35f;

    private float maximumWalkingSpeed=2.8f;
    private float walkingSpeed = 2.65f;
    private float diggingSpeed = 2.15f;
    private float breakingSpeed = 2.15f;

    private final float ORIGINALFALLINGSPEED = 3.9f;

    private float fallingSpeed = ORIGINALFALLINGSPEED;





    //boolean riittääkin eli ei tarvita front-directionia, koska isWalking, inAir jne jotka voi tsekata
    //eikun ylös ja alas


    public static boolean LEFT = false;
    public static boolean RIGHT = true;

    //public static int UP = 8;
    //public static int DOWN =2;

    //private boolean direction = LEFT;
    private boolean breakingDirection = LEFT;

    private boolean attackDirection = LEFT;

    public boolean getAttackDirection () {
        return attackDirection;
    }

    //public boolean isWalking () {
    //    return walking;
    //}

    //public void setWalking (boolean walking) {
    //    this.walking = walking;
    //}
    public void setStatus (int status) {
        this.status=status;
    }
    public int getStatus () {
        return status;
    }

    public int getStageCurrentlyIn () {
        return stageCurrentlyIn;
    }

    public void setStageCurrentlyIn (int stageCurrentlyIn) {
        this.stageCurrentlyIn = stageCurrentlyIn;
    }

    public float getAttackRange () {
        return attackRange;
    }

    public void setAttackRange (float attackRange) {
        this.attackRange = attackRange;
    }

    //public boolean isDigging () {
    //    return digging;
    //}
/*
    public void setDigging (boolean digging) {
        this.digging = digging;
    }

*/
    public float getWalkingSpeed () {
        return walkingSpeed;
    }

    public void setWalkingSpeed (float walkingSpeed) {
        this.walkingSpeed = walkingSpeed;
    }

    public float getDiggingSpeed () {
        return diggingSpeed;
    }

    public void setDiggingSpeed (float diggingSpeed) {
        this.diggingSpeed = diggingSpeed;
    }
/*
    public boolean isBreaking () {
        return breaking;
    }

    public void setBreaking (boolean breaking) {
        this.breaking = breaking;
    }
*/
    //public void setGravityPull (float gravityPull) {
      //  this.gravityPull = gravityPull;
    //}



    //isReady doesnt have attribute of its own so this is not a traditional getter.
    //if player is not doing anything else (walking, digging, falling, they are ready to take orders from
    //player keyboard
/*    public boolean isReady() {
        return !(inAir || walking || digging || breaking);
    }
*/

    @Override
    public void confirmChangeInTilePosition() {
        super.confirmChangeInTilePosition();
        rectangle.setWidth(normalWidth);

    }

    public void testXandY() {
        //System.out.println("Current ObjectPosX, ObjectPosY: "+getX()+", "+getY());
        //System.out.println("Current TilePosX, TilePosY: "+getTilePosX()+", "+getTilePosY());
        //System.out.println("Test the heightbased thing x y: "+Math.round(getHeightAdjustedY()));
        //System.out.println("Current RawX, RawY: "+getRawTileX()+", "+getRawTileY());

        //System.out.println(TableTools.getRealTileX(this));
    }





    public Player() {

        //texture =

        texture = new GameTexture((new Texture("playerStand.png")));


        //setTexture(new Texture("playerStand2.png"));
        setTexture(texture);



        walkAnimation = new SheetAnimation(new Texture("playerWalk.png"), 1, 9, 9, 60);
        digAnimation = new SheetAnimation(new Texture("playerDrillAnimate.png"), 1, 15, 5, 60);
        breakAnimation = new SheetAnimation(new Texture("playerBreakAnimate.png"), 1, 15, 5, 60);
        breakRoofAnimation = new SheetAnimation(new Texture("playerBreakRoofAnimate.png"), 1, 15 , 5, 60);
        fallAnimation = new SheetAnimation(new Texture("playerFallAnimate.png"), 1, 15, 5, 60);
        attackAnimation = new SheetAnimation(new Texture("playerAttackAnimate.png"), 1, 7, 7, 60);

        //täytyy muistaa että confirmChangeInTilePositionissa on rectangleSetWidth(1.0f)
        rectangle = new Rectangle(1.0f, 1.0f, texture.getTexture().getWidth()/355f, texture.getTexture().getHeight()/355f);

        //System.out.println(getWidth());

        //setTilePosX(0);
        //setTilePosY(0);
        //confirmChangeInTilePosition();

        putInTilePos(0,4);

        setStatus(READY);
        fallingSpeed=ORIGINALFALLINGSPEED;










    }
    //needs work
    public void getZapped() {
        fallingSpeed=ORIGINALFALLINGSPEED;
        putInTilePos(0,4);
        setStatus(READY);
        //not sure yet if this method is complete, probably not
    }
    public void continueFalling(float delta) {
        fallingSpeed=fallingSpeed*1.01f;
        setY(getY()+(delta*fallingSpeed));
        fallAnimation.continueAnimation(delta);
    }
    public void startFalling(int noOfTiles) {
        setStatus(FALLING);
        fallAnimation.resetAnimation();

        setTargetGameObjectPosY(getY()+noOfTiles);
        setTargetTilePosY(getTilePosY()+noOfTiles);
    }

    //player will go back and forth performing an attack
    public void continueAttacking(float delta) {
        float moveSpeed;

        if (attackDirection == RIGHT) {
            moveSpeed = delta * attackSpeed;
        } else {
            moveSpeed = -delta * attackSpeed;}

        if (attackForthAndBackMoveRemaining >= (maximumAttackForthAndBackMove/2) ) {
            setX(getX()+moveSpeed);
            } else {
            setX(getX()-moveSpeed);
            }
        attackForthAndBackMoveRemaining=attackForthAndBackMoveRemaining-Math.abs(moveSpeed);
        attackAnimation.continueAnimation(delta);
    }

    public void startAttacking(boolean attackDirection) {
        if (this.attackDirection != attackDirection) {
            this.attackDirection = attackDirection;
            attackAnimation.flipMirror();
        }

        setStatus(ATTACKING);
        attackAnimation.resetAnimation();

        attackForthAndBackMoveRemaining = maximumAttackForthAndBackMove;

        rectangle.setWidth(attackWidth);

        /*
        if(attackDirection==LEFT) {
            setX(getX()-attackWidthMultiplier/6);
        }*/

        //to prevent attacking 2 tile range to right
        /*
        if(attackDirection==RIGHT) {
            setX(getX()-attackWidth/5);
        }*/




    }

    public void continueDigging(float delta) {

        setY(getY()+(delta*diggingSpeed));
        digAnimation.continueAnimation(delta);
        //System.out.println("continuing digging happens");

    }


    public void startDigging() {
        setStatus(DIGGING);
        digAnimation.resetAnimation();

        setTargetGameObjectPosY(getY()+1);
        setTargetTilePosY(getTilePosY()+1);


    }

    public void continueBreaking(float delta) {

        /*
        float moveAmount=delta*breakingSpeed;
        if (breakingDirection==RIGHT) {
            setX(getX()+moveAmount);
        } else {
            setX(getX()-moveAmount);
        }*/


        breakingRemaining=breakingRemaining-delta*breakingSpeed;
        breakAnimation.continueAnimation(delta);
    }

    public void continueBreakingRoof(float delta) {
        breakingRemaining=breakingRemaining-delta*breakingSpeed;
        breakRoofAnimation.continueAnimation(delta);
    }

    public void startBreakingRoof() {
        setStatus(BREAKINGROOF);
        breakRoofAnimation.resetAnimation();

        breakingRemaining=1.0f;
    }

    //if some tiles will take longer to break than others(pettävä tile? vähemmän), they should
    //probably give out the time here as parameter to put in breakingRemaining. other option
    //would be to have every tile to have "health/duration" left, but it would maybe be heavier on the resources
    //if we want to take older phones into account. Hard to know yet how heavy this will be on resources in general.
    public void startBreaking(boolean breakingDirection) {

        if (this.breakingDirection != breakingDirection) {
            this.breakingDirection = breakingDirection;
            breakAnimation.flipMirror();
        }

            setStatus(BREAKING);
            breakAnimation.resetAnimation();




            breakingRemaining=1.0f;

        //1.0f on aina tilen leveys, siitä on lähdetty, voisi tulla kyllä parametrinä jos se muuttuu
            /*
            if (breakingDirection == RIGHT) {
                targetGameObjectPosX = getX() + 1.0f;
                setTargetTilePosX(getTilePosX()+1);
                //targetTilePosX = tilePosX + 1;

            } else {
                targetGameObjectPosX = getX() - 1.0f;
                setTargetTilePosX(getTilePosX()-1);


            }*/
    }


    public void continueWalking(float delta) {

        //tätä kerrointa muuttamalla hidastusta enemmän tai vähemmän, (tai sitten maxmiumwalkingspeediä)
        walkingSpeed=walkingSpeed*0.98f;

        float moveAmount=delta*walkingSpeed;
        if (getDirection()==RIGHT) {
            setX(getX()+moveAmount);

        } else {
            setX(getX()-moveAmount);
        }
        walkAnimation.continueAnimation(delta);
    }
    //delta ei nyt sittenkään vielä käytössä?
    public void startWalking(boolean direction) {

        testXandY();

        walkingSpeed=maximumWalkingSpeed;

        if (getDirection() != direction) {
            setDirection(direction);
            walkAnimation.flipMirror();
        }

        setStatus(WALKING);
        walkAnimation.resetAnimation();

        //1.0f on aina tilen leveys, siitä on lähdetty, voisi tulla kyllä parametrinä jos se muuttuu

        if (direction == RIGHT) {
            setTargetGameObjectPosX(getX()+1.0f);
            setTargetTilePosX(getTargetTilePosX()+1);

        } else {
            setTargetGameObjectPosX(getX()-1.0f);
            setTargetTilePosX(getTargetTilePosX()-1);

        }


    }



    /*
    OLDER WALKMETHOD WITH FREE RANGE WALKING
    public void walk(boolean direction, float delta) {

        /*
        if (this.direction != direction) {
            this.direction = direction;
            walkAnimation.flipMirror();



        if (this.direction != direction) {
            this.direction = direction;
            walkAnimation.flipMirror();
        }





        if (!walking) {

            walking=true;
            walkAnimation.resetAnimation();




        }

        walkAnimation.continueAnimation(delta);

        float moveAmount= delta*walkingSpeed;


        if(direction == RIGHT) {
            setX(getX()+moveAmount);

        } else {
            setX(getX()-moveAmount);
        }


    }
    */

    public void keepFalling() {

        //DecimalFormat df = new DecimalFormat("#.##");
        //df.setRoundingMode(RoundingMode.FLOOR);
        //float newDestination = getY()+gravityPull;
        //newDestination = df.format(newDestination);
        //System.out.println(newDestination);

        //TÄÄ TOIMII MUTTA PITÄÄKIN TEHDÄ TOISELLA LAILLA
        //float newPosRounded = (float)Math.round(getLegPosY() * 100f) / 100f;


        //System.out.println((float)Math.round(getLegPosY() * 100f) / 100f);

        //int previousTilePos = (int)getLegPosY();

        //setY(getY()+gravityPull);

        //int nextTilePos = (int)getLegPosY();



        //if (previousTilePos < 0 && nextTilePos >= 0) {
            //inAir = false;
            //setY(0-getHeight());

        //if (previousTilePos != nextTilePos) {


        //    inAir=false;
        //    setY(nextTilePos - getHeight());
        //}

        //float upcomingPosition =
                //setY(getY()+gravityPull);

       // if(getLegPosY() % 1 == 0) {
        //    inAir=false;
         //   System.out.println("sdfsfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdf");
        //}



    }

    public void updateMovement(float delta, Stage currentStage) {

        //mitenkähän oikeaoppisesti saakaan täällä MainGamen tileheightin ja pikselimäärän kamerasta, ehkä ei ookaan tarkoitus hmm

        //System.out.println(getY()+ getHeight());
        //System.out.println(getLegPosY());

        //System.out.println(getLegPosY());
        /*
        if(getLegPosY() % 1 != 0) {
            inAir=true;
        } else {
            inAir=false;
        }

        if (inAir) {
            keepFalling();
        }

         */

        //System.out.println("player.getX(): "+getX()+", player.getY(): "+getY());

        //if (inAir) {
        //    keepFalling();
        //}


        if (status==WALKING) {
            boolean actionContinues=true;
            if (getDirection() == RIGHT && getTargetGameObjectPosX() <= getX() ) {
                actionContinues=false;
            }
            if (getDirection() == LEFT && getTargetGameObjectPosX() >= getX() ) {
                actionContinues=false;
            }

            if (actionContinues) {
                continueWalking(delta);

            } else {
                setStatus(READY);
                confirmChangeInTilePosition();
            }

        } else if (status == DIGGING) {

            //System.out.println("it is here in updatemovement digging");
            boolean actionContinues=true;
            if (getTargetGameObjectPosY() <= getY() ) {
               actionContinues=false;
            }
            if (actionContinues) {
                continueDigging(delta);
            } else {
                setStatus(READY);
                confirmChangeInTilePosition();
                currentStage.tiles[getTilePosY()][getTilePosX()].vanish();

            }
        } else if (status == BREAKING) {
/*
            boolean actionContinues=true;
            if (breakingDirection == RIGHT && targetGameObjectPosX <= getX() ) {
                actionContinues=false;
            }
            if (breakingDirection == LEFT && targetGameObjectPosX >= getX() ) {
                actionContinues=false;
            }
            if (actionContinues) {
                continueBreaking(delta);
            } else {
                setStatus(READY);
                confirmChangeInTilePosition();
            }*/

            if (breakingRemaining >= 0) {
                continueBreaking(delta);
            } else {
                setStatus(READY);
                //shouldn't actually change tile position, but maybe if different breaking animations
                //do something for position, its good to have this. maybe.
                confirmChangeInTilePosition();
                if (breakingDirection == RIGHT) {
                    currentStage.tiles[getTilePosY()][getTilePosX()+1].vanish();
                } else {
                    currentStage.tiles[getTilePosY()][getTilePosX()-1].vanish();
                }

            }

        } else if (status == BREAKINGROOF) {
            boolean actionContinues = true;
            if (breakingRemaining >=0) {
                continueBreakingRoof(delta);
            } else {
                setStatus(READY);
                confirmChangeInTilePosition();
                currentStage.tiles[getTilePosY()-1][getTilePosX()].vanish();
            }

        }else if (status == FALLING) {

            boolean actionContinues=true;



            if (getTargetGameObjectPosY() <= getY() ) {
                actionContinues=false;
            }
            if (actionContinues) {
                continueFalling(delta);
            } else {
                fallingSpeed=ORIGINALFALLINGSPEED;
                setStatus(READY);
                confirmChangeInTilePosition();
            }
        } else if (status == ATTACKING) {


            if(attackForthAndBackMoveRemaining>0) {
                continueAttacking(delta);

            } else {
                rectangle.setWidth(normalWidth);
                confirmChangeInTilePosition();
                setStatus(READY);
            }
        }





    }

    //saattaa kuulostaa tyhmältä metodin nimeltä mutta tätä on tarkoitus käyttää vain silloin kun
    //se on tosiaan muuttunut eli liike varmasti tullut päätökseen

    //TÄRKEÄÄÄÄÄ PITÄÄ MUISTAA LAITTAA TÄSSÄ SE ADJUSTOINTI GETPOSSIINKIN ettei jää yhtään eri kohtaan kuin keskelle


    float getLegPosY() {


        return (getY()+getHeight());
        //return (float)(Math.round((getY()+getHeight() ) * 100f)) / 100f;

    }

    //this should probably go into "mammal" or "living" class or some walking interface
    @Override
    public void draw(SpriteBatch batch) {

        //tää pitää tehdä uudestaan sitten kun falling ym muutenkin front oleellinen että sitä kyätetään?
        if (status==READY) {

            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        } else if (status==WALKING) {
            batch.draw(walkAnimation.getCurrentFrame(), getX(), getY(), getWidth(), getHeight());

        //HUOMI VAIHDOIN GETWIDTHIÄ ja GETX TÄSSÄ, ANIMAATIOIDEN KOKOSUHTEIDEN KANSSA PITÄÄ MIETTIÄ MITEN KUVASUHDE AINA SÄILYY
        } else if (status==DIGGING) {
            batch.draw(digAnimation.getCurrentFrame(), getX()+   0.15f    , getY(), getWidth()*0.6f, getHeight()) ;
        } else if (status==BREAKING) {
            batch.draw(breakAnimation.getCurrentFrame(), getX()+   0.15f    , getY(), getWidth()*0.6f, getHeight()) ;
        } else if (status==BREAKINGROOF) {
            batch.draw(breakRoofAnimation.getCurrentFrame(), getX()+ 0.15f, getY(), getWidth()*0.6f, getHeight()) ;
        } else if (status==FALLING) {
            batch.draw(fallAnimation.getCurrentFrame(), getX()+   0.15f    , getY(), getWidth()*0.6f, getHeight()) ;
        } else if (status==ATTACKING) {
            batch.draw(attackAnimation.getCurrentFrame(), getX()    , getY(), getWidth()*0.6f, getHeight()) ;
        }

    }


}
