package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.MiniPlayer;
import entities.Player;
import main.Game;
import utilz.LoadSave;

public class AnimatedProp extends Entity
{
	private Player player;
	private MiniPlayer miniplayer;
	
	//Matriz de localizacao dos sprites
	private BufferedImage[][] animations;
	
	private int[][] levelData;
	
	//Configurações do tamanho da Hitbox
	private float xDrawOffset = 1f * Game.SCALE;
	private float yDrawOffset = 1f * Game.SCALE;
	
	private int frame=0, x, y, width, height, framesQnt, speed, tick=0;
	
	public AnimatedProp( String fileName, int x, int y, int width, int height, int framesQnt, int speed) 
	{
		super((int)(x * (Game.SCALE)), (int)(y * (Game.SCALE)), (int)(width * (Game.SCALE)), (int)((height/framesQnt) * (Game.SCALE)));
		//Tamanho da hitbox
		initHitbox((x)  * Game.SCALE, (y) * Game.SCALE,(int) (width * Game.SCALE), (int) ((height/framesQnt)* Game.SCALE));

		this.width = width;
		this.height = height;
		this.framesQnt = framesQnt;
		this.speed = speed;
		
		loadAnimations(fileName);
	}
	
	private void loadAnimations(String fileName) 
	{
		BufferedImage image = LoadSave.GetSpriteAtlas(fileName);
		
		//Matriz dos sprites
		animations = new BufferedImage[framesQnt][1];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = image.getSubimage(i * width, j * height/framesQnt, width, height/framesQnt);
	}
	
	public void draw(Graphics g, int xLevelOffset, int yLevelOffset, Player player, MiniPlayer miniplayer)
	{
		this.player = player;
		this.miniplayer = miniplayer;
			
		g.drawImage(animations[frame][0],
			(int) ((hitbox.x - xDrawOffset) - xLevelOffset),
			(int) (hitbox.y - yDrawOffset) - yLevelOffset,
			(int)(width * Game.SCALE), (int)((height/framesQnt) * Game.SCALE), null);
	}
	
	public void update()
	{
		tick++;
		
		if (tick>=speed)
		{
			tick=0;
			frame++;
		}
		
		if (frame >= framesQnt)
			frame=0;
	}
}