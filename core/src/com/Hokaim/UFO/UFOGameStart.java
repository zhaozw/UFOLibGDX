package com.Hokaim.UFO;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UFOGameStart extends Game {
   public static final int SCREEN_WIDTH = 1280;
   public static final int SCREEN_HEIGHT = 720;
   public static Preferences prefs;
   SpriteBatch batch;
   BitmapFont font;

   public void create() {
      batch = new SpriteBatch();
      // Use LibGDX's default Arial font.
      font = new BitmapFont();
      font.setScale(2);
      prefs = Gdx.app.getPreferences("gameplay");
      initPrefs();
      this.setScreen(new UFOMainMenuScreen(this));
   }
   
   private void initPrefs() {
      if (!prefs.contains("playMusic"))
         prefs.putBoolean("playMusic", true);
      
      if (!prefs.contains("playSounds"))
         prefs.putBoolean("playSounds", true);

      if (!prefs.contains("useAccel"))
         prefs.putBoolean("useAccel", Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer));

      prefs.flush();
   }

   public void render() {
      super.render(); // important!
   }

   public void dispose() {
      batch.dispose();
      font.dispose();
   }
}
