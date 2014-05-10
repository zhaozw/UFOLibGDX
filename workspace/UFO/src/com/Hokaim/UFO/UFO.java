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
import com.badlogic.gdx.math.Vector3;

public class UFO {
   
   public static final int UFO_WIDTH = 64;
   public static final int UFO_HEIGHT = 64;
   
   static boolean isMoving = false;
      
   Texture UFOImage;
   Texture UFOImageTest;
   Rectangle shape;
   Circle test;
   Sprite UFOSprite;
   float rotation; //Seems to be at 100 = 1/4 rotations per second 
   
   OrthographicCamera camera;
   
   public UFO() {
      camera = new OrthographicCamera();
      camera.setToOrtho(false, UFOGameStart.SCREEN_WIDTH, UFOGameStart.SCREEN_HEIGHT);
      
      UFOImageTest = new Texture(Gdx.files.internal("Textures/Spaceship Alpha 2.png"));
      UFOImage = new Texture(Gdx.files.internal("Textures/Spaceship Alpha 2 small.png"));

      shape = new Rectangle();
      shape.set(200, 200, UFO_WIDTH, UFO_HEIGHT);
      
      test = new Circle();
      test.setRadius(UFO_WIDTH / 2);
      
      UFOSprite = new Sprite(UFOImageTest);
      UFOSprite.setSize(UFO_WIDTH, UFO_HEIGHT);
      UFOSprite.setOrigin(UFO_WIDTH / 2, UFO_HEIGHT / 2);
      UFOSprite.setRotation(0);
      rotation = 1;
   }
   
   /*
    * updates the rotation depending on time and the set rotation coefficient
    */
   public void updateRotation() {
      UFOSprite.rotate(rotation * 360 * Gdx.graphics.getDeltaTime());
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
         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camera.unproject(touchPos);
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
         touchPos.set(Gdx.input.getX(),Gdx.input.getY(), 0);
         camera.unproject(touchPos);
         shape.x = touchPos.x - UFO_WIDTH / 2;
         shape.y = touchPos.y - UFO_HEIGHT / 2;
      }

      if (Gdx.input.isKeyPressed(Keys.LEFT)) shape.x -= 200 * Gdx.graphics.getDeltaTime();
      if (Gdx.input.isKeyPressed(Keys.RIGHT)) shape.x += 200 * Gdx.graphics.getDeltaTime();
      if (Gdx.input.isKeyPressed(Keys.DOWN)) shape.y -= 200 * Gdx.graphics.getDeltaTime();
      if (Gdx.input.isKeyPressed(Keys.UP)) shape.y += 200 * Gdx.graphics.getDeltaTime();

      // make sure the bucket stays within the screen bounds
      if (shape.x < 0) shape.x = 0; //left bounds
      if (shape.x > UFOGameStart.SCREEN_WIDTH - UFO_WIDTH) shape.x = UFOGameStart.SCREEN_WIDTH - UFO_WIDTH;
      if (shape.y < 0) shape.y = 0; //bottom bounds
      if (shape.y > UFOGameStart.SCREEN_HEIGHT - UFO_HEIGHT) shape.y = UFOGameStart.SCREEN_HEIGHT - UFO_HEIGHT;
      
      UFOSprite.setX(shape.x);
      UFOSprite.setY(shape.y);
      test.setPosition(shape.x + (UFO_WIDTH / 2), shape.y + (UFO_HEIGHT / 2));
   }
   
   public boolean collides(Circle circle) {
      return test.overlaps(circle);
   }
   
   public boolean collides(Rectangle rectangle) {

      if (Intersector.overlaps(test, rectangle)) {
         return true;
      }
//      if (test.contains(rectangle.x, rectangle.y)
//            || test.contains(rectangle.x + rectangle.width, rectangle.y)
//            || test.contains(rectangle.x, rectangle.y + rectangle.height)
//            || test.contains(rectangle.x + rectangle.width, rectangle.y + rectangle.height)) {
//         return true;
//      }
      return false;
   }
}
