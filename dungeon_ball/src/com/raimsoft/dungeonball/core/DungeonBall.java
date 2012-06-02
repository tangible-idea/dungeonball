package com.raimsoft.dungeonball.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.raimsoft.dungeonball.screens.MainMenuScreen;

public class DungeonBall extends Game
{
	public static void main(String[] args)
	{
		new JoglApplication(new DungeonBall(),
							"dungeon ball",
							800, 480, 
							false);
	}

	@Override
	public void create()
	{
		setScreen(new MainMenuScreen(this));
	}
}
