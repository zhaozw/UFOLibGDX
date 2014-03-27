package com.Hokaim.UFO;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class UFOGameScreen implements Screen {

	final UFO game;
	final int UFO_WIDTH = 64;
	final int UFO_HEIGHT = 64;
	
   Texture dropImage;
   Texture bucketImage;
   Texture backgroundImage;
   Sound dropSound;
   Music rainMusic;
   OrthographicCamera camera;
   Rectangle bucket;
   Array<Rectangle> raindrops;
   long lastDropTime;
   Sprite backgroundSprite;
   boolean isMoving = false;
   boolean isPaused = false;

   public UFOGameScreen(final UFO gam) {
	   this.game = gam;
	   Gdx.input.setCatchBackKey(true);
	   
      // load the images for the droplet and the bucket, 64x64 pixels each
      dropImage = new Texture(Gdx.files.internal("data/droplet.png"));
      bucketImage = new Texture(Gdx.files.internal("data/bucket.png"));
      backgroundImage = new Texture(Gdx.files.internal("data/libgdx.png"));

      backgroundImage.setFilter(TextureFilter.Linear, TextureFilter.Linear);

      // load the drop sound effect and the rain background "music"
      dropSound = Gdx.audio.newSound(Gdx.files.internal("data/smb3_coin.wav"));
      rainMusic = Gdx.audio.newMusic(Gdx.files.internal("data/Robert Miles - Children.mp3"));

      // start the playback of the background music immediately
      rainMusic.setLooping(true);

      // create the camera and the SpriteBatch
      camera = new OrthographicCamera();
      camera.setToOrtho(false, UFO.SCREEN_WIDTH, UFO.SCREEN_HEIGHT);

      // create a Rectangle to logically represent the bucket
      bucket = new Rectangle();
      bucket.x = UFO.SCREEN_WIDTH / 2 - UFO_WIDTH / 2; // center the bucket horizontally
      bucket.y = 20; // bottom left corner of the bucket is 20 pixels above the bottom screen edge
      bucket.width = UFO_WIDTH;
      bucket.height = UFO_HEIGHT;

      // create the raindrops array and spawn the first raindrop
      raindrops = new Array<Rectangle>();
      spawnRaindrop();
      
      TextureRegion backgroundRegion = new TextureRegion(backgroundImage, 0, 0, 512, 275);

      backgroundSprite = new Sprite(backgroundRegion);
      backgroundSprite.setSize(0.9f, 0.9f * backgroundSprite.getHeight() / backgroundSprite.getWidth());
      backgroundSprite.setOrigin(backgroundSprite.getWidth()/2, backgroundSprite.getHeight()/2);
      backgroundSprite.setPosition(-backgroundSprite.getWidth()/2, -backgroundSprite.getHeight()/2);

   }

   private void spawnRaindrop() {
      Rectangle raindrop = new Rectangle();
      raindrop.x = MathUtils.random(0, UFO.SCREEN_WIDTH - 64);
      raindrop.y = UFO.SCREEN_HEIGHT;
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

      game.batch.end();
      if (Gdx.input.justTouched()) {
         isPaused = false;
      }
   }
   
   private void renderGame() {
      
      if (Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE)) {
         pause();
      }

      renderScreen();
      moveUFO();

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
      game.batch.draw(backgroundSprite, 0, 0, UFO.SCREEN_WIDTH, UFO.SCREEN_HEIGHT);
      game.batch.draw(bucketImage, bucket.x, bucket.y);
      for (Rectangle raindrop: raindrops) {
         game.batch.draw(dropImage, raindrop.x, raindrop.y);
      }
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
         if (raindrop.overlaps(bucket)) {
            if (UFO.prefs.getBoolean("playSounds")) {
               dropSound.play();
            }
            iter.remove();
         }
      }
   }
   
   private void moveUFO() {
      // process user input
      //TODO: will be a isAccelOn flag later
      if (UFO.prefs.getBoolean("useAccel") && Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
          bucket.x = Gdx.input.getAccelerometerY() * UFO.SCREEN_WIDTH / 10 + UFO.SCREEN_WIDTH / 2;
          bucket.y = Gdx.input.getAccelerometerX() * UFO.SCREEN_HEIGHT / 10 * -1 + UFO.SCREEN_HEIGHT / 2;
       }
      
      if (Gdx.input.justTouched()) {
         Vector3 touchPos = new Vector3();
         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camera.unproject(touchPos);
         if (touchPos.x >= bucket.x - (UFO_WIDTH / 2) && touchPos.x <= bucket.x + UFO_WIDTH * 3 / 2
               && touchPos.y >= bucket.y - (UFO_HEIGHT / 2) && touchPos.y <= bucket.y + UFO_HEIGHT * 3 / 2) {
            isMoving = true;
         }
         else {
            isMoving = false;
         }
      }
      
      if (Gdx.input.isTouched() && isMoving) {
         Vector3 touchPos = new Vector3();
         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camera.unproject(touchPos);
         bucket.x = touchPos.x - UFO_WIDTH / 2;
         bucket.y = touchPos.y - UFO_HEIGHT / 2;
      }

      if (Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
      if (Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();
      if (Gdx.input.isKeyPressed(Keys.DOWN)) bucket.y -= 200 * Gdx.graphics.getDeltaTime();
      if (Gdx.input.isKeyPressed(Keys.UP)) bucket.y += 200 * Gdx.graphics.getDeltaTime();

      // make sure the bucket stays within the screen bounds
      if (bucket.x < 0) bucket.x = 0;
      if (bucket.x > UFO.SCREEN_WIDTH - UFO_WIDTH) bucket.x = UFO.SCREEN_WIDTH - UFO_WIDTH;
      if (bucket.y < 0) bucket.y = 0;
      if (bucket.y > UFO.SCREEN_HEIGHT - UFO_HEIGHT) bucket.y = UFO.SCREEN_HEIGHT - UFO_HEIGHT;
      
   }

   @Override
   public void dispose() {
      // dispose of all the native resources
      dropImage.dispose();
      bucketImage.dispose();
      dropSound.dispose();
      rainMusic.dispose();
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
      if (UFO.prefs.getBoolean("playMusic")) {
         rainMusic.play();
      }
   }

   @Override
   public void hide() {

   }
}