package fi.tuni.tiko.digging;

public class LevelStats {

    //unique id of the level
    private int id;
    //ei ainakaan viela toteutettu
    private int condition;

    private int resourcesLeft;
    private int resourceRichnessDuringRuns;

    private float resourceRichnessMultiplier;

    public int getId () {
        return id;
    }

    public int getCondition () {
        return condition;
    }

    public void setCondition (int condition) {
        this.condition = condition;
    }

    public int getResourcesLeft () {
        return resourcesLeft;
    }

    public void setResourcesLeft (int resourcesLeft) {
        this.resourcesLeft = resourcesLeft;
    }

    public int getResourceRichnessDuringRuns () {
        return resourceRichnessDuringRuns;
    }

    public void setResourceRichnessDuringRuns (int resourceRichnessDuringRuns) {
        this.resourceRichnessDuringRuns = resourceRichnessDuringRuns;
    }

    public LevelStats(int id, int condition, int resourcesLeft, float resourceRichnessMultiplier) {
        this.id=id;
        this.condition=condition;
        this.resourcesLeft=resourcesLeft;
        this.resourceRichnessMultiplier=resourceRichnessMultiplier;
        calculateResourceRichness();

    }


    public void calculateResourceRichness() {
        resourceRichnessDuringRuns = (int)(resourcesLeft * resourceRichnessMultiplier);
    }


}
