package entities;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity 
{
	
	//Matriz de localizacao dos sprites
	private BufferedImage[][] animations;
	
	//velocidade de animacao e qual sprite sera exibido na tela
	private int animationTick, animationIndex, animationSpeed = 30;
	
	private int[][] levelData;
	
	//Animacoes e velocidade de movimento, estado do player, estados dos botoes
	private int playerAction = IDLE;
	private boolean moving = false;
	private boolean up, left, down, right, jump, dashPressed;
	private float playerSpeed = 0.75f * Game.SCALE;
	private float xSpeed = 0;
	
	// Variaveis de pulo e gravidade
	private float airSpeed = 0f;
	
	private float wallDistance=3.5f * Game.SCALE;
	private boolean gravityTrigger=false;
	private float gravityValue = 0.06f;
	private float gravity = gravityValue;
	private float heavyGravity = 0.15f;
	private float lightGravity = 0.01f;
	private double wallJumpSpeed = 0.9;
	
	private boolean jumpTrigger=false;
	private float jumpSpeedValue = -1.6f * Game.SCALE;
	private float jumpSpeed = jumpSpeedValue;
	
	private float fallSpeedAfterCollision = 0.25f * Game.SCALE;
	public boolean inAir = false;

	private int dash = 0, verticalDirection = 0, horizontalDirection = 1, dashMinTime=10, dashMaxTime = 34;
	private float dashSpeed = 2.5f, divAirSpeedOne=3f, divAirSpeedTwo=1.25f;

	//Configurações do tamanho da Hitbox
	private float xDrawOffset = 5 * Game.SCALE;
	private float yDrawOffset = 8 * Game.SCALE;
	
	//Inverter o personagem para cada direcao
	private int flipX = width;
	private int flipW = -1;
	private int flipAdd = -7;
	
	
	
	

	
	public Player(float x, float y, int width, int height) 
	{
		super(x, y, width, height);
		loadAnimations();
		initHitbox(x, y,(int) (6 * Game.SCALE), (int) (10 * Game.SCALE));
	}
	
//////////////////////FUNCOES DE SPRITES E ANIMACOES//////////////////////////////////////////////////////////////

	private void updateAnimationTick() 
	{
		animationTick++;
		if (animationTick >= animationSpeed) 
		{
			animationTick = 0;
			animationIndex++;
			
			if (playerAction != CROUNCH && playerAction != FALLING)
			{
				if (animationIndex >= GetSpriteAmount(playerAction)) 
				{
					animationIndex = 0;
				}
			}
			else
			{
				if (animationIndex >= GetSpriteAmount(playerAction)) 
				{
					animationIndex = GetSpriteAmount(playerAction)-1;
				}
			}
		}
	}
	
	private void setAnimation() 
	{
		int startAnimation = playerAction;

		if (moving)
		{
			animationSpeed=10;
			playerAction = RUNNING;
		}
		
		else if (down)
		{
			animationSpeed=5;
			playerAction = CROUNCH;
			
		}
		else
		{
			animationSpeed=60;
			playerAction = IDLE;
		}

		if (inAir) 
		{
			if (airSpeed < 0)
			{
				if (down)
					setGravity(heavyGravity);
				else
					setGravity(gravityValue);
				animationSpeed=30;
				playerAction = JUMP;
			}
			else
			{
				if (!(CanMoveHere(hitbox.x + wallDistance, hitbox.y, hitbox.width, hitbox.height, levelData)) ||
					(!CanMoveHere(hitbox.x - wallDistance, hitbox.y, hitbox.width, hitbox.height, levelData))
					)
				{
					if (gravityTrigger==false)
					{
						resetInAir();
						setGravityTrigger(true);
					}
					playerAction = CLIMB;

					animationSpeed=30;
					
					if (down)
						setGravity(heavyGravity);
					else
						setGravity(lightGravity);
				}
				else
				{
					animationSpeed=15;
					playerAction = FALLING;
					setGravityTrigger(false);
					
					if (down)
						setGravity(heavyGravity);
					else
						setGravity(gravityValue);
				}
			}
		}
		

		


		if (startAnimation != playerAction)
			resetAnimationTick();
	}

	private void setGravityTrigger(boolean graviyTrigger) 
	{
		this.gravityTrigger=graviyTrigger;
	}

	private void resetAnimationTick() 
	{
		animationTick = 0;
		animationIndex = 0;
	}

	private void loadAnimations() 
	{
		BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

		//Matriz dos sprites
		animations = new BufferedImage[8][12];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = image.getSubimage(i * 19, j * 19, 19, 19);
	}
	
//////////////////////FUNCOES DE ATUALIZACOES//////////////////////////////////////////////////////////////

	public void update() 
	{
		updatePosition();
		updateAnimationTick();
		setAnimation();
		updateDash();
	}

	private void updateDash() {
		
		if (dash > 0)
			dash++;

			
		
		if (dash > dashMaxTime)
		{
			setInAir(true);
			
			//Verificar se o player cai no chao pra dai resetar a variavel
				if (IsEntityOnFloor(hitbox, levelData))
					dash=0;
		}
		
		if (dash > dashMinTime && dash < dashMaxTime)
		{
			airSpeed=0;
			
			
			

			
			
			
			
			//Direções unicas
			if ((verticalDirection == 0) && horizontalDirection == 1)
			{
				playerAction = DASH;
				xSpeed = (dashSpeed / divAirSpeedTwo) * Game.SCALE;
				
				//Direita
				flipAdd = -5;
				flipX = width;
				flipW = -1;
			}
			
			if ((verticalDirection == 0) && horizontalDirection == -1)
			{
				playerAction = DASH;
				xSpeed = (-dashSpeed / divAirSpeedTwo) * Game.SCALE;

				//Esquerda
				flipAdd=0;
				flipX = 0;
				flipW = 1;
			}
			
			if ((verticalDirection == 1) && horizontalDirection == 0)
			{
				playerAction = JUMP;
				airSpeed = (-dashSpeed/divAirSpeedOne) * Game.SCALE;
			}
			
			if ((verticalDirection == -1) && horizontalDirection == 0)
			{
				playerAction = CROUNCH;
				airSpeed = (dashSpeed) * Game.SCALE;
			}
			
			//Diagonais
			if ((verticalDirection == 1) && horizontalDirection == 1)
			{
				playerAction = DASH;
				xSpeed = (dashSpeed / 2f) * Game.SCALE;
				airSpeed = (-dashSpeed /divAirSpeedOne / 1.4f) * Game.SCALE;
				
				//Direita
				flipAdd = -5;
				flipX = width;
				flipW = -1;
			}
			
			if ((verticalDirection == 1) && horizontalDirection == -1)
			{
				playerAction = DASH;
				xSpeed = (-dashSpeed / 2f) * Game.SCALE;
				airSpeed = (-dashSpeed /divAirSpeedOne / 1.4f) * Game.SCALE;
				
				//Esquerda
				flipAdd=0;
				flipX = 0;
				flipW = 1;
			}
			
			if ((verticalDirection == -1) && horizontalDirection == 1)
			{
				playerAction = DASH;
				xSpeed = (dashSpeed / 2f) * Game.SCALE;
				airSpeed = (dashSpeed / 5) * Game.SCALE;
				
				//Direita
				flipAdd = -5;
				flipX = width;
				flipW = -1;
			}
			
			if ((verticalDirection == -1) && horizontalDirection == -1)
			{
				playerAction =DASH;
				xSpeed = (-dashSpeed / 2f) * Game.SCALE;
				airSpeed = (dashSpeed / 5) * Game.SCALE;
				
				//Esquerda
				flipAdd=0;
				flipX = 0;
				flipW = 1;
			}
			
			updateXPosition(xSpeed);
			updateYPosition(airSpeed);
		}
	}

	public void render(Graphics g)
	{
		g.drawImage(animations[playerAction][animationIndex], 
				(int) ((hitbox.x - xDrawOffset + flipX + flipAdd) - Playing.xLevelOffset), 
				(int) (hitbox.y - yDrawOffset +1) - Playing.yLevelOffset, 
				width * flipW, height, null);
		drawHitbox(g);
	}

	//Resetar todas as booleans quando a janela perder o foco
	public void resetDirectionBooleans() 
	{
		left = false;
		right = false;
		jump = false;
		up = false;
		dashPressed = false;
		down = false;
	}
	
//////////////////////FUNCOES DE GAMEPLAY//////////////////////////////////////////////////////////////
	
	private void updatePosition() 
	{
		if (dash < dashMinTime || dash > dashMaxTime)
		{
			moving = false;
			
			if (!left && !right && !inAir && !down && !jump && !up)
			{
				return;
			}
			
			verifyDirection(up,left,down,right);
			
			xSpeed = 0;

			if (right && !left && !down)
			{
				xSpeed = playerSpeed;
			}
			
			if (left && !right && !down)
			{
				xSpeed = -playerSpeed;
			}
			
			if (jump)
			{
				jump();
			}
			
			if ((CanMoveHere(hitbox.x + wallDistance, hitbox.y, hitbox.width, hitbox.height, levelData)))
			{
				if (left && !right && !down)
				{
					flipAdd=0;
					flipX = 0;
					flipW = 1;
				}
			}
			
			if ((CanMoveHere(hitbox.x - wallDistance, hitbox.y, hitbox.width, hitbox.height, levelData)))
			{
				if (right && !left && !down)
				{
					flipAdd = -5;
					flipX = width;
					flipW = -1;
				}
			}
		}

		if (inAir) 
		{
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) 
			{
				hitbox.y += airSpeed;
				airSpeed += gravity;
				if (dash < dashMinTime || dash > dashMaxTime)
					updateXPosition(xSpeed);
			} 
			else 
			{
				//metodo para verificar se o player colidiu com o chao ou o teto
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				
				//Ao atingir o chao
				if (airSpeed > 0)
				{
					resetInAir();
					jumpSpeed=jumpSpeedValue;
					
					if (dash > dashMaxTime)
						dash=0;
				}
				//Ao atingir o teto
				else
					airSpeed = fallSpeedAfterCollision;
				if (dash < dashMinTime || dash > dashMaxTime)
					updateXPosition(xSpeed);
			}
		}
		else
		{
			if (dash < dashMinTime || dash > dashMaxTime)
				updateXPosition(xSpeed);
			if (!IsEntityOnFloor(hitbox, levelData))
				setInAir(true);

		}

		if ((!right && left || !left && right) && !down && 
			((CanMoveHere(hitbox.x + wallDistance, hitbox.y, hitbox.width, hitbox.height, levelData)) &&
			(CanMoveHere(hitbox.x - wallDistance, hitbox.y, hitbox.width, hitbox.height, levelData))))
			moving = true;
	}
	
	
	
	private void verifyDirection(boolean up, boolean left, boolean down, boolean right) {
		//Direções unicas
		if (left && (!up && !down && !right))
		{
			horizontalDirection=-1;
			verticalDirection=0;
		}
		else if (right && (!up && !down && !left))
		{
			horizontalDirection=1;
			verticalDirection=0;
		}
		else if (up && (!left && !right && !down))
		{
			horizontalDirection=0;
			verticalDirection=1;
		}
		else if (down && (!left && !right && !up))
		{
			horizontalDirection=0;
			verticalDirection=-1;
		}
		

		//Diagonais
		if (left && up && (!down && !right))
		{
			horizontalDirection=-1;
			verticalDirection=1;
		}
		else if (left && down && (!up && !right))
		{
			horizontalDirection=-1;
			verticalDirection=-1;
		}

		else if (right && up && (!down && !left))
		{
			horizontalDirection=1;
			verticalDirection=1;
		}
		else if (right && down && (!up && !left))
		{
			horizontalDirection=1;
			verticalDirection=-1;
		}
}

	
	
	//Player nao comecar voando, evitando quem ta jogando a ter que apertar algum botao para dai comecar o jogo de verdade
	public void loadLevelData(int[][] levelData) 
	{
		this.levelData = levelData;
		if (!IsEntityOnFloor(hitbox, levelData))
			setInAir(true);	
	}
	
	private void jump() 
	{
		if (jumpTrigger==false)
		{
			if (inAir)
				//Ele envia as coordenadas da hitbox, mas com um valor a mais para a direita e para a esquerda, caso alguma
				//delas colidam com uma parede, se é possivel pular de novo
				if ((CanMoveHere(hitbox.x + wallDistance, hitbox.y, hitbox.width, hitbox.height, levelData)) &&
				(CanMoveHere(hitbox.x - wallDistance, hitbox.y, hitbox.width, hitbox.height, levelData)))
					return;
				
				else
				{
					//quanto mais pulos voce der na parede, a forca do pulo vai ficando mais fraca
					jumpSpeed *= wallJumpSpeed;
				}

			setInAir(true);
			airSpeed = jumpSpeed;
			setJumpTrigger(true);
		}
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
	
	
	

	//Getters and Setters do pulo, dash, gravidade
	public void resetInAir() 
	{
		setInAir(false);
		airSpeed = 0;
	}
	
	public void setInAir(boolean inAir)
	{
		this.inAir = inAir;
	}
	
	public boolean getDashPressed()
	{
		return dashPressed;
	}
	
	public void setDashPressed(boolean dashPressed)
	{
		this.dashPressed = dashPressed;
	}
	
	public int getDash()
	{
		return dash;
	}
	
	public void setDash(int dash)
	{
		this.dash = dash; 
	}
	
	public float getJumpSpeed()
	{
		return jumpSpeed;
	}
	
	public void setJumpTrigger(boolean jumpTrigger) {
		this.jumpTrigger = jumpTrigger;
	}
	
	public void setGravity(float gravity)
	{
		this.gravity=gravity * Game.SCALE;
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

	public void setJump(boolean jump) 
	{
		this.jump = jump;
	}
}