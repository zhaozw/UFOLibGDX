package com.Hokaim.UFO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
   
   public static final int PROJECTILE_WIDTH = 64;
   public static final int PROJECTILE_HEIGHT = 64;
      
   Texture image;
   Rectangle shape;
   Sprite sprite;
   float rotation;
   Vector2 direction;

   public Projectile() {
      image = new Texture(Gdx.files.internal("Textures/droplet.png"));
      sprite = new Sprite(image);
      sprite.setSize(PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
      sprite.setOrigin(PROJECTILE_WIDTH / 2, PROJECTILE_HEIGHT / 2);
      sprite.setRotation(45);
   }
   
   public Projectile(float x, float y, float velX, float velY) {
      image = new Texture(Gdx.files.internal("Textures/droplet.png"));
      sprite = new Sprite(image);
      sprite.setSize(PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
      sprite.setPosition(x, y);
      sprite.setOrigin(PROJECTILE_WIDTH / 2, PROJECTILE_HEIGHT / 2);
      direction = new Vector2(velX, velY);
      sprite.setRotation((direction.angle() + 90) % 360);
   }
   
   public void updateProjectile() {
      sprite.setX(sprite.getX() + (direction.x * Gdx.graphics.getDeltaTime()));
      sprite.setY(sprite.getY() + (direction.y * Gdx.graphics.getDeltaTime()));
   }

}
