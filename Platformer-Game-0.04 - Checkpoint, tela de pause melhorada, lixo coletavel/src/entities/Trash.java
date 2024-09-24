package entities;

import static utilz.HelpMethods.CanMoveHere;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Trash extends Entity
{
	private Player player;
	private MiniPlayer miniplayer;
	
	//Matriz de localizacao dos sprites
	private BufferedImage[][] animations;
	private boolean active = true;
	
	private int[][] levelData;
	
	//Configurações do tamanho da Hitbox
	private float xDrawOffset = 1f * Game.SCALE;
	private float yDrawOffset = 4f * Game.SCALE;
	
	private float saveWidthandHeight=16;
	private float saveX;
	private float saveY;
	private boolean pickupTrigger = false;
	private int pickupTimer=50;
	
	public Trash( String fileName, float x, float y, Player player, MiniPlayer miniplayer) 
	{
		super((int)(x * (Game.SCALE)), (int)(y * (Game.SCALE)), (int)(16 * (Game.SCALE)), (int)(16 * (Game.SCALE)));
		loadAnimations(fileName);
		//Tamanho da hitbox do saco de lixo
		initHitbox((x)  * Game.SCALE, (y+4)  * Game.SCALE,(int) (13 * Game.SCALE), (int) (17 * Game.SCALE));
		
		this.player = player;
		this.miniplayer = miniplayer;
		
		saveX=x;
		saveY=y;
	}

	private void loadAnimations(String fileName) 
	{		
		BufferedImage image = LoadSave.GetSpriteAtlas(fileName);
		
		//Matriz dos sprites
		animations = new BufferedImage[1][4];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = image.getSubimage(i * 16, j * 16, 16, 16);
	}
	
	public void draw(Graphics g, int xLevelOffset, int yLevelOffset, Player player, MiniPlayer miniplayer)
	{
		this.player = player;
		this.miniplayer = miniplayer;
		
		if (active==true)
		{
			//drawHitbox(g);
			checkTrashTouched(player.getHitbox());
			g.drawImage(animations[0][Playing.trashAnimationIndex],
					(int) ((hitbox.x - xDrawOffset) - xLevelOffset),
					(int) (hitbox.y - yDrawOffset) - yLevelOffset,
					width, height, null);
		}
	}
	
	public void update()
	{

		if (pickupTrigger==true)
		{
			pickupTimer--;
			
			if (pickupTimer<=0)
			{
				active=false;
			}
			
			//Animação de encolher o lixo
			saveX+=0.25;
			saveY+=0.25;
			
			if (saveWidthandHeight>0)
				saveWidthandHeight-=0.45;
			
			
			initHitbox((saveX)  * Game.SCALE, (saveY+4)  * Game.SCALE,(int) (13 * Game.SCALE), (int) (17 * Game.SCALE));
			super.setWidthandHeight((int)(saveWidthandHeight), (int) saveWidthandHeight);
		}
	}

	public void checkTrashTouched(Rectangle2D.Float hitbox)
	{
		if (this.hitbox.intersects(hitbox))
		{
			pickupTrigger=true;
		}
	}
	
	
}