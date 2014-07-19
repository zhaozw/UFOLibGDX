package com.Hokaim.UFO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public class ProjDrop implements Projectile {
   
   public static final int PROJECTILE_WIDTH = 64;
   public static final int PROJECTILE_HEIGHT = 64;
      
   Texture image;
   Circle shape;
   Sprite sprite;
   float rotation;
   Vector2 direction;
   Vector2 acceleration;
   
   boolean isGravity = true;
   int gravity = 100;
   
   public ProjDrop() {
      image = new Texture(Gdx.files.internal("Textures/droplet.png"));
      
      sprite = new Sprite(image);
      sprite.setSize(PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
      sprite.setOrigin(PROJECTILE_WIDTH / 2, PROJECTILE_HEIGHT / 2);
      sprite.setRotation(45);
      
      shape = new Circle();
      shape.setRadius((PROJECTILE_WIDTH + PROJECTILE_HEIGHT) / 4);
   }
   
   public ProjDrop(float x, float y, float velX, float velY) {
      image = new Texture(Gdx.files.internal("Textures/droplet.png"));
      
      sprite = new Sprite(image);
      sprite.setSize(PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
      sprite.setPosition(x, y);
      sprite.setOrigin(PROJECTILE_WIDTH / 2, PROJECTILE_HEIGHT / 2);
      
      direction = new Vector2(velX, velY);
      acceleration = new Vector2(0, 0);
      sprite.setRotation(direction.angle());
      
      shape = new Circle();
      shape.setRadius((PROJECTILE_WIDTH + PROJECTILE_HEIGHT) / 4);
   }
   
   public void updateProjectile() {

      if (isGravity && UFOGameStart.prefs.getBoolean("useAccel")) {
         acceleration.x = Gdx.input.getAccelerometerY() * gravity;
         acceleration.y = Gdx.input.getAccelerometerX() * -gravity;
      }
      
      direction.x += acceleration.x * Gdx.graphics.getDeltaTime();
      direction.y += acceleration.y * Gdx.graphics.getDeltaTime();
      sprite.setX(sprite.getX() + (direction.x * Gdx.graphics.getDeltaTime()));
      sprite.setY(sprite.getY() + (direction.y * Gdx.graphics.getDeltaTime()));
      shape.x = sprite.getX() + (PROJECTILE_WIDTH / 2);
      shape.y = sprite.getY() + (PROJECTILE_HEIGHT / 2);
      updateRotation();
   }
   
   public void updateRotation() {
      sprite.setRotation(direction.angle());
   }
   
   public void draw(SpriteBatch batch) {
      sprite.draw(batch);
   }

   public float getX() {
      return sprite.getX();
   }

   public float getY() {
      return sprite.getY();
   }

   public float getWidth() {
      return sprite.getWidth();
   }

   public float getHeight() {
      return sprite.getHeight();
   }

   // Uses collision libraries to determine collision for circles
   public boolean collides(Circle circle) {
      return Intersector.overlaps(shape, circle);
   }

}