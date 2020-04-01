package fi.tuni.tiko.digging;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

// will determine how many Areas, and what kind, there will be in each Stages map (GameTile[][] tiles) there will be

//will also determine probability of non-stone tiles being blank tiles instead of dirt tiles

//will also determine hazardTemplate (chances of different hazards being generated)

public class MapTemplate {

    ArrayList <AreaTemplate> areaTemplates;

    private TerrainTemplate terrainTemplate;
    private HazardTemplate hazardTemplate;



    public HazardTemplate getHazardTemplate () {
        return hazardTemplate;
    }

    public void setHazardTemplate (HazardTemplate hazardTemplate) {
        this.hazardTemplate = hazardTemplate;
    }

    public TerrainTemplate getTerrainTemplate () {
        return terrainTemplate;
    }

    public void setTerrainTemplate (TerrainTemplate terrainTemplate) {
        this.terrainTemplate = terrainTemplate;
    }

    //this method could use premade templates instead of randoming like in default constructor
    public MapTemplate(int number) {


    }

    //constructor that makes (somewhat random number number of) ONLY areas as random as possible
    //AND uses default hazardTemplate
    public MapTemplate() {

        areaTemplates = new ArrayList<AreaTemplate>();
        terrainTemplate = new TerrainTemplate();
        hazardTemplate = new HazardTemplate();

        //terrainTemplate.setChanceOfDirtTileBeingBlank(30);

        // laitoin vaan 3,6 testin vuoksi, myöhemmin vähän enemmän

        int numberOfAreas = MathUtils.random(5,7);
        for (int i=0; i < numberOfAreas; i++) {
            AreaTemplate generalTemplate = new AreaTemplate();
            areaTemplates.add(generalTemplate);
        }

    }



    public MapTemplate getAreaTemplate(int number) {
        return new MapTemplate(number);
    }


    //en tiä onko paras nimi
    public ArrayList<AreaTemplate> getArrayList() {
        return areaTemplates;
    }


}
