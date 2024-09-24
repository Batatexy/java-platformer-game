package entities;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class CheckPoint extends Entity 
{
	private Player player;
	
	//Matriz de localizacao dos sprites
	private BufferedImage[][] animations;
	
	//velocidade de animacao e qual sprite sera exibido na tela
	private int animationTick, animationIndex, animationSpeed = 30;
	private int checkpointAction = 0;
	
	private int[][] levelData;
	
	//Configurações do quanto o sprite se desloca da Hitbox
	private float xDrawOffset = 20 * Game.SCALE;
	private float yDrawOffset = 18 * Game.SCALE;
	
	private int restartValue = 100, restartTimer = restartValue;
	private float saveX, saveY;
	private boolean restartTrigger=true;
	public static boolean resetCheckpoint=false;

	public CheckPoint(float x, float y, Player player) 
	{
		
		
		super((int)(x * (Game.SCALE)), (int)((y) * (Game.SCALE)), (int)(64 * (Game.SCALE)), (int)(64 * (Game.SCALE)));
		loadAnimations();
		//Tamanho da hitbox do Checkpoint
		initHitbox((x+4)  * Game.SCALE, (y-30)  * Game.SCALE,(int) (7 * Game.SCALE), (int) (47 * Game.SCALE));
		
		this.player = player;
		
		saveX=(int)((x-9) * (Game.SCALE));
		saveY=(int)((y+4) * (Game.SCALE));
	}
	
	private void loadAnimations() 
	{
		BufferedImage image = LoadSave.GetSpriteAtlas("/CheckPointFlag.png");

		//Matriz dos sprites
		animations = new BufferedImage[3][19];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = image.getSubimage(i * 64, j * 64, 64, 64);
	}

	private void updateAnimationTick() 
	{
		animationTick++;
		if (animationTick >= animationSpeed) 
		{
			animationTick = 0;
			animationIndex++;
			
			if (checkpointAction == 0)
			{
				animationIndex = 0;
			}
			else if (checkpointAction == 1)
			{
				if (animationIndex >= 19) 
				{
					animationIndex = 0;
					checkpointAction=2;
					resetAnimationTick();
				}
			}
			else if (checkpointAction == 2)
			{
				if (animationIndex >= 6) 
				{
					animationIndex = 0;
					resetAnimationTick();
				}
			}


		}
	}
	
	private void setAnimation() 
	{
		int startAnimation = checkpointAction;


		if (checkpointAction == 0)
			animationSpeed=0;
		else if (checkpointAction == 1)
			animationSpeed=5;
		else if (checkpointAction == 2)
			animationSpeed=10;
	}

	private void resetAnimationTick() 
	{
		animationTick = 0;
		animationIndex = 0;
	}


	public void update() 
	{
		if(!Playing.paused)
		{
			updateAnimationTick();
			setAnimation();
			
			if (resetCheckpoint)
				checkpointAction=0;
		}
	}


	public void draw(Graphics g, int xLevelOffset, int yLevelOffset, Player player)
	{
		this.player = player;
		
		drawHitbox(g, Color.YELLOW);
		
		g.drawImage(animations[checkpointAction][animationIndex],
				(int) ((hitbox.x - xDrawOffset) - Playing.xLevelOffset),
				(int) (hitbox.y - yDrawOffset) - Playing.yLevelOffset,
				width, height, null);
		
		checkTrashTouched(player.getHitbox());	
	}

	public void checkTrashTouched(Rectangle2D.Float hitbox)
	{
		if (this.hitbox.intersects(hitbox))
		{
			//checkPoint.resetTrigger();
			
			if (restartTrigger)
			{
				Player.lastX=(int) saveX;
				Player.lastY=(int) saveY;
				
				checkpointAction=1;
				resetAnimationTick();
				restartTrigger=false;
				resetCheckpoint=false;
			}
		}
		
		if (resetCheckpoint)
			restartTrigger=true;
	}
	


	public static void restartAllCheckpoints() 
	{
		resetCheckpoint=true;
	}
}