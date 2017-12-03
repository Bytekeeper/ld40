package org.bk.dungeon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import org.bk.dungeon.DungeonGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.fast = true;
        settings.combineSubdirectories = true;
        settings.filterMag = Texture.TextureFilter.MipMapLinearNearest;
        settings.maxWidth = 4096;
        settings.maxHeight = 4096;
        TexturePacker.processIfModified(settings,"../asset_src/Dungeon Crawl Stone Soup Full", "assets", "dcssf");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 600;
		new LwjglApplication(new DungeonGame(), config);
	}
}
