package com.Hokaim.UFO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class UFOMainMenuScreen implements Screen {
   
   final UFO game;

   OrthographicCamera camera;
   
//   TextButton playGame = new TextButton("Play Game", skin);

   public UFOMainMenuScreen(final UFO gam) {
      game = gam;

      camera = new OrthographicCamera();
      camera.setToOrtho(false, UFO.SCREEN_WIDTH, UFO.SCREEN_HEIGHT);
      
      if (!UFO.prefs.contains("playMusic")) {
         UFO.prefs.putBoolean("playMusic", true);
      }
      if (!UFO.prefs.contains("playSounds")) {
         UFO.prefs.putBoolean("playSounds", true);
      }

      if (!UFO.prefs.contains("useAccel")) {
         if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
            UFO.prefs.putBoolean("useAccel", true);
         }
         else {
            UFO.prefs.putBoolean("useAccel", false);
         }
      }
      UFO.prefs.flush();
   }

   @Override
   public void render(float delta) {
      
      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      camera.update();
      game.batch.setProjectionMatrix(camera.combined);

      game.batch.begin();
      game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
      game.font.draw(game.batch, "Tap here to begin!", 100, 100);
      if (UFO.prefs.getBoolean("playMusic")) {
         game.font.draw(game.batch, "MUSIC IS ON!", UFO.SCREEN_WIDTH / 2, UFO.SCREEN_HEIGHT / 4);
      }
      else {
         game.font.draw(game.batch, "music is off", UFO.SCREEN_WIDTH / 2, UFO.SCREEN_HEIGHT / 4);
      }
      if (UFO.prefs.getBoolean("playSounds")) {
         game.font.draw(game.batch, "SOUNDS ARE ON!", UFO.SCREEN_WIDTH * 3 / 4, UFO.SCREEN_HEIGHT / 4);
      }
      else {
         game.font.draw(game.batch, "sounds are off", UFO.SCREEN_WIDTH * 3 / 4, UFO.SCREEN_HEIGHT / 4);
      }
      if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
         if (UFO.prefs.getBoolean("useAccel")) {
            game.font.draw(game.batch, "ACCEL IS ON!", UFO.SCREEN_WIDTH / 2, UFO.SCREEN_HEIGHT / 4 * 3);
         }
         else {
            game.font.draw(game.batch, "accel is off", UFO.SCREEN_WIDTH / 2, UFO.SCREEN_HEIGHT / 4 * 3);
         }
      }

      game.batch.end();

      if (Gdx.input.justTouched()) {
         Vector3 touchPos = new Vector3();
         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camera.unproject(touchPos);
         if (touchPos.x <= UFO.SCREEN_WIDTH / 2 && touchPos.y <= UFO.SCREEN_HEIGHT / 2) {
            game.setScreen(new UFOGameScreen(game));
            dispose();
         }
         else if (touchPos.x > UFO.SCREEN_WIDTH * 3 / 4 && touchPos.y <= UFO.SCREEN_HEIGHT / 2) {
            if (UFO.prefs.getBoolean("playSounds")) {
               UFO.prefs.putBoolean("playSounds", false);
            }
            else {
               UFO.prefs.putBoolean("playSounds", true);
            }
         }
         else if (touchPos.x > UFO.SCREEN_WIDTH / 2 && touchPos.y <= UFO.SCREEN_HEIGHT / 2) {
            if (UFO.prefs.getBoolean("playMusic")) {
               UFO.prefs.putBoolean("playMusic", false);
            }
            else {
               UFO.prefs.putBoolean("playMusic", true);
            }
         }
         else if (touchPos.x > UFO.SCREEN_WIDTH / 2 && touchPos.y > UFO.SCREEN_HEIGHT / 2) {
            if (UFO.prefs.getBoolean("useAccel")) {
               UFO.prefs.putBoolean("useAccel", false);
            }
            else {
               UFO.prefs.putBoolean("useAccel", true);
            }
         }
      }
      UFO.prefs.flush();
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
