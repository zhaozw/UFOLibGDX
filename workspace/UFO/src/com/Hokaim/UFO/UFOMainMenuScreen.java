package com.Hokaim.UFO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class UFOMainMenuScreen implements Screen {
   
   final UFO game;

   OrthographicCamera camera;

   Preferences gameplayPrefs;
   
//   TextButton playGame = new TextButton("Play Game", skin);

   public UFOMainMenuScreen(final UFO gam) {
      game = gam;

      camera = new OrthographicCamera();
      camera.setToOrtho(false, 800, 480);

      gameplayPrefs = Gdx.app.getPreferences("gameplay");
      
      gameplayPrefs.putBoolean("playMusic", true);
      gameplayPrefs.putBoolean("playSounds", true);

      if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
         gameplayPrefs.putBoolean("useAccel", true);
      }
      else {
         gameplayPrefs.putBoolean("useAccel", false);
      }
      gameplayPrefs.flush();
   }

   @Override
   public void render(float delta) {

      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      camera.update();
      game.batch.setProjectionMatrix(camera.combined);

      game.batch.begin();
      game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
      game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
      game.batch.end();

      if (Gdx.input.isTouched()) {
         game.setScreen(new UFOGameScreen(game, gameplayPrefs));
         dispose();
      }

   }

   @Override
   public void resize(int width, int height) {

   }

   @Override
   public void show() {

   }

   @Override
   public void hide() {

   }

   @Override
   public void pause() {

   }

   @Override
   public void resume() {

   }

   @Override
   public void dispose() {

   }
}
