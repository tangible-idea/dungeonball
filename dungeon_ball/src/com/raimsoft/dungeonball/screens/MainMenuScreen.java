package com.raimsoft.dungeonball.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.raimsoft.dungeonball.core.Renderer;
import com.raimsoft.dungeonball.core.Updater;

public class MainMenuScreen extends BaseScreen
{
	private int screenWidth;
	private int screenHeight;
	

	
	

	public MainMenuScreen(Game _game)
	{
		super(_game);	
		// Defer until create() when Gdx is initialized.
		screenWidth = -1;
		screenHeight = -1;
	}
	
	Renderer render;
	Updater updater;


	@Override
	public void show()
	{
		if (screenWidth == -1)
		{
			screenWidth = Gdx.graphics.getWidth();
			screenHeight = Gdx.graphics.getHeight();
		}
		
		render= new Renderer(screenWidth, screenHeight);
		updater= new Updater();
		
		//Assets.loadImagesFromAsset();
		

	}

	@Override
	public void hide()
	{
		
	}

	@Override
	public void render(float delta) 
	{
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		render.Render();
		updater.Update();
	}

	@Override
	public void resize(int width, int height)
	{
		//spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		
	}

	@Override
	public void resume()
	{
		
	}

}
