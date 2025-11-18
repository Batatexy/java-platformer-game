package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import entities.MiniPlayer;
import entities.Player;
import entities.Trash;
import entities.TriggerEvent;
import entities.Vehicle;
import levels.LevelManager;
import levels.Props;
import main.Game;
import ui.TextBox;
import utilz.LoadSave;

public class CutScene extends State implements Statemethods
{
	private LevelManager levelManager;
	private MiniPlayer miniplayer;
	private Player player;
	private Trash objTrashOne, objTrashTwo, objTrashTree, objTrashFour, objTrashFive;
	private Vehicle garbagetruck, othervehiclesOne, othervehiclesTwo;
	
	private boolean space = false;
	private int skipPercentageValue=200;
	private int skipPercentage=skipPercentageValue;
	private int skipValue=1;
	
	private int events=0;
	private int black = 255;
	
	private int miniplayerInitialX = -10 * (int)(Game.SCALE);
	private int miniplayerInitialY = 438 * (int)(Game.SCALE);
	
	private int defaultXLevelOffset=0;
	private int defaultYLevelOffset=(int)(160 * Game.SCALE);
	
	public static int xLevelOffset = 0, yLevelOffset=(int)(160 * Game.SCALE);
	private static int subTick=0, tick=0;
	private static int tickVertical=0;
	
	private BufferedImage buildings = LoadSave.GetSpriteAtlas("/Buildings.png");
	
	public CutScene(Game game) 
	{
		super(game);
		initClasses();
		TextBox.loadBoxImages("/TextBoxNull.png");
	}
	
	private void initClasses() 
	{
		levelManager = new LevelManager(game, LoadSave.CUTSCENE_DATA);
		
		objTrashOne = new Trash("/TrashFive.png", 100, 428, player, miniplayer);
		objTrashTwo = new Trash("/TrashFive.png", 150, 427, player, miniplayer);
		objTrashTree = new Trash("/TrashFive.png", 260, 430, player, miniplayer);
		objTrashFour = new Trash("/TrashFive.png", 410, 431, player, miniplayer);
		objTrashFive = new Trash("/TrashFive.png", 540, 425, player, miniplayer);
		
		miniplayer = new MiniPlayer(0, 0 , (int)(5 * Game.SCALE), (int)(10 * Game.SCALE));
		miniplayer.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		
		player = new Player(-100, -100 , (int)(5 * Game.SCALE), (int)(10 * Game.SCALE));
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());

