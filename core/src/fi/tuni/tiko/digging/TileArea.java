package fi.tuni.tiko.digging;


/*TileAreas should only (and always) be used when generating a new "map" (GameTile [][] tiles).
  One map consists of many TileAreas, some of which might be random, and some always the same when
  it comes to GameTiles themselves. They might have different resources though.

  Pitää myös miettiä onko TileAreat olemassa vain jossain Stagen luontimetodissa, ja niiden sisältö
  vaan aina kopioidaan Tiles-tauluun. Luultavasti, jos ei ole muuta virkaa.

  Pitää miettiä onko resourcet, esteet jne mukana TileAreoiden generoinnissa vai tuleeko ne erillisenä "sapluunana"
  myöhemmin päälle randomisti
 */
public class TileArea {

    int edgeLeftWidth;
    int edgeRightWidth;

    int rows;

    int entrancePosX;

    int exitPosX;

    GameTile[][] areaTiles;

    //public GameTile[][] addAreas (GameTile[][] tiles, AreaTemplate template) {

    //}
    /*
    public GameTile[][] addAreas (GameTile[][] tiles, AreaTemplate template) {

    }*/



}
