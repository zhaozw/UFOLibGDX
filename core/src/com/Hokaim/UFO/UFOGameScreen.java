package com.Hokaim.UFO;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class UFOGameScreen implements Screen {
//TODO: NOTE: Can probably make an abstract class for game screens, help to standardize flow
	final UFOGameStart game;
	
   Sound dropSound;
   Music gameMusic;
   OrthographicCamera camera;
   Array<Projectile> projectiles;
   long lastDropTime;
   boolean isPaused = false;
   int score;
   UFO UFO;
   Background background;

   public UFOGameScreen(final UFOGameStart gam) {
	   this.game = gam;
	   Gdx.input.setCatchBackKey(true);
	   
	   UFO = new UFO("Textures/Spaceship Alpha 2.png");
	   background = new Background();
	   
      // load the drop sound effect and the rain background "music"
      dropSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/smb3_coin.wav"));
      gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/mega-man-wily-stage-1-2.mp3"));

      // start the playback of the background music immediately
      gameMusic.setLooping(true);

      // create the camera and the SpriteBatch
      camera = new OrthographicCamera();
      camera.setToOrtho(false, UFOGameStart.SCREEN_WIDTH, UFOGameStart.SCREEN_HEIGHT);
      
      projectiles = new Array<Projectile>();
      
      spawnProjectile();
      
      //TODO: remove later
      float x = MathUtils.random(0, UFOGameStart.SCREEN_WIDTH - ProjDrop.PROJECTILE_WIDTH);
      float velX = MathUtils.random(-10, 10);
            
      ProjBucket p = new ProjBucket(x, UFOGameStart.SCREEN_HEIGHT, velX, -200);
      p.acceleration.x = MathUtils.random() * 50;
      
      projectiles.add(p);
      
      
   }

   private void spawnProjectile() {
      //TODO: Clean this function up, may want to consider multiple sizes for projectile?
      float x = MathUtils.random(0, UFOGameStart.SCREEN_WIDTH - ProjDrop.PROJECTILE_WIDTH);
      float velX = MathUtils.random(-10, 10);
            
      ProjDrop p = new ProjDrop(x, UFOGameStart.SCREEN_HEIGHT, velX, -400);
      p.acceleration.x = MathUtils.random() * 50;
      
      projectiles.add(p);
      
      lastDropTime = TimeUtils.nanoTime();
   }

   @Override
   public void render(float delta) {
      if (isPaused) {
         renderPause();
      }
      else {
         renderGame();
      }
   }
   
   private void renderPause() {
      renderScreen();
      camera.update();
      game.batch.setProjectionMatrix(camera.combined);

      game.batch.begin();
      game.font.draw(game.batch, "PAUSED ", 100, 150);
      game.font.draw(game.batch, "Tap here to resume!", 100, 100);
      game.font.draw(game.batch, "Tap here for main menu", UFOGameStart.SCREEN_WIDTH * 3 / 4, UFOGameStart.SCREEN_HEIGHT / 4);

      game.batch.end();

      if (Gdx.input.justTouched()) {
         Vector3 touchPos = new Vector3();
         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camera.unproject(touchPos);
         if (touchPos.x <= UFOGameStart.SCREEN_WIDTH / 2 && touchPos.y <= UFOGameStart.SCREEN_HEIGHT / 2) {
            isPaused = false;
         }
         else if (touchPos.x > UFOGameStart.SCREEN_WIDTH * 3 / 4 && touchPos.y <= UFOGameStart.SCREEN_HEIGHT / 2) {
            game.setScreen(new UFOMainMenuScreen(game));
            dispose();
         }
      }
   }
   
   private void renderGame() {
      if (Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE))
         pause();

      UFO.updateRotation();
      UFO.moveUFO();
      moveProjectiles();
      renderScreen();
   }
   
   private void renderScreen() {
      // tell the camera to update its matrices.
      camera.update();

      // tell the SpriteBatch to render in the
      // coordinate system specified by the camera.
      game.batch.setProjectionMatrix(camera.combined);

      // begin a new batch and draw the bucket and
      // all drops
      game.batch.begin();
      game.batch.draw(background.backgroundSprite, 0, 0, UFOGameStart.SCREEN_WIDTH, UFOGameStart.SCREEN_HEIGHT);
      
      UFO.sprite.draw(game.batch);

      for (Projectile p: projectiles) {
         p.draw(game.batch);
      }
      game.font.draw(game.batch, "SCORE: " + score, UFOGameStart.SCREEN_WIDTH * 7 / 8, UFOGameStart.SCREEN_HEIGHT / 8);

      game.font.draw(game.batch, "numProjectiles: " + projectiles.size, UFOGameStart.SCREEN_WIDTH * 1 / 8, UFOGameStart.SCREEN_HEIGHT / 8);
      
      game.batch.end();
   }
   
   private void moveProjectiles() {
      // check if we need to create a new raindrop
      if (TimeUtils.nanoTime() - lastDropTime > 250000000) spawnProjectile();

      // move the projectiles, remove any that are beneath the bottom edge of
      // the screen or that hit the ufo. In the later case we play back
      // a sound effect as well.
      Iterator<Projectile> iter = projectiles.iterator();
      while (iter.hasNext()) {
         Projectile p = iter.next();
         p.updateProjectile();
         if (p.getX() + p.getWidth() < 0) iter.remove();
         else if (p.getX() > UFOGameStart.SCREEN_WIDTH) iter.remove();
         else if (p.getY() + p.getHeight() < 0) iter.remove();
//         if (p.sprite.getBoundingRectangle().overlaps(UFO.shape)) {
//         if (UFO.collides(p.sprite.getBoundingRectangle())) {
//         else if (UFO.collides(p.shape)) {
         else if (p.collides(UFO.shape)) {
            
            score++;
            if (UFOGameStart.prefs.getBoolean("playSounds")) {
               dropSound.play();
            }
            if (p.getClass() == ProjDrop.class) {
               Gdx.input.vibrate(75);
            }
            iter.remove();
         }
      }
   }

   @Override
   public void dispose() {
      // dispose of all the native resources
      dropSound.dispose();
      gameMusic.dispose();
   }

   @Override
   public void resize(int width, int height) {
   }

   @Override
   public void pause() {
//      Gdx.input.setCatchBackKey(false);
//      game.setScreen(new UFOMainMenuScreen(game));
//      dispose();
      isPaused = true;
   }

   @Override
   public void resume() {
   }

   @Override
   public void show() {
	   // start the playback of the background music when the screen is shown
      if (UFOGameStart.prefs.getBoolean("playMusic")) {
         gameMusic.play();
      }
   }

   @Override
   public void hide() {

   }
}