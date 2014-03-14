package com.Hokaim.UFO;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UFO extends Game {
   public static final int SCREEN_WIDTH = 800;
   public static final int SCREEN_HEIGHT = 480;
   public static Preferences prefs;
	 SpriteBatch batch;
	 BitmapFont font;

	 public void create() {
		 batch = new SpriteBatch();
		 // Use LibGDX's default Arial font.
		 font = new BitmapFont();
		 prefs = Gdx.app.getPreferences("gameplay");
		 this.setScreen(new UFOMainMenuScreen(this));
	 }

	 public void render() {
		 super.render(); // important!
	 }

	 public void dispose() {
		 batch.dispose();
		 font.dispose();
	 }
}
