package fi.tuni.tiko.digging;

public abstract class TileBasedObject extends GameObject {

    private boolean vanishing = false;
    private float vanishTimeLeft=0;

    private SheetAnimation vanishAnimation;

    private int tilePosY;
    private int tilePosX;

    private int targetTilePosY = tilePosY;
    private int targetTilePosX = tilePosX;


    public SheetAnimation getVanishAnimation () {
        return vanishAnimation;
    }

    public void setVanishAnimation (SheetAnimation vanishAnimation) {
        this.vanishAnimation = vanishAnimation;
    }

    public void continueVanishAnimation(float delta) {

        setVanishTimeLeft(getVanishTimeLeft()-delta*0.5f);
        vanishAnimation.continueAnimationOnce(delta);

    }

    public float getHeightAdjustedY() {
        return getY()-(1f - getHeight());
    }

    public float getVanishTimeLeft () {
        return vanishTimeLeft;
    }

    public void setVanishTimeLeft (float vanishTimeLeft) {
        this.vanishTimeLeft = vanishTimeLeft;
    }

    public boolean isVanishing () {
        return vanishing;
    }

    public void setVanishing (boolean vanishing) {
        this.vanishing = vanishing;
    }

    public int getTilePosY () {
        return tilePosY;
    }

    public void setTilePosY (int tilePosY) {
        this.tilePosY = tilePosY;
    }

    public int getTilePosX () {
        return tilePosX;
    }

    public void setTilePosX (int tilePosX) {
        this.tilePosX = tilePosX;
    }

    //will return nearest position Y where the object currently is based on its rectangles location
    public int getRawTileY() {

        return Math.round(getHeightAdjustedY());

    }
    //will return nearest position X where the object currently is based on its rectangles location
    public int getRawTileX() {

        return Math.round(getX());

    }

    public int getTargetTilePosY () {
        return targetTilePosY;
    }

    public void setTargetTilePosY (int targetTilePosY) {
        this.targetTilePosY = targetTilePosY;
    }

    public int getTargetTilePosX () {
        return targetTilePosX;
    }

    public void setTargetTilePosX (int targetTilePosX) {
        this.targetTilePosX = targetTilePosX;
    }

    //this shortcut is most likely not best practice but it will make working with hazards much easier
    //should never be used with any other TileBasedObject but those that implement Hazard
    public int getHazardStrength() {
        return -5555;
    }

    public void confirmChangeInTilePosition() {

        tilePosX=targetTilePosX;
        targetTilePosX=tilePosX;

        tilePosY=targetTilePosY;
        targetTilePosY=tilePosY;

        putInTilePos(tilePosY, tilePosX);
    }

    //hmm pitää vielä miettiä miten tämän voisi ehkä tehdä
    public void confirmFallPosition() {
        tilePosX=targetTilePosX;
        targetTilePosX=tilePosX;

        tilePosY=targetTilePosY;
        targetTilePosY=tilePosY;


    }


    public void putInTilePos(int posY, int posX) {

        //these 4 rows are mostly unnecceasry, but these positions should be confirmed in constructor or something
        //probably constructor of tileBasedObject should force put these first
        setTilePosX(posX);
        setTilePosY(posY);
        targetTilePosX=posX;
        targetTilePosY=posY;

        setY( (1f-getHeight() )+posY);
        setX( (0.5f-(getWidth()/2) )+posX);

    }

    //IMPORTANT doesnt actually dispose texture but its static so shouldnt be big problem?
    public void dispose() {
    super.dispose();
         if (vanishAnimation != null) {vanishAnimation.dispose();}
    }
}
