package fi.tuni.tiko.digging.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fi.tuni.tiko.digging.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title="Digging Game pre-pre-release beta omega v 0.12";
		config.pauseWhenBackground=true;
		config.pauseWhenMinimized=true;
		config.height=1024;


		config.width=576;

		new LwjglApplication(new MainGame(), config);
	}
}
