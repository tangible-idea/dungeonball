package com.raimsoft.dungeonball.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.raimsoft.dungeonball.tiled.TiledMapHelper2;

public class Renderer
{
	private float FPS= 0.0f;				// 초당 프레임
	//private float LOOP= 0.0f;				// 원루프에 걸리는 시간
	
	private final float CHAR_WID= 48f;		// 캐릭터 가로값
	private final float CHAR_HEI= 48f;		// 캐릭터 세로값
	
	private final float V_GRAVITY_Y= -70f;	// 중력값(Y)
	private final float V_JUMP= 12550f;		// 캐릭터 점프 벡터값 (Y)
	private final float V_VELOCITY= 5f;	// 캐릭터 좌우 속도 (X)
	private final int   N_DENCITY= 10;		// 캐릭터 밀도 (무게?)
	private final float V_DAMPING= 7.0f;	// 댐핑
	
	/**
	 * Box2d works best with small values. If you use pixels directly you will
	 * get weird results -- speeds and accelerations not feeling quite right.
	 * Common practice is to use a constant to convert pixels to and from
	 * "meters".
	 */
	// Box2d에서는 작은 값으로 동작하는 것이 좋다. 픽셀을 미터단위로 변환해주는 상수 
	public static final float PIXELS_PER_METER = 60.0f;
	
	
	// 마지막 렌더 시각 (frame-rate에 활용)
	private long now;
	private long lastRender;

	// 맵에서 그려지지 않은 별도의 텍스쳐들
	private Texture overallTexture;

	// 캐릭터 스프라이트
	private Sprite sprite_ball;

	// 스프라이트를 그릴 때 쓰는 도구
	private SpriteBatch spriteBatch, txtBatch;

	// box2d 에서 "container" 오브젝트.
	// 모든 "Body"는 이 안에 그려진다. 모든 오브젝트가 World를 통해 시뮬레이트된다. 
	private World world;

	// 캐릭터의 물리적 Body
	private Body body_char;

	// Box2DDebugRenderer는 libgdx에서 테스트코드이다.
	// world의 충돌 바운더리를 만들어준다.
	// 테스트용이기 때문에 다소 느리다.
	private Box2DDebugRenderer debugRenderer;
	// 프레임 표시용 텍스트
	private BitmapFont txt_frame;

	
	private TiledMapHelper2 tiledMapHelper;
	
	int wid, hei;
	
	public Renderer(int w, int h)
	{
//		NinePatch up= new NinePatch(Assets.button_1);
//		NinePatch down= new NinePatch(Assets.button_2);
//
//		ButtonStyle style= new ButtonStyle();
//		style.up= up;
//		style.down= down;
//		
//		btn_retry= new Button(style,"Retry");
		this.wid= w;
		this.hei= h;
		
		tiledMapHelper = new TiledMapHelper2();
		
		tiledMapHelper.setCommonPackFile("data");

		tiledMapHelper.loadMap("data/world/stage1.tmx");
		
		//모든 텍스쳐를 불러온다.
		overallTexture = new Texture(Gdx.files.internal("data/ball_char1.png"));
		overallTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		sprite_ball = new Sprite(overallTexture, 0, 0, (int)CHAR_WID, (int)CHAR_HEI);

		spriteBatch = new SpriteBatch();
		txtBatch = new SpriteBatch();

		// world의 중력값을 이 생성자를 통해서 입력할 수 있다. (x,y 방향 입력 가능)
		world = new World(new Vector2(0.0f, V_GRAVITY_Y), true);

		// 바디의 정보들을 넣어준다.
		BodyDef jumperBodyDef = new BodyDef();
		jumperBodyDef.type = BodyDef.BodyType.DynamicBody;	// 다이나믹바디이고
		jumperBodyDef.position.set(5.0f, 1.0f);			// 처음 위치정보

		body_char = world.createBody(jumperBodyDef);		// 생성한 정보를 Body에 대입

		/**
		 * Boxes are defined by their "half width" and "half height", hence the
		 * 2 multiplier.
		 */
		// 
		//CircleShape ballShape= new CircleShape();
//		PolygonShape jumperShape = new PolygonShape();
//		jumperShape.setAsBox(CHAR_WID / (2 * PIXELS_PER_METER), CHAR_HEI / (2 * PIXELS_PER_METER));
		CircleShape SHP_char= new CircleShape();
		SHP_char.setRadius(48f / (2*PIXELS_PER_METER) );

		
		// 캐릭터의 회전을 고정시킬 것인가?
		body_char.setFixedRotation(true);

		
		// 캐릭터의 밀도 : 70, 실험해볼 때 가장 적합한 값.
		body_char.createFixture(SHP_char, N_DENCITY);
		SHP_char.dispose();

		body_char.setLinearVelocity(new Vector2(0.0f, 0.0f));	// 캐릭터 바디의 기본속도를 입력
		body_char.setLinearDamping(V_DAMPING);						// 이것도 비슷한 것 같다.

		// 충돌 정보를 맵에 넣어준다.
		tiledMapHelper.loadCollisions("data/collisions.txt", world,	PIXELS_PER_METER);
		

		debugRenderer = new Box2DDebugRenderer();
		txt_frame =  new BitmapFont();
		
		lastRender = System.nanoTime();
	}
	
	
	public void Render()
	{
		now  = System.nanoTime();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		this.RenderMap();
		this.RenderChar();		
		this.RenderButton();
		
		/**
		 * Draw this last, so we can see the collision boundaries on top of the
		 * sprites and map.
		 */
		// debugRenderer.render(world);

		now = System.nanoTime();
		
		this.RenderFrame();
		
		FPS=Gdx.graphics.getFramesPerSecond();
		
		
		
		
//		if (now - lastRender < 30000000)
//		{ // 30 ms, ~33FPS
//			try
//			{
//				Thread.sleep(30 - (now - lastRender) / 1000000);
//			}
//			catch (InterruptedException e)	{}
//		}

		lastRender = now;
		
	
	}	
	
