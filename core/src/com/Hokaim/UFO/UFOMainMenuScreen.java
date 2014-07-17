package com.Hokaim.UFO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class UFOMainMenuScreen implements Screen {
   
   final UFOGameStart game;

   OrthographicCamera camera;
   
   Background background;
   
   public UFOMainMenuScreen(final UFOGameStart gam) {
      game = gam;
      
      Gdx.input.setCatchBackKey(false);

      camera = new OrthographicCamera();
      camera.setToOrtho(false, UFOGameStart.SCREEN_WIDTH, UFOGameStart.SCREEN_HEIGHT);
      
      background = new Background("Textures/Spaceship Alpha 1.png", 512, 275);  
   }

   @Override
   public void render(float delta) {

      camera.update();
      game.batch.setProjectionMatrix(camera.combined);

      game.batch.begin();
      game.batch.draw(background.backgroundSprite, 0, 0, UFOGameStart.SCREEN_WIDTH, UFOGameStart.SCREEN_HEIGHT);
      game.font.draw(game.batch, "UFO Mania!!! ", 100, 150);
      game.font.draw(game.batch, "Tap here to begin!", 100, 100);
      if (UFOGameStart.prefs.getBoolean("playMusic"))
         game.font.draw(game.batch, "MUSIC IS ON!", UFOGameStart.SCREEN_WIDTH / 2, UFOGameStart.SCREEN_HEIGHT / 4);
      else
         game.font.draw(game.batch, "music is off", UFOGameStart.SCREEN_WIDTH / 2, UFOGameStart.SCREEN_HEIGHT / 4);

      if (UFOGameStart.prefs.getBoolean("playSounds"))
         game.font.draw(game.batch, "SOUNDS ARE ON!", UFOGameStart.SCREEN_WIDTH * 3 / 4, UFOGameStart.SCREEN_HEIGHT / 4);
      else
         game.font.draw(game.batch, "sounds are off", UFOGameStart.SCREEN_WIDTH * 3 / 4, UFOGameStart.SCREEN_HEIGHT / 4);

      if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
         if (UFOGameStart.prefs.getBoolean("useAccel"))
            game.font.draw(game.batch, "ACCEL IS ON!", UFOGameStart.SCREEN_WIDTH / 2, UFOGameStart.SCREEN_HEIGHT / 4 * 3);
         else
            game.font.draw(game.batch, "accel is off", UFOGameStart.SCREEN_WIDTH / 2, UFOGameStart.SCREEN_HEIGHT / 4 * 3);
      }

      game.batch.end();

      updatePrefs();
   }
   
   private void updatePrefs() {
      if (Gdx.input.justTouched()) {
         Vector3 touchPos = new Vector3();
         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camera.unproject(touchPos);
         if (touchPos.x <= UFOGameStart.SCREEN_WIDTH / 2 && touchPos.y <= UFOGameStart.SCREEN_HEIGHT / 2) {
            game.setScreen(new UFOGameScreen(game));
            dispose();
         }
         else if (touchPos.x > UFOGameStart.SCREEN_WIDTH * 3 / 4 && touchPos.y <= UFOGameStart.SCREEN_HEIGHT / 2) {
            if (UFOGameStart.prefs.getBoolean("playSounds")) {
               UFOGameStart.prefs.putBoolean("playSounds", false);
            }
            else {
               UFOGameStart.prefs.putBoolean("playSounds", true);
            }
         }
         else if (touchPos.x > UFOGameStart.SCREEN_WIDTH / 2 && touchPos.y <= UFOGameStart.SCREEN_HEIGHT / 2) {
            if (UFOGameStart.prefs.getBoolean("playMusic")) {
               UFOGameStart.prefs.putBoolean("playMusic", false);
            }
            else {
               UFOGameStart.prefs.putBoolean("playMusic", true);
            }
         }
         else if (touchPos.x > UFOGameStart.SCREEN_WIDTH / 2 && touchPos.y > UFOGameStart.SCREEN_HEIGHT / 2) {
            if (UFOGameStart.prefs.getBoolean("useAccel")) {
               UFOGameStart.prefs.putBoolean("useAccel", false);
            }
            else {
               UFOGameStart.prefs.putBoolean("useAccel", true);
            }
         }
      }
      UFOGameStart.prefs.flush();
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
