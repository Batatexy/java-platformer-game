package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class OrbDash extends Entity 
{
	private static Player player;
	
	//Matriz de localizacao dos sprites
	private BufferedImage[][] animations;
	
	//velocidade de animacao e qual sprite sera exibido na tela
	private int orbValue=400;
	private int orbTimer=orbValue;
	private boolean orbTrigger=false;
	public static boolean staticOrbTrigger=false;
	
	private int index=0;
	
	private int[][] levelData;
	
	//Configurações do quanto o sprite se desloca da Hitbox
	private float xDrawOffset = 2 * Game.SCALE;
	private float yDrawOffset = 0 * Game.SCALE;

	private float x,y;

	private boolean restartTrigger=true;

	public OrbDash(float xValue, float yValue, Player player) 
	{
		super((int)((xValue) * (Game.SCALE)), (int)((yValue) * (Game.SCALE)), (int)(16 * (Game.SCALE)), (int)(16 * (Game.SCALE)));
		loadAnimations();
		//Tamanho da hitbox do Checkpoint
		initHitbox((xValue)  * Game.SCALE, (yValue)  * Game.SCALE,(int) (11 * Game.SCALE), (int) (14 * Game.SCALE));

		this.player = player;
		
		x=xValue;
		y=yValue;
	}
	
	public void draw(Graphics g, int xLevelOffset, int yLevelOffset, Player player)
	{
		this.player = player;
		
		drawHitbox(g, Color.ORANGE);
		
		g.drawImage(animations[0][index],
				(int) ((hitbox.x - xDrawOffset) - Playing.xLevelOffset),
				(int) (hitbox.y - yDrawOffset - (Game.SCALE)) - Playing.yLevelOffset,
				width, height, null);

		
		
		checkTrashTouched(player.getHitbox());	
	}
	
	public void update()
	{
		if(!Playing.paused)
		{
			if (orbTrigger)
			{
				if (orbTimer>orbValue-1)
					Player.canDashAgain=true;
				
				index=1;
				orbTimer--;
			}
			else
				index=0;
			
			if (orbTimer<0)
			{
				orbTimer=orbValue;
				orbTrigger=false;
			}
		}
		
		if (staticOrbTrigger)
		{
			orbTimer=orbValue;
			orbTrigger=false;
			staticOrbTrigger=false;
		}
	}
	
	public void checkTrashTouched(Rectangle2D.Float hitbox)
	{
		if (this.hitbox.intersects(hitbox) && (Player.canDashAgain==false))
		{
			orbTrigger=true;
		}
	}
	
	public static void resetAllOrbtriggers()
	{
		staticOrbTrigger=true;
	}
	
	private void loadAnimations() 
	{
		BufferedImage image = LoadSave.GetSpriteAtlas("/OrbDash.png");
		
		//Matriz dos sprites
		animations = new BufferedImage[1][2];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = image.getSubimage(i * 16, j * 16, 16, 16);
	}
}
