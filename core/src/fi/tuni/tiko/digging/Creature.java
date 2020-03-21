package fi.tuni.tiko.digging;

/*creatures that conquer a tile with their precense. Player is one of the creatures, and they cant
coexist in the same tile with another creature.

PITÄÄ MIETTIÄ MITÄ MUITA ON. VARMAAN CREATURET ON AINA CONCRETE JA SITTEN JOTAIN JUURISTOJA YM. VOI
OLLA OBJECTINA MYÖS JOILLA ON TOISAALTA KYLLÄ TILEPAIKKA, ELI... PITÄÄ VIELÄ TEHDÄ UUSI ALILUOKKA
EI AINOASTAAN GameObject->Creature-> Player vaan myös esim
GameObject->TileObject->Creature->Player
OSA TILEOBJECTEISTA ON CONCRETE FALSE, KUTEN PUUNJUURET JOITA KASTELLAAN JNE HMM, CONCRETE JA EI-CONCRETE
VOI OLLA SAMASSA TILESSÄ, JA VARMAAN EI-CONCRETEJA VOI OLLA USEAMPI MUTTA PITÄÄ MIETTIÄ MITEN TÄMÄ TOIMII
OHJELMALLISESTI, PITÄÄKÖ TEHDÄ TILEISTÄ ArrayListejä? damn

eikun ei, koska player ei oo stagessa vaan ihan maingamen alla... hmmmm en oo nyt ihan varma
varmaan currentStagen alla on tiles-lisäksi... resources[][] jne


 */

import static fi.tuni.tiko.digging.Player.LEFT;
import static fi.tuni.tiko.digging.Player.RIGHT;

public abstract class Creature extends TileBasedObject {

    private float targetGameObjectPosX;
    private float targetGameObjectPosY;

    private boolean direction = LEFT;

    public boolean getDirection () {
        return direction;
    }

    public void setDirection (boolean direction) {
        this.direction = direction;
    }

    public void changeDirection() {
        if (direction == LEFT) {
            direction = RIGHT;
        } else direction = LEFT;

    }

    public float getTargetGameObjectPosX () {
        return targetGameObjectPosX;
    }

    public void setTargetGameObjectPosX (float targetGameObjectPosX) {
        this.targetGameObjectPosX = targetGameObjectPosX;
    }

    public float getTargetGameObjectPosY () {
        return targetGameObjectPosY;
    }

    public void setTargetGameObjectPosY (float targetGameObjectPosY) {
        this.targetGameObjectPosY = targetGameObjectPosY;
    }
}
