package fi.tuni.tiko.digging;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class ResourceUI {

    private int linesToDraw=0;


    GameTexture meter = new GameTexture(new Texture("menus/resourceMeter.png"));
    GameTexture greenResource = new GameTexture(new Texture("menus/resourceCollected.png"));


    public ResourceUI (int totalResourcesCollected) {

    }


    public void updateLinesToDraw(int totalCollected, int episode) {
        //System.out.println("HUUHUHUUHDUHUDFHDUAFHDF");

        linesToDraw = totalCollected / 37;

    }

    public int getLinesToDraw () {
        return linesToDraw;
    }
}
