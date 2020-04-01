package fi.tuni.tiko.digging;

public class StageSettings {

    //1-3 first half of the game, 4-6 episodes, the last half of the game
    private int episode;
    private int level;
    private MapTemplate mapTemplate;
    //private HazardTemplate hazardTemplate;

    //jotenkin rootsit ehkä tähän

    public StageSettings (int episode, int level) {
        if (level < 1 || level > 10) {
            throw new IllegalArgumentException("level must be between 1-10");
        }
        this.episode=episode;
        if (episode >= 1 && episode <= 3) {
            this.level=level;
        } else if (episode >= 4 && episode <= 6) {
            this.level=level+10;
        } else throw new IllegalArgumentException("wrong must be 1-6 episode");
    //createMapTemplate(episode, level);

        createMapTemplate(episode, level);
    }

    public void createMapTemplate(int episode, int level) {
        if (level == 1) {
            //basic template with farm and very random
            mapTemplate = new MapTemplate();
        } else mapTemplate = new MapTemplate(episode, level);
    }

    public int getEpisode () {
        return episode;
    }

    public void setEpisode (int episode) {
        this.episode = episode;
    }

    public int getLevel () {
        return level;
    }

    public void setLevel (int level) {
        this.level = level;
    }

    public MapTemplate getMapTemplate () {
        return mapTemplate;
    }

    public void setMapTemplate (MapTemplate mapTemplate) {
        this.mapTemplate = mapTemplate;
    }
}
