package com.raimsoft.dungeonball.obj;

import com.badlogic.gdx.math.Rectangle;

public class Stone extends BaseWall
{

	public Stone(Rectangle rect)
	{
		super(rect);
		
		this.fFric= 9500f;
	}

}