		garbagetruck = new Vehicle(true,-75, 444);
		othervehiclesOne = new Vehicle(false,500, 424);
		othervehiclesTwo = new Vehicle(false,850, 424);
	}
	
	public MiniPlayer getminiplayer()
	{
		return miniplayer;
	}
	
	@Override
	public void update() 
	{
		levelManager.update();
		miniplayer.update();
		skipCutScene();
		tickVertical++;
		
		objTrashOne.update();
		objTrashTwo.update();
		objTrashTree.update();
		objTrashFour.update();
		objTrashFive.update();
		
		switch (tickVertical)
		{
			case (1700):
			{
				miniplayer.setUp(true);
				break;
			}
			
			case (1720):
			{
				miniplayer.setUp(false);
				break;
			}
			
			case (2340):
			{
				miniplayer.setDown(true);
				break;
			}
			
			case (2360):
			{
				miniplayer.setDown(false);
				break;
			}
			
			case (3050):
			{
				miniplayer.setUp(true);
				break;
			}
			
			case (3070):
			{
				miniplayer.setUp(false);
				break;
			}
		}
		
		switch (events)
		{
			case 0:
			{
				miniplayer.getHitbox().x=miniplayerInitialX;
				miniplayer.getHitbox().y=miniplayerInitialY;

				Player.lastX=(int) (Playing.playerInitialX * Game.SCALE);
				Player.lastY=(int) (Playing.playerInitialY * Game.SCALE);
				player.lastX=(int) (Playing.playerInitialX * Game.SCALE);
				player.lastY=(int) (Playing.playerInitialY * Game.SCALE);
				
				Playing.newGame=true;
				Game.deathCount=0;

				Playing.black=255;
				TextBox.place=false;
				
				TriggerEvent.resetAllTick(true);
				Trash.restartAllTrashes(true);
				Trash.trashActive=true;
				
				xLevelOffset=defaultXLevelOffset;
				yLevelOffset=defaultYLevelOffset;

				player.getHitbox().x = (int)(Playing.playerInitialX);
				player.getHitbox().y = (int)(Playing.playerInitialY);
				
				TextBox.loadBoxImages("/TextBoxNull.png");
				updateTick();
				
				if (tick >= 10)
				{
					updateTick();
					events++;
				}
				
				break;
			}
			
			case 1:
			{
				TriggerEvent.resetAllTick(false);
				Trash.restartAllTrashes(false);
				TextBox.loadBoxImages("/TextBoxOne.png");
				resetThings();
				events++;
				break;
			}
			
			case 2:
			{
				TextBox.update();
				updateTick();

				if (tick >= 50)
				{
					resetThings();
					events++;
				}
				
				break;
			}
			
			case 3:
			{
				TextBox.loadBoxImages("/TextBoxNull.png");
				updateTick();

				if (tick >= 15)
				{
					resetThings();
					events++;
				}
				
				break;
			}

			case 4:
			{
				yLevelOffset+=1;
				
				if (black>0)
					black--;
				
				else
					black=0;
				
				if (yLevelOffset >= (int)(315 * Game.SCALE))
				{
					miniplayer.setRight(true);
					garbagetruck.setXSpeed(0.5f);
				}
				
				if (yLevelOffset >= (int)(325 * Game.SCALE))
				{
					TextBox.loadBoxImages("/TextBoxTwo.png");
					resetThings();
					events++;
				}
				
				break;
			}
			
			case 5:
			{
				othervehiclesOne.setXSpeed(-0.6f);
				othervehiclesTwo.setXSpeed(-0.55f);
				miniplayer.setRight(true);
				yLevelOffset=(int)(325 * Game.SCALE);
				xLevelOffset+=1;
				
				updateTick();
				TextBox.update();

				if (tick>50)
				{
					TextBox.loadBoxImages("/TextBoxNull.png");
					resetThings();
					events++;
				}

				break;
			}
			case 6:
			{
				miniplayer.setRight(true);
				yLevelOffset=(int)(325 * Game.SCALE);
				xLevelOffset+=1;
				updateTick();
				
				if (tick>15)
				{
					TextBox.loadBoxImages("/TextBoxTree.png");
					resetThings();
					events++;
				}
				
				break;
			}
			case 7:
			{
				miniplayer.setRight(true);
				yLevelOffset=(int)(325 * Game.SCALE);
				xLevelOffset+=1;
				updateTick();
				TextBox.update();
				
				if (tick>60)
				{
					TextBox.loadBoxImages("/TextBoxNull.png");
					resetThings();
					events++;
				}
				
				break;
			}
			case 8:
			{
				miniplayer.setRight(true);
				yLevelOffset=(int)(325 * Game.SCALE);
				xLevelOffset+=1;
				updateTick();
				
				if (tick>30)
				{
					TextBox.loadBoxImages("/TextBoxFour.png");
					resetThings();
					events++;
				}
				
				break;
			}
			case 9:
			{
				miniplayer.setRight(false);
				garbagetruck.setXSpeed(0);
				yLevelOffset=(int)(325 * Game.SCALE);
				updateTick();
				TextBox.update();
				
				if (tick>60)
				{
					TextBox.loadBoxImages("/TextBoxNull.png");
					resetThings();
					events++;
				}
				
				break;
			}
			case 10:
			{
				updateTick();
				if (tick<9)
				{
					miniplayer.setRight(true);
					miniplayer.setUp(true);
				}
				else if (tick>8 && tick<20)
				{
					miniplayer.setRight(false);
					miniplayer.setUp(false);
					miniplayer.setPlayerSpeed(0.32f);
				}
				else if (tick>=20 && tick<25)
				{
					miniplayer.setUp(true);
				}
				else if (tick>25 && tick<30)
				{
					miniplayer.setUp(false);
				}
				else if (tick>30 && tick<35)
				{
					miniplayer.setUp(true);
				}
				else if (tick>35 && tick<40)
				{
					miniplayer.setUp(false);
				}

				else if (tick>40 && tick<45)
				{
					miniplayer.setUp(true);
				}
				else if (tick>45 && tick<50)
				{
					miniplayer.setUp(false);
				}

				else if (tick>50 && tick<55)
				{
					miniplayer.setUp(true);
				}
				else if (tick>55 && tick<60)
				{
					miniplayer.setUp(false);
				}

				else if (tick>60 && tick<65)
				{
					miniplayer.setUp(true);
				}
				else if (tick>64 && tick<70)
				{
					miniplayer.setUp(false);
				}
				
				else if (tick>70 && tick<75)
				{
					miniplayer.setUp(true);
				}
				else if (tick>74 && tick<80)
				{
					miniplayer.setUp(false);
				}
				else if (tick>90)
				{
					if (black<255)
					{
						
						black+=1;
					}
					else
					{
						events++;
					}
				}
				break;
			}
			
			case 11:
			{		
				TriggerEvent.resetAllTick(false);
				Trash.restartAllTrashes(false);
				skipPercentage=skipPercentageValue;
				setSpace(false);
				resetThings();
				Playing.newGame=false;
				tickVertical=0;
				Game.collectedTrashes=0;
				Game.deathCount=0;
				Game.attemptCount=0;
				black=255;
				Playing.black=255;
				Player.restartGame();
				events=0;
				Gamestate.state = Gamestate.PLAYING;
			}
		}
	} 
	
	private void resetThings()
	{
		tick=0;
		subTick=0;
		TextBox.resetTick();
	}
	
	private void updateTick()
	{
		subTick++;
		if (subTick>10)
		{
			tick++;
			subTick=0;
		}
		
		othervehiclesOne.update();
		othervehiclesTwo.update();
		garbagetruck.update();
	}
	
	@Override
	public void draw(Graphics g) 
	{
		levelManager.draw(g, xLevelOffset, yLevelOffset);

		drawBehindProps(g);
		drawAboveProps(g);
		
		miniplayer.render(g);

		othervehiclesOne.draw(g, xLevelOffset, yLevelOffset);
		othervehiclesTwo.draw(g, xLevelOffset, yLevelOffset);
		garbagetruck.draw(g, xLevelOffset, yLevelOffset);
		
		if (events < 5 || events>=10)
		{
			g.setColor(new Color(0,0,0,black));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		}
		
		TextBox.draw(g);
	}
	
	private void drawAboveProps(Graphics g) 
	{
		objTrashFive.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
	}
	
	private void drawBehindProps(Graphics g)
	{
		Props.draw(g, buildings, 0, 434 , xLevelOffset, yLevelOffset);
		objTrashOne.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashTwo.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashTree.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashFour.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
	}
	
	private void skipCutScene()
	{	
		if (space)
			skipPercentage-=skipValue;
		else
			skipPercentage+=skipValue;
		
		if (skipPercentage>skipPercentageValue)
			skipPercentage=skipPercentageValue;
		
		if (skipPercentage<=0)
		{
			events=11;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		switch (e.getKeyCode()) 
		{
			//Tecla de espaço
			case KeyEvent.VK_SPACE:
			{
				setSpace(true);
				break;
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) 
	{
		switch (e.getKeyCode()) 
		{
			//Tecla para Esquerda
			case KeyEvent.VK_SPACE:
			{
				setSpace(false);
				break;
			}
		}
	}
	
	public void setSpace(boolean space)
	{
		this.space = space;
	}
}