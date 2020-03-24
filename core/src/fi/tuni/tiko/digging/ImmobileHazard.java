package fi.tuni.tiko.digging;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

//subclasses of this abstract class are hazards that can't move or fall (it is not a creature)
public abstract class ImmobileHazard extends TileBasedObject implements Hazard {

    private boolean statusDead = false;

    public boolean isStatusDead () {
        return statusDead;
    }

    public void setStatusDead (boolean statusDead) {
        this.statusDead = statusDead;
    }

    @Override
    public void vanish () {
        setVanishing(true);
        setConcrete(false);
        setVanishTimeLeft(0.3f);
    }

    public void checkIfConcreteTileBelow(GameTile[][] tiles) {
        if (!tiles[getTilePosY()+1][getTilePosX()].isConcrete() ) {
            vanish();
        }
    }

    public void updateVanishStatus(float delta) {
        if (isVanishing()) {
            if (getVanishTimeLeft()>0) {
                continueVanishAnimation(delta);
            } else setStatusDead(true);}

    }

    @Override
    public void occupyTile (Stage currentStage) {
        currentStage.tiles[getRawTileY()][getRawTileX()].setOccupied(true);
    }

    @Override
    public void unOccupyTile (Stage currentStage) {
        currentStage.tiles[getRawTileY()][getRawTileX()].setOccupied(false);

    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!isVanishing()) {
            batch.draw(gameTextureRegion, getX(), getY(), getWidth(), getHeight());
        } else if (!isStatusDead()) {

            batch.draw(getVanishAnimation().getCurrentFrame(), getX(), getY(), getWidth(), getHeight());

        }
    }

}
