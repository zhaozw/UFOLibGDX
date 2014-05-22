package com.Hokaim.UFO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {
   
   Texture backgroundImage;
   TextureRegion backgroundRegion;
   Sprite backgroundSprite;

   public Background() {
      
      backgroundImage = new Texture(Gdx.files.internal("Backgrounds/Catalina.PNG"));
      backgroundImage.setFilter(TextureFilter.Linear, TextureFilter.Linear);
   
      backgroundRegion = new TextureRegion(backgroundImage, 0, 0, 512, 512);

      backgroundSprite = new Sprite(backgroundRegion);
      backgroundSprite.setSize(0.9f, 0.9f * backgroundSprite.getHeight() / backgroundSprite.getWidth());
      backgroundSprite.setOrigin(backgroundSprite.getWidth()/2, backgroundSprite.getHeight()/2);
      backgroundSprite.setPosition(-backgroundSprite.getWidth()/2, -backgroundSprite.getHeight()/2);
   }
   
   public Background(String image, int width, int height) {
      
      backgroundImage = new Texture(Gdx.files.internal(image));
      backgroundImage.setFilter(TextureFilter.Linear, TextureFilter.Linear);
   
      backgroundRegion = new TextureRegion(backgroundImage, 0, 0, width, height);

      backgroundSprite = new Sprite(backgroundRegion);
      backgroundSprite.setSize(0.9f, 0.9f * backgroundSprite.getHeight() / backgroundSprite.getWidth());
      backgroundSprite.setOrigin(backgroundSprite.getWidth()/2, backgroundSprite.getHeight()/2);
      backgroundSprite.setPosition(-backgroundSprite.getWidth()/2, -backgroundSprite.getHeight()/2);
   }
}
