package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import gamestates.Gamestate;
import gamestates.Playing;
import levels.AnimatedProp;
import main.Game;
import ui.TextBox;

public class TriggerEvent extends Entity
{
	private Player player;
	private AnimatedProp dashKeys = new AnimatedProp("/DashKeys.png", 2864-32, 2304, 32, 32, 2, 100);;
	private int collisionTrigger=0;

	private int[][] levelData;
	
	//Configurações do tamanho da Hitbox
	private float xDrawOffset = 1f * Game.SCALE;
	private float yDrawOffset = 4f * Game.SCALE;
	private int action;
	private int subTickValue = 10, subTick=subTickValue, tick = 0;
	public static boolean resetAllTick=false;
	private boolean drawDashKeys=false;
	private boolean right=false, dash=false, up=false, down=false, left=false;
	
	public TriggerEvent(float x, float y, int width, int height, int action, Player player) 
	{
		super((int)(x * (Game.SCALE)), (int)(y * (Game.SCALE)), (int)(width * (Game.SCALE)), (int)(height * (Game.SCALE)));
		//Tamanho da hitbox do espinho
		initHitbox((x)  * Game.SCALE, (y)  * Game.SCALE,(int) ((width*16) * Game.SCALE), (int) ((height*16) * Game.SCALE));
		this.player = player;
		this.action = action;
	}
	
	public void update()
	{
		checkTriggerEventTouched(player.getHitbox());
		
		if (resetAllTick)
		{
			collisionTrigger=0;
			subTick=subTickValue;
			tick = 0;
		}
		
		if (collisionTrigger==1)
		{
			if(!Playing.paused)
				subTick--;
			
			if (subTick<=0)
			{
				tick++;
				subTick=subTickValue;
			}
			
			switch (action)
			{
				case 1:
				{
					Trash.restartAllTrashes(true);
					player.setCantMove(true);
					player.setRight(true);

					if (tick>20)
					{
						Trash.restartAllTrashes(false);
						player.setCantMove(false);
						collisionTrigger=2;
					}
					break;
				}
				
				case 2://Esse ai sou eu...
				{
					TextBox.place = true;
					
					if (right && dash && tick>20 && !up && !down && !left)
					{
						player.setGravity(0);
						player.setAirSpeed(0);
						Playing.dashEnable=true;
						player.dash=1;
						Game.slowMotion=Game.UPS_SET;
						collisionTrigger=2;
					}
					
					if (tick==0)
						Game.slowMotion=0;
					
					else if (tick==1)
					{
						TextBox.loadBoxImages("/PlayingOne.png");
					}
					else if (tick > 110 && tick < 112)
					{
						TextBox.loadBoxImages("/PlayingTwo.png");
						TextBox.resetTick();
					}
					else if (tick > 190 && tick < 192)
					{
						TextBox.loadBoxImages("/PlayingTree.png");
						TextBox.resetTick();
					}
					else if (tick > 250 && tick < 252)
					{
						TextBox.loadBoxImages("/PlayingFour.png");
						TextBox.resetTick();
					}
					
					if (tick >300 && collisionTrigger!=2)
					{
						dashKeys.update();
						drawDashKeys=true;
					}
					else
						drawDashKeys=false;
					break;
				}
				
				case 3: //Cena do comeco onde ele escala a montanha
				{
					Game.timeMil=0;
					Game.timeSec=0;
					Game.timeMin=0;
					player.setCantMove(true);
					player.setRight(true);
					
					if (tick>4)
					{
						if (Playing.black>0)
						{
							Playing.increaseBlackSreen(-1);
						}
						else
						{
							Playing.black=0;
						}
					}
					
					if (tick==9 || tick==13 || tick==17 || tick==21 || tick==25)
					{
						player.setJump(false);
						player.setJumpTrigger(false);
					}
					
					if (tick>=5 && tick<9)
					{
						player.setJump(true);
					}

					if (tick>9 && tick<13)
					{
						player.setJump(true);
					}
					
					if (tick>13 && tick<17)
					{
						player.setJump(true);
					}
					
					if (tick>17 && tick<21)
					{
						player.setJump(true);
					}
					
					if (tick>21 && tick<25)
					{
						player.setJump(true);
					}

					if (tick>35)
					{
						Player.lastX=(int) (512 * Game.SCALE);
						Player.lastY=(int) (2720 * Game.SCALE);
						player.setRight(false);
						player.setCantMove(false);
						collisionTrigger=2;
					}
					break;
				}
				
				case 4: //Cena final onde ele corre para o abismo e faz um salto de fe
				{
					Gamestate.state = Gamestate.END;
					player.setCantMove(true);
					player.setRight(true);
					
					if (player.getHitbox().x >= 6434 * Game.SCALE)
						player.jump();
					
					if (tick>20)
						Playing.garbageTruckSpeed=1;
					
					Player.setFinalTimer();
					
					break;
				}
				
				case 5:
				{
					Player.active=false;
					
					if (player.getHitbox().x >= 6750 * Game.SCALE)
					{
						Playing.increaseBlackSreen(1);
					}
					
					if (Playing.black>=255)
						Gamestate.state = Gamestate.END;
					
					break;
				}
			}
		}
	}
	
	public void draw(Graphics g, int xLevelOffset, int yLevelOffset, Player player)
	{
		this.player = player;
		drawHitbox(g, Color.MAGENTA, xLevelOffset, yLevelOffset);
		
		if (drawDashKeys)
			dashKeys.draw(g, xLevelOffset, yLevelOffset, player, null);
	}
	
	public void checkTriggerEventTouched(Rectangle2D.Float hitbox)
	{
		if (this.hitbox.intersects(hitbox) && collisionTrigger==0)
		{
			collisionTrigger=1;
		}
	}
	
	public int getCollisionTrigger()
	{
		return collisionTrigger;
	}
	
	public static void resetAllTick(boolean resetAllTickValue)
	{
		resetAllTick=resetAllTickValue;
	}
	
	public int getTick()
	{
		return tick;
	}
	
	public void setDash(boolean dash)
	{
		this.dash = dash;
	}
	
	public void setRight(boolean right)
	{
		this.right = right;
	}
	
	public void setLeft(boolean left)
	{
		this.left = left;
	}
	
	public void setUp(boolean up)
	{
		this.up = up;
	}
	
	public void setDown(boolean down)
	{
		this.down = down;
	}
}