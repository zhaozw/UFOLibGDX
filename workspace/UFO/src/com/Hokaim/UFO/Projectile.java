package com.Hokaim.UFO;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public interface Projectile {

   public void updateProjectile();
   
   public void updateRotation();
   
   public boolean collides(Circle circle);
   
   public void draw(SpriteBatch batch);

   public float getX();

   public float getY();

   public float getWidth();

   public float getHeight();

}