	private void RenderFrame()
	{
		txtBatch.begin();
		txt_frame.draw(txtBatch, "FPS : "+Gdx.graphics.getFramesPerSecond() , 10, Gdx.graphics.getHeight()-10 );
		txtBatch.end();
	}
	
	private void RenderChar()
	{
		//Gdx.app.log("char pos", " X : "+ body_char.getPosition().x + " Y : "+body_char.getPosition().y);
		
//		Gdx.app.log("Acceleromenter", "X : "+Gdx.input.getAccelerometerX()+
//									  "Y : "+Gdx.input.getAccelerometerY()+
//									  "Z : "+Gdx.input.getAccelerometerZ() );

		sprite_ball.setRotation(-7*Gdx.input.getAccelerometerY());
		
		body_char.applyForce(new Vector2(V_VELOCITY*FPS* Gdx.input.getAccelerometerY(), 0.0f), new Vector2(
				sprite_ball.getWidth() / (2 * PIXELS_PER_METER),
				sprite_ball.getHeight() / (2 * PIXELS_PER_METER)));
		
		if(Gdx.input.isTouched())
		{
			if(Gdx.input.getX() > 400)	// 왼쪽 터치시
			{
				body_char.applyForce(new Vector2(V_VELOCITY*FPS, 0.0f), new Vector2(
						sprite_ball.getWidth() / (2 * PIXELS_PER_METER),
						sprite_ball.getHeight() / (2 * PIXELS_PER_METER)));				
			}
			else	// 오른쪽 터치시
			{
				body_char.applyForce(new Vector2(-V_VELOCITY*FPS, 0.0f), new Vector2(
						sprite_ball.getWidth() / (2 * PIXELS_PER_METER),
						sprite_ball.getHeight() / (2 * PIXELS_PER_METER)));
			}
			//Gdx.app.log("touch", " X : "+Gdx.input.getX()+" Y : "+Gdx.input.getY());
		}


		/**
		 * The jumper dude can only jump while on the ground. There are better
		 * ways to detect ground contact, but for our purposes it is sufficient
		 * to test that the vertical velocity is zero (or close to it). As in
		 * the above code, the 4500N figure here was found through
		 * experimentation. It's enough to get the guy off the ground.
		 * 
		 * As before, force is applied to the center of the jumper.
		 */
		//if (Math.abs(jumper.getLinearVelocity().y) < 1e-9)
		
		if (body_char.getLinearVelocity().y == 0)
		//if (Math.abs(body_char.getLinearVelocity().y) < 1e-9)
		{	
			body_char.applyForce(new Vector2(0.0f, V_JUMP), new Vector2(
					sprite_ball.getWidth() / (2 * PIXELS_PER_METER),
					sprite_ball.getHeight() / (2 * PIXELS_PER_METER)));
		}

		
		/**
		 * Prepare the SpriteBatch for drawing.
		 */
		spriteBatch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		spriteBatch.begin();

		sprite_ball.setPosition( PIXELS_PER_METER * body_char.getPosition().x - sprite_ball.getWidth() / 2,
								 PIXELS_PER_METER * body_char.getPosition().y - sprite_ball.getHeight() / 2);
		sprite_ball.draw(spriteBatch);

		/**
		 * "Flush" the sprites to screen.
		 */
		spriteBatch.end();
	}
	
	private void RenderMap()
	{
		

//		if (Gdx.input.justTouched())
//		{
//			x = Gdx.input.getX();
//			y = Gdx.input.getY();
//		}
//		else if (Gdx.input.isTouched())
//		{
//			tiledMapHelper.getCamera().position.x += x	- Gdx.input.getX();
//
//			/**
//			 * Camera y is opposite of Gdx.input y, so the subtraction is
//			 * swapped.
//			 */
//			tiledMapHelper.getCamera().position.y += Gdx.input.getY() - y;
//
//			x = Gdx.input.getX();
//			y = Gdx.input.getY();
//		}
		
		
		
		
		

		/**
		 * Have box2d update the positions and velocities (and etc) of all
		 * tracked objects. The second and third argument specify the number of
		 * iterations of velocity and position tests to perform -- higher is
		 * more accurate but is also slower.
		 */
		world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
		
		
		// world의 카메라 컨트롤을 캐릭터의 움직임에 따라서 변경한다. (x값만 활용)
		tiledMapHelper.getCamera().position.x = PIXELS_PER_METER * body_char.getPosition().x;

		// 카메라가 맵을 벗어나면 안된다.
		if (tiledMapHelper.getCamera().position.x < wid / 2)
		{
			tiledMapHelper.getCamera().position.x = wid / 2;
		}
		if (tiledMapHelper.getCamera().position.x >= tiledMapHelper.getWidth() - wid / 2)
		{
			tiledMapHelper.getCamera().position.x = tiledMapHelper.getWidth() - wid / 2;
		}

		if (tiledMapHelper.getCamera().position.y < hei / 2)
		{
			tiledMapHelper.getCamera().position.y = hei / 2;
		}
		if (tiledMapHelper.getCamera().position.y >= tiledMapHelper.getHeight()	- hei / 2)
		{
			tiledMapHelper.getCamera().position.y = tiledMapHelper.getHeight() - hei / 2;
		}

		
		
		tiledMapHelper.getCamera().update();
		tiledMapHelper.render();
		
		//debugRenderer.render(world, tiledMapHelper.getCamera().combined);
	}
	
	private void RenderButton()
	{	
	}
}
