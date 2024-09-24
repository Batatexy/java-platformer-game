package entities;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
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
		initHitbox(x, y,(int) (5 * Game.SCALE), (int) (10 * Game.SCALE));
	}
	
//////////////////////FUNCOES DE SPRITES E ANIMACOES//////////////////////////////////////////////////////////////

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
	
//////////////////////FUNCOES DE ATUALIZACOES//////////////////////////////////////////////////////////////

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
	}

	
//////////////////////FUNCOES DE GAMEPLAY//////////////////////////////////////////////////////////////
	
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
	
	
	
	//Player nao comecar voando, forcando quem ta jogando a apertar algum botao para dai comecar o jogo de verdade
	public void loadLevelData(int[][] levelData) 
	{
		this.levelData = levelData;
	}

	private void updateXPosition(float xSpeed) 
	{
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) 
		{
			hitbox.x += xSpeed;
		} 
		else
		{
			hitbox.x = GetEntityXPositionNextToWall(hitbox, xSpeed);
		}

	}

	private void updateYPosition(float airSpeed) 
	{
		if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) 
		{
			hitbox.y += airSpeed;
		} 
		else
		{
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
		}
	}

	//Getters and Setters dos botões do teclado e mouse
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}