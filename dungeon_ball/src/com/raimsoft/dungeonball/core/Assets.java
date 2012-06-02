package com.raimsoft.dungeonball.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets
{
	public static Texture tiles;
	public static TextureRegion tile;
	
	public static Texture loadTexture (String file)
	{
		return new Texture(Gdx.files.internal(file));
	}
	
	public static boolean loadImagesFromAsset()
	{
//		tiles= loadTexture("images/tiles.gif");
//		tile= new TextureRegion(tiles,  0 , 0, 220,186);
		
		
		return true;
	}
}
