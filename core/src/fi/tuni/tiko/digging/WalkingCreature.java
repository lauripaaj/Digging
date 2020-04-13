package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class WalkingCreature extends Creature {

    public static final int DEAD = -2;
    public static final int VANISHING = -1;
    public static final int READY = 0;
    public static final int FALLING = 1;
    public static final int WALKING = 2;

    //for FallingTrap, maybe some others too
    public static final int TRIGGERED = 20;


    public static final boolean LEFT = false;
    public static final boolean RIGHT = true;

    private GameTexture standTexture;

    // these will be initialized with low values so it's easier to notice if they haven't been set in subclass
    private float walkingSpeed = 0.2f;

    private float originalFallingSpeed=0.2f;
    private float fallingSpeed = originalFallingSpeed;




    private int status = READY;

    private SheetAnimation walkAnimation;
    private SheetAnimation fallAnimation;
    private SheetAnimation triggerAnimation;




    //for fallingTrap, maybe some others too
    private float triggerTimeLeft;

    public void setTriggerAnimation (SheetAnimation triggerAnimation) {
        this.triggerAnimation = triggerAnimation;
    }

    public SheetAnimation getTriggerAnimation () {
        return triggerAnimation;
    }

    public GameTexture getStandTexture () {
        return standTexture;
    }

    public float getTriggerTimeLeft () {
        return triggerTimeLeft;
    }

    public void setTriggerTimeLeft (float triggeredTimeLeft) {
        this.triggerTimeLeft = triggeredTimeLeft;
    }

    public void setStandTexture (GameTexture standTexture) {
        this.standTexture = standTexture;
        setTexture(standTexture);
    }

    public float getWalkingSpeed () {
        return walkingSpeed;
    }

    public void setWalkingSpeed (float walkingSpeed) {
        this.walkingSpeed = walkingSpeed;
    }

    public float getOriginalFallingSpeed () {
        return originalFallingSpeed;
    }

    public void setOriginalFallingSpeed (float originalFallingSpeed) {
        this.originalFallingSpeed = originalFallingSpeed;
        fallingSpeed = originalFallingSpeed;
    }

    public float getFallingSpeed () {
        return fallingSpeed;
    }

    public void setFallingSpeed (float fallingSpeed) {
        this.fallingSpeed = fallingSpeed;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public SheetAnimation getWalkAnimation () {
        return walkAnimation;
    }

    public void setWalkAnimation (SheetAnimation walkAnimation) {
        this.walkAnimation = walkAnimation;
    }

    public SheetAnimation getFallAnimation () {
        return fallAnimation;
    }

    public void setFallAnimation (SheetAnimation fallAnimation) {
        this.fallAnimation = fallAnimation;
    }


    public void continueTriggering(float delta) {

        if (triggerTimeLeft >= 0) {
            triggerTimeLeft = triggerTimeLeft - 1.2f * delta;
            triggerAnimation.continueAnimationOnce(delta);

        }
    }

    public void startTriggering() {
        setStatus(TRIGGERED);
        getTriggerAnimation().resetAnimation();
        setTriggerTimeLeft(4f);
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
    public void checkIfNeedToFallEvenMore(GameTile[][] tiles) {
        if (tiles[getTargetTilePosY()+1][getTilePosX()].isConcrete()==false) {
            setTargetTilePosY(getTargetTilePosY()+1);
            setTargetGameObjectPosY(getTargetGameObjectPosY()+1);
            //System.out.println("added one for falling");
        }
    }

    public void continueWalking(float delta) {

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

    public void draw(SpriteBatch batch) {

        //tää pitää tehdä uudestaan sitten kun falling ym muutenkin front oleellinen että sitä kyätetään?
        if (status==READY) {

            batch.draw(standTexture, getX(), getY(), getWidth(), getHeight());
        } else if (status==WALKING) {
            batch.draw(walkAnimation.getCurrentFrame(), getX(), getY(), getWidth(), getHeight());

            //HUOMI VAIHDOIN GETWIDTHIÄ ja GETX TÄSSÄ, ANIMAATIOIDEN KOKOSUHTEIDEN KANSSA PITÄÄ MIETTIÄ MITEN KUVASUHDE AINA SÄILYY

        } else if (status==FALLING) {
            batch.draw(fallAnimation.getCurrentFrame(), getX()+   0.15f    , getY(), getWidth()*0.6f, getHeight()) ;
            //batch.draw(standTexture, getX(), getY(), getWidth(), getHeight());
        } else if (status==VANISHING) {
            batch.draw(getVanishAnimation().getCurrentFrame(), getX()+0.15f, getY(), getWidth()*0.6f, getHeight());
        }

    }

    /*
    abstract int getStatus () ;

    abstract float getWalkingSpeed ();

    abstract float getFallingSpeed ();

    abstract SheetAnimation getWalkAnimation();

    abstract SheetAnimation getFallAnimation();

    */



    /*
    abstract void startWalking();
    abstract void continueWalking();

    abstract void startFalling();
    abstract void continueFalling();
    */
}
