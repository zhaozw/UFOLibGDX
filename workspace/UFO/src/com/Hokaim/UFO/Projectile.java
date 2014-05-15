package com.Hokaim.UFO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
   
   public static final int PROJECTILE_WIDTH = 64;
   public static final int PROJECTILE_HEIGHT = 64;
      
   Texture image;
   Circle shape;
   Sprite sprite;
   float rotation;
   Vector2 direction;
   Vector2 acceleration;
   
   public Projectile() {
      image = new Texture(Gdx.files.internal("Textures/droplet.png"));
      
      sprite = new Sprite(image);
      sprite.setSize(PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
      sprite.setOrigin(PROJECTILE_WIDTH / 2, PROJECTILE_HEIGHT / 2);
      sprite.setRotation(45);
      
      shape = new Circle();
      shape.setRadius((PROJECTILE_WIDTH + PROJECTILE_HEIGHT) / 4);
   }
   
   public Projectile(float x, float y, float velX, float velY) {
      image = new Texture(Gdx.files.internal("Textures/droplet.png"));
      
      sprite = new Sprite(image);
      sprite.setSize(PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
      sprite.setPosition(x, y);
      sprite.setOrigin(PROJECTILE_WIDTH / 2, PROJECTILE_HEIGHT / 2);
      
      direction = new Vector2(velX, velY);
      acceleration = new Vector2(0, 0);
      sprite.setRotation((direction.angle() + 90) % 360);
      
      shape = new Circle();
      shape.setRadius((PROJECTILE_WIDTH + PROJECTILE_HEIGHT) / 4);
   }
   
   public void updateProjectile() {
//      acceleration.x = Gdx.input.getAccelerometerY() * UFOGameStart.SCREEN_WIDTH / 10 + UFOGameStart.SCREEN_WIDTH / 2;
//      acceleration.y = Gdx.input.getAccelerometerX() * UFOGameStart.SCREEN_HEIGHT / 10 * -1 + UFOGameStart.SCREEN_HEIGHT / 2;

      //trying to figure out which method of accel. movement i like better
      //i think ill use the true version, allows me to preset accelerations
      if (true) {
         acceleration.x += Gdx.input.getAccelerometerY() * 1;
         acceleration.y += Gdx.input.getAccelerometerX() * -1;
      }
      else {
         acceleration.x = Gdx.input.getAccelerometerY() * 30;
         acceleration.y = Gdx.input.getAccelerometerX() * -30;
      }
      
      direction.x += acceleration.x * Gdx.graphics.getDeltaTime();
      direction.y += acceleration.y * Gdx.graphics.getDeltaTime();
      sprite.setX(sprite.getX() + (direction.x * Gdx.graphics.getDeltaTime()));
      sprite.setY(sprite.getY() + (direction.y * Gdx.graphics.getDeltaTime()));
      sprite.setRotation((direction.angle() + 90) % 360);
      shape.x = sprite.getX() + (PROJECTILE_WIDTH / 2);
      shape.y = sprite.getY() + (PROJECTILE_HEIGHT / 2);
   }

}
