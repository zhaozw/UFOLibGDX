package com.Hokaim.UFO;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class UFOGameScreen implements Screen {

	final UFOGameStart game;
	
   Texture dropImage;
   Sound dropSound;
   Music gameMusic;
   OrthographicCamera camera;
   Array<Rectangle> raindrops;
   Array<Projectile> projectiles;
   long lastDropTime;
   boolean isMoving = false;
   boolean isPaused = false;
   Integer score;
   
   UFO UFO;
   Background background;

   public UFOGameScreen(final UFOGameStart gam) {
	   this.game = gam;
	   Gdx.input.setCatchBackKey(true);
	   
	   UFO = new UFO();
	   background = new Background();
	   score = new Integer(0);
	   
      // load the images for the droplet and the bucket, 64x64 pixels each
      dropImage = new Texture(Gdx.files.internal("Textures/droplet.png"));

      // load the drop sound effect and the rain background "music"
      dropSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/smb3_coin.wav"));
      gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/Robert Miles - Children.mp3"));

      // start the playback of the background music immediately
      gameMusic.setLooping(true);

      // create the camera and the SpriteBatch
      camera = new OrthographicCamera();
      camera.setToOrtho(false, UFOGameStart.SCREEN_WIDTH, UFOGameStart.SCREEN_HEIGHT);

      // create the raindrops array and spawn the first raindrop
      raindrops = new Array<Rectangle>();
      spawnRaindrop();
      
      projectiles = new Array<Projectile>();
      Projectile p = new Projectile(10, 10, 10, 10);
      projectiles.add(p);
      p = new Projectile(20, 10, 10, 0);
      projectiles.add(p);
   }

   private void spawnRaindrop() {
      Rectangle raindrop = new Rectangle();
      raindrop.x = MathUtils.random(0, UFOGameStart.SCREEN_WIDTH - 64);
      raindrop.y = UFOGameStart.SCREEN_HEIGHT;
      raindrop.width = 64;
      raindrop.height = 64;
      raindrops.add(raindrop);
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
      
      if (Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE)) {
         pause();
      }

      UFO.updateRotation();
      UFO.moveUFO();
      renderScreen();

      moveRaindrops();
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
      
      game.batch.draw(UFO.UFOImage, UFO.shape.x, UFO.shape.y);
      UFO.UFOSprite.draw(game.batch);

      for (Rectangle raindrop: raindrops) {
         game.batch.draw(dropImage, raindrop.x, raindrop.y);
      }
      for (Projectile p: projectiles) {
         p.sprite.draw(game.batch);
      }
      game.font.draw(game.batch, "SCORE: " + score.toString(), UFOGameStart.SCREEN_WIDTH * 7 / 8, UFOGameStart.SCREEN_HEIGHT / 8);

      game.batch.end();
   }
   
   private void moveRaindrops() {
      // check if we need to create a new raindrop
      if (TimeUtils.nanoTime() - lastDropTime > 50000000) spawnRaindrop();

      // move the raindrops, remove any that are beneath the bottom edge of
      // the screen or that hit the bucket. In the later case we play back
      // a sound effect as well.
      Iterator<Rectangle> iter = raindrops.iterator();
      while (iter.hasNext()) {
         Rectangle raindrop = iter.next();
         raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
         if (raindrop.y + 64 < 0) iter.remove();
         if (raindrop.overlaps(UFO.shape)) {
            score++;
            if (UFOGameStart.prefs.getBoolean("playSounds")) {
               dropSound.play();
            }
            iter.remove();
         }
      }
      Iterator<Projectile> itr = projectiles.iterator();
      while (itr.hasNext()) {
         Projectile p = itr.next();
         p.updateProjectile();
         if (p.sprite.getX() + p.sprite.getWidth() < 0) itr.remove();
         if (p.sprite.getY() + p.sprite.getHeight() < 0) itr.remove();
         if (p.sprite.getBoundingRectangle().overlaps(UFO.shape)) {
            score++;
            if (UFOGameStart.prefs.getBoolean("playSounds")) {
               dropSound.play();
            }
            itr.remove();
         }
      }
   }

   @Override
   public void dispose() {
      // dispose of all the native resources
      dropImage.dispose();
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
	   // start the playback of the background music
	   // when the screen is shown
      if (UFOGameStart.prefs.getBoolean("playMusic")) {
         gameMusic.play();
      }
   }

   @Override
   public void hide() {

   }
}