package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import entities.MiniPlayer;
import entities.Player;
import entities.Trash;
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
	private Trash objTrashOne, objTrashTwo, objTrashTree;
	
	private boolean space = false;
	private int skipPercentageValue=400;
	private int skipPercentage=skipPercentageValue;
	private int skipValue=1;
	
	private int events=0;
	private int black = 255;

	private int distance = 0;
	private int miniplayerInitialX = -10;
	private int miniplayerInitialY = 438;
	
	//yLevelOffset=620;
	public static int xLevelOffset, yLevelOffset=620;
	
	private static int subTick=0, tick=0;
	
	private BufferedImage backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND);
	private BufferedImage backgroundImageTwo = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDTWO);
	private BufferedImage backgroundImageTree = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDTREE);
	private BufferedImage backgroundImageFour = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDFOUR);
	private BufferedImage backgroundImageFive = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDFIVE);

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
		
		objTrashOne = new Trash("/TrashTree.png", 0,0, player, miniplayer);
		
		miniplayer = new MiniPlayer(miniplayerInitialX * Game.SCALE , miniplayerInitialY * Game.SCALE, (int)(5 * Game.SCALE), (int)(10 * Game.SCALE));
		miniplayer.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		
		
	}
	
	public MiniPlayer getminiplayer()
	{
		return miniplayer;
	}
	
	@Override
	public void update() {
		levelManager.update();
		miniplayer.update();
		skipCutScene();

		switch (events)
		{
			case 0:
			{
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
				{
					
					black-=1;
				}
				else
				{
					black=0;
				}
				
				if (yLevelOffset >= 1100)
				{
					miniplayer.setRight(true);
				}
				
				if (yLevelOffset >= 1300)
				{
					TextBox.loadBoxImages("/TextBoxTwo.png");
					resetThings();
					events++;
				}
				
				break;
			}
			
			case 5:
			{
				miniplayer.setRight(true);
				yLevelOffset=1300;
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
				yLevelOffset=1300;
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
				yLevelOffset=1300;
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
				yLevelOffset=1300;
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
				yLevelOffset=1300;
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
				break;
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
	}
	
	@Override
	public void draw(Graphics g) 
	{
		g.drawImage(backgroundImage, 0,0,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageTwo,0 , (int)(distance - yLevelOffset*0.1) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageTree,0, (int)(distance - yLevelOffset*0.12) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageFour,0, (int)(distance - yLevelOffset*0.14) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageFive,0, (int)(distance - yLevelOffset*0.16) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		
		levelManager.draw(g, xLevelOffset, yLevelOffset);

		drawBehindProps(g);
		
		miniplayer.render(g);
		
		drawAboveProps(g);
		
		if (events < 5)
		{
			g.setColor(new Color(0,0,0,black));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		}
		
		TextBox.draw(g);
	}
	
	private void drawBehindProps(Graphics g) {
		Props.draw(g, buildings, 0, 340, xLevelOffset, yLevelOffset);

	}
	
	private void drawAboveProps(Graphics g) {

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
			skipPercentage=skipPercentageValue;
			Playing.newGame=true;
			Gamestate.state = Gamestate.PLAYING;
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
	public void keyPressed(KeyEvent e) {
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
	public void keyReleased(KeyEvent e) {
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
