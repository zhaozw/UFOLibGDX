package com.Hokaim.UFO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class UFO {
   
   public static final int UFO_WIDTH = 64;
   public static final int UFO_HEIGHT = 64;
   
   private static final int ACCEL_SMOOTH = 4;   // Poss. values, 0 and up, 0 = no smoothing, 3-5 is good range
                                                // Can also use to make slower ships, try 10
   
   static boolean isMoving = false;
      
   Texture UFOImage;
   Circle shape;
   Sprite sprite;
   float rotation; //Seems to be at 100 = 1/4 rotations per second 
   
   OrthographicCamera camera;
   
   public UFO(String textureName) {
      camera = new OrthographicCamera();
      camera.setToOrtho(false, UFOGameStart.SCREEN_WIDTH, UFOGameStart.SCREEN_HEIGHT);
      
      UFOImage = new Texture(Gdx.files.internal(textureName));
      
      shape = new Circle();
      shape.setRadius(UFO_WIDTH / 2);
      
      sprite = new Sprite(UFOImage);
      sprite.setSize(UFO_WIDTH, UFO_HEIGHT);
      sprite.setOrigin(UFO_WIDTH / 2, UFO_HEIGHT / 2);
      sprite.setRotation(0);
      sprite.setPosition(UFOGameStart.SCREEN_WIDTH / 2 - UFO_WIDTH / 2, 100);
      rotation = 1;
   }
   
   // updates the rotation depending on current position, time and the set rotation coefficient
   public void updateRotation() {
      sprite.rotate(rotation * 360 * Gdx.graphics.getDeltaTime());
   }
   
   public void moveUFO() {
      Vector2 location = new Vector2(sprite.getX(), sprite.getY());
      
      // Process accelerometer input
      //TODO: have other people help determine best ACCEL_SMOOTH value for good balance of speed and smoothness
      //TODO: also may want to determine if this is the best way for smoothing
      if (UFOGameStart.prefs.getBoolean("useAccel") && Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
          location.x = ((Gdx.input.getAccelerometerY() * UFOGameStart.SCREEN_WIDTH / 10 + UFOGameStart.SCREEN_WIDTH / 2) + location.x * ACCEL_SMOOTH) / (ACCEL_SMOOTH + 1);
          location.y = ((Gdx.input.getAccelerometerX() * UFOGameStart.SCREEN_HEIGHT / 10 * -1 + UFOGameStart.SCREEN_HEIGHT / 2) + location.y * ACCEL_SMOOTH) / (ACCEL_SMOOTH + 1);
      }
      
      // Process touchscreen input
      if (Gdx.input.justTouched()) {
         Vector3 touchPos = new Vector3();
         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camera.unproject(touchPos);
         if (touchPos.x >= location.x - (UFO_WIDTH / 2) && touchPos.x <= location.x + UFO_WIDTH * 3 / 2
               && touchPos.y >= location.y - (UFO_HEIGHT / 2) && touchPos.y <= location.y + UFO_HEIGHT * 3 / 2) {
            isMoving = true;
         }
         else {
            isMoving = false;
         }
      }
      
      if (Gdx.input.isTouched() && isMoving) {
         Vector3 touchPos = new Vector3();
         touchPos.set(Gdx.input.getX(),Gdx.input.getY(), 0);
         camera.unproject(touchPos);
         location.x = touchPos.x - UFO_WIDTH / 2;
         location.y = touchPos.y - UFO_HEIGHT / 2;
      }

      //Keyboard input movement
      if (Gdx.input.isKeyPressed(Keys.LEFT))  location.x -= 200 * Gdx.graphics.getDeltaTime();
      if (Gdx.input.isKeyPressed(Keys.RIGHT)) location.x += 200 * Gdx.graphics.getDeltaTime();
      if (Gdx.input.isKeyPressed(Keys.DOWN))  location.y -= 200 * Gdx.graphics.getDeltaTime();
      if (Gdx.input.isKeyPressed(Keys.UP))    location.y += 200 * Gdx.graphics.getDeltaTime();

      // make sure the bucket stays within the screen bounds
      if (location.x < 0) location.x = 0; //left bounds
      if (location.x > UFOGameStart.SCREEN_WIDTH - UFO_WIDTH) location.x = UFOGameStart.SCREEN_WIDTH - UFO_WIDTH;
      if (location.y < 0) location.y = 0; //bottom bounds
      if (location.y > UFOGameStart.SCREEN_HEIGHT - UFO_HEIGHT) location.y = UFOGameStart.SCREEN_HEIGHT - UFO_HEIGHT;
      
      //Set Sprite and shape location based on location
      //TODO: can replace location with just sprite for calculations in future
      sprite.setX(location.x);
      sprite.setY(location.y);
      shape.setPosition(location.x + (UFO_WIDTH / 2), location.y + (UFO_HEIGHT / 2));
   }
   
   // Uses collision libraries to determine collision for circles
   public boolean collides(Circle circle) {
      return Intersector.overlaps(shape, circle);
   }
   
   // Uses collision libraries to determine collision for rectangles
   public boolean collides(Rectangle rectangle) {
      return Intersector.overlaps(shape, rectangle);
   }
}
