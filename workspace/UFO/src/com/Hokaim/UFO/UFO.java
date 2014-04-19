package com.Hokaim.UFO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class UFO {
   
   public static final int UFO_WIDTH = 64;
   public static final int UFO_HEIGHT = 64;
   
   static boolean isMoving = false;
      
   Texture UFOImage;
   Rectangle shape;
   
   public UFO() {
      
      UFOImage = new Texture(Gdx.files.internal("Textures/bucket.png"));
      shape = new Rectangle();
      shape.set(200, 200, UFO_WIDTH, UFO_HEIGHT);
   }
   
   public void moveUFO() {
      // process user input
      //TODO: will be a isAccelOn flag later
      if (UFOGameStart.prefs.getBoolean("useAccel") && Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
          shape.x = Gdx.input.getAccelerometerY() * UFOGameStart.SCREEN_WIDTH / 10 + UFOGameStart.SCREEN_WIDTH / 2;
          shape.y = Gdx.input.getAccelerometerX() * UFOGameStart.SCREEN_HEIGHT / 10 * -1 + UFOGameStart.SCREEN_HEIGHT / 2;
       }
      
      if (Gdx.input.justTouched()) {
         Vector3 touchPos = new Vector3();
         touchPos.set(Gdx.input.getX(), UFOGameStart.SCREEN_HEIGHT - Gdx.input.getY(), 0);
//         camera.unproject(touchPos);
         if (touchPos.x >= shape.x - (UFO_WIDTH / 2) && touchPos.x <= shape.x + UFO_WIDTH * 3 / 2
               && touchPos.y >= shape.y - (UFO_HEIGHT / 2) && touchPos.y <= shape.y + UFO_HEIGHT * 3 / 2) {
            isMoving = true;
         }
         else {
            isMoving = false;
         }
      }
      
      if (Gdx.input.isTouched() && isMoving) {
         Vector3 touchPos = new Vector3();
         touchPos.set(Gdx.input.getX(), UFOGameStart.SCREEN_HEIGHT - Gdx.input.getY(), 0);
//         camera.unproject(touchPos);
         shape.x = touchPos.x - UFO_WIDTH / 2;
         shape.y = touchPos.y - UFO_HEIGHT / 2;
      }

      if (Gdx.input.isKeyPressed(Keys.LEFT)) shape.x -= 200 * Gdx.graphics.getDeltaTime();
      if (Gdx.input.isKeyPressed(Keys.RIGHT)) shape.x += 200 * Gdx.graphics.getDeltaTime();
      if (Gdx.input.isKeyPressed(Keys.DOWN)) shape.y -= 200 * Gdx.graphics.getDeltaTime();
      if (Gdx.input.isKeyPressed(Keys.UP)) shape.y += 200 * Gdx.graphics.getDeltaTime();

      // make sure the bucket stays within the screen bounds
      if (shape.x < 0) shape.x = 0;
      if (shape.x > UFOGameStart.SCREEN_WIDTH - UFO_WIDTH) shape.x = UFOGameStart.SCREEN_WIDTH - UFO_WIDTH;
      if (shape.y < 0) shape.y = 0;
      if (shape.y > UFOGameStart.SCREEN_HEIGHT - UFO_HEIGHT) shape.y = UFOGameStart.SCREEN_HEIGHT - UFO_HEIGHT;
   }

   
//   public void render() {
//      game.batch.draw(bucketImage, bucket.x, bucket.y);
//   }
}
