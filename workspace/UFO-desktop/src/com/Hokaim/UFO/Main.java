package com.Hokaim.UFO;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "UFO";
		cfg.useGL20 = false;
		cfg.width = UFOGameStart.SCREEN_WIDTH;
		cfg.height = UFOGameStart.SCREEN_HEIGHT;
		
		new LwjglApplication(new UFOGameStart(), cfg);
	}
}
