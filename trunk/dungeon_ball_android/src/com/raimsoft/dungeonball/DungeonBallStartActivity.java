package com.raimsoft.dungeonball;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.raimsoft.dungeonball.core.DungeonBall;

public class DungeonBallStartActivity extends AndroidApplication
{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		initialize(new DungeonBall(), false); 
	}

}
