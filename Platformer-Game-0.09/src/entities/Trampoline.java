package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Trampoline extends Entity 
{
	private static Random random = new Random();
	
	private static Player player;
	
	//Matriz de localizacao dos sprites
	private BufferedImage[][] animations;
	
	//velocidade de animacao e qual sprite sera exibido na tela
	private int jumpValue=10;

	private int jumpTimer=jumpValue;
	private boolean jumpTrigger=false;
	
	private int[][] levelData;
	
	//Configurações do quanto o sprite se desloca da Hitbox
	private float xDrawOffset = 2 * Game.SCALE;
	private float yDrawOffset = 0 * Game.SCALE;
	
	private int restartValue = 10, restartTimer = restartValue;
	
	private float x,y;

	private int yUp=15;
	private boolean restartTrigger=true;
	
	private int xs;
	private static int saveSpringRandomize = 20;

	
	//se escolhe o primeiro numero da array que deseja aleatorizar, e o segundo valor é quantos sons tem
	public static void randomizeSpringsSounds() 
	{
		int randomize = 20;
		randomize += random.nextInt(7);
		
		while (randomize == saveSpringRandomize)
		{
			randomize=20;
			randomize += random.nextInt(7);
		}
		
		saveSpringRandomize=randomize;
		AudioPlayer.playEffect(randomize);
	}
	
	public Trampoline(float xValue, float yValue, Player player) 
	{
		super((int)((xValue) * (Game.SCALE)), (int)((yValue) * (Game.SCALE)), (int)(16 * (Game.SCALE)), (int)(32 * (Game.SCALE)));
		loadAnimations();
		//Tamanho da hitbox do Checkpoint
		initHitbox((xValue+2)  * Game.SCALE, (yValue+15)  * Game.SCALE,(int) (12 * Game.SCALE), (int) (1 * Game.SCALE));

		this.player = player;
		
		x=xValue;
		y=yValue;
		
		xs=(int) x/Game.TILES_SIZE;
	}
	
	public void draw(Graphics g, int xLevelOffset, int yLevelOffset, Player player)
	{
		this.player = player;
		
		int rangeX = xLevelOffset/Game.TILES_SIZE;
		int rangeY = yLevelOffset/Game.TILES_SIZE;
		
		int xw = 16/Game.TILES_SIZE;
		int yw = 16/Game.TILES_SIZE;
		
		//Sistema de carregar apenas o que está dentro da tela
		//Mude debug para 1 ou maior para visualizar em jogo
		if (((xs+xw) > ((rangeX - 3 + Playing.debugPropsDraw)/Game.SCALE))         
		&& ((xs)    < ((rangeX + 21 - Playing.debugPropsDraw)/Game.SCALE)))
			g.drawImage(animations[0][0],
				(int) ((hitbox.x - xDrawOffset) - Playing.xLevelOffset),
				(int) (hitbox.y - yDrawOffset - (yUp*Game.SCALE)) - Playing.yLevelOffset,
				width, height, null);

		BufferedImage image = LoadSave.GetSpriteAtlas("/TrampolineTileset.png");
		
		//Matriz dos sprites
		BufferedImage[][] subImage = new BufferedImage[1][4];
		
		for (int j = 0; j < subImage.length; j++)
			for (int i = 0; i < subImage[j].length; i++)
				subImage[j][i] = image.getSubimage(i * 16, j * 16, 16, 16);
		


		drawHitbox(g, Color.ORANGE, xLevelOffset, yLevelOffset);
		
		checkTrashTouched(player.getHitbox());
	}
	
	public void update()
	{
		if(!Playing.paused)
		{
			if (jumpTrigger)
			{
				if (jumpTimer>9)
				{
					randomizeSpringsSounds();
					player.jumpSpeed=player.jumpSpeed * 1.85f;
					Player.dash=0;
					Player.canDashAgain=true;
					player.jump();
				}
				else
				{
					player.jumpSpeed=player.jumpSpeed * 0.95f;
					player.jump();
				}
				jumpTimer--;
				yUp=20;
			}
			else
			{
				if (yUp>15)
					yUp-=1;
				else
					yUp=15;
			}
			
			if (jumpTimer<0)
			{
				player.setJump(false);
				player.setJumpTrigger(false);
				jumpTimer=jumpValue;
				jumpTrigger=false;
			}
		}
	}
	
	public void checkTrashTouched(Rectangle2D.Float hitbox)
	{
		if (this.hitbox.intersects(hitbox))
		{
			jumpTrigger=true;
		}
	}
	
	private void loadAnimations() 
	{
		BufferedImage image = LoadSave.GetSpriteAtlas("/Trampoline.png");

		//Matriz dos sprites
		animations = new BufferedImage[1][1];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = image.getSubimage(i * 16, j * 32, 16, 32);
	}
}
