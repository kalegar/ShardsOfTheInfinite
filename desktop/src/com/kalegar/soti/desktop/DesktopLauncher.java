package com.kalegar.soti.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.kalegar.soti.ShardsOfTheInfinite;

public class DesktopLauncher {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 960;
		config.height = 540;
		config.forceExit = false;
		if (args.length > 0) {
			for (String arg : args) {
				if (arg.equalsIgnoreCase("-pack")) {
					System.out.println("Packing textures...");
					TexturePacker.Settings settings = new TexturePacker.Settings();
					settings.maxWidth = 1024;
					settings.maxHeight = 1024;
					TexturePacker.process("../../assets-raw/images-game","./images","game");
				}
			}
		}
		new LwjglApplication(new ShardsOfTheInfinite(), config);
	}
}
