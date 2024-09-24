package entities;

import static utilz.Constants.PlayerConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.CutScene;
import main.Game;
import utilz.LoadSave;

public class MiniPlayer extends Entity 
{
	//Matriz de localizacao dos sprites
	private BufferedImage[][] animations;
	
	//velocidade de animacao e qual sprite sera exibido na tela
	private int animationTick, animationIndex, animationSpeed = 30;
	private int[][] levelData;
	
	//Animacoes e velocidade de movimento, estado do player, estados dos botoes
	private int playerAction = IDLE;
	private boolean moving = false;
	private boolean up, left, down, right;
	private float playerSpeed = 0.335f * Game.SCALE;
	
	//Inverter o personagem para cada direcao
	private int flipX = width;
	private int flipW = -1;
	private int flipAdd = -7;
	
	//Configurações do tamanho da Hitbox
	private float xDrawOffset = 1 * Game.SCALE;
	private float yDrawOffset = 1 * Game.SCALE;

	public MiniPlayer(float x, float y, int width, int height) 
	{
		super(x, y, width, height);
		loadAnimations();
		//Tamanho da hitbox do miniplayer
		initHitbox(x, y,(int) (5 * Game.SCALE), (int) (10 * Game.SCALE));
	}
	
	private void updateAnimationTick() 
	{
		animationTick++;
		if (animationTick >= animationSpeed) 
		{
			animationTick = 0;
			animationIndex++;
			
			if (animationIndex >= 2)
				animationIndex = 0;
		}
	}
	
	private void setAnimation() 
	{
		int startAnimation = playerAction;

		if (moving)
			playerAction = 1;
		else
			playerAction = 0;
		
		if (startAnimation != playerAction)
			resetAnimationTick();
	}
	
	private void resetAnimationTick() 
	{
		animationTick = 0;
		animationIndex = 0;
	}
	
	private void loadAnimations() 
	{
		BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.MINI_PLAYER_ATLAS);

		//Matriz dos sprites
		animations = new BufferedImage[2][2];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = image.getSubimage(i * 5, j * 10, 5, 10);
	}
	
	public void update() 
	{
		updatePosition();
		updateAnimationTick();
		setAnimation();
	}
	
	public void render(Graphics g)
	{
		g.drawImage(animations[playerAction][animationIndex], 
				(int) ((hitbox.x - xDrawOffset + flipX + flipAdd) - CutScene.xLevelOffset),
				(int) (hitbox.y - yDrawOffset +1) - CutScene.yLevelOffset,
				width * flipW, height, null);
		
		drawHitbox(g, Color.GREEN, CutScene.xLevelOffset, CutScene.yLevelOffset);
	}
	
	private void updatePosition() 
	{
		moving = false;
		
		if (!left && !right && !down && !up)
		{
			return;
		}
		else
			moving = true;
		
		if (right && !left)
		{
			hitbox.x += playerSpeed;
			flipAdd = -5;
			flipX = width;
			flipW = -1;
		}
		
		if (left && !right)
		{
			hitbox.x -= playerSpeed;
			flipAdd=0;
			flipX = 0;
			flipW = 1;
		}
		
		if (up && !down)
		{
			hitbox.y -= playerSpeed;
		}
		
		if (down && !up)
		{
			hitbox.y += playerSpeed;
		}
	}
	
	public void loadLevelData(int[][] levelData) 
	{
		this.levelData = levelData;
	}
	
	//Getters and Setters dos botões do teclado
	public boolean isUp()
	{
		return up;
	}
	
	public void setUp(boolean up)
	{
		this.up = up;
	}
	
	public boolean isLeft()
	{
		return left;
	}
	
	public void setLeft(boolean left) 
	{
		this.left = left;
	}
	
	public boolean isRight() 
	{
		return right;
	}
	
	public void setRight(boolean right) 
	{
		this.right = right;
	}
	
	public boolean isDown() 
	{
		return down;
	}
	
	public void setDown(boolean down) 
	{
		this.down = down;
	}
	
	public int getWidth() 
	{
		return width;
	}
	
	public void setWidth(int width) 
	{
		this.width = width;
	}
	
	public void setPlayerSpeed(float playerSpeed)
	{
		this.playerSpeed = playerSpeed * Game.SCALE;
	}
}