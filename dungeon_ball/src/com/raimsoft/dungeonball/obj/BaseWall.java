package com.raimsoft.dungeonball.obj;

import com.badlogic.gdx.math.Rectangle;

public class BaseWall
{
	Rectangle rect;
	int[] nTypes;
	float fFric;	// 마찰력 (높을수록 팅겨냄)

	/**
	 * @param rect
	 */
	public BaseWall(Rectangle rect)
	{
		super();
		this.rect = rect;
		fFric= 15500f;
		
		// tiled에서 맞는 타입을 찾아서 넣어주자 (아래는 임시)
		nTypes[0]= 1;
		nTypes[1]= 2;
		nTypes[2]= 3;
		nTypes[3]= 4;
	}
	
}
