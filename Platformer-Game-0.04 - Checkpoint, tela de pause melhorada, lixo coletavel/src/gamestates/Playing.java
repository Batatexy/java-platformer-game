package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.CheckPoint;
import entities.MiniPlayer;
import entities.Player;
import entities.Trash;
import levels.LevelManager;
import levels.Props;
import main.Game;
import ui.PauseOverlay;
import ui.TextBox;
import utilz.LoadSave;

public class Playing extends State implements Statemethods
{
	public static int trashAnimationTick;

	public static int trashAnimationIndex;

	public static int trashAnimationSpeed = 16;
	
	public static boolean newGame=true;
	
	private LevelManager levelManager;
	private Player player;
	private MiniPlayer miniplayer;
	private CheckPoint checkPointOne, checkPointTwo, checkPointTree, checkPointFour, checkPointFive, checkPointSix;
	private Trash objTrashOne, objTrashTwo, objTrashTree;
	
	private PauseOverlay pauseOverlay;
	private boolean paused = false;
	
	//New Game Coordenadas: 260 * Game.SCALE, 2786 * Game.SCALE
	//Testar o começo do jogo: 300, 2700
	private int distance = (int) (212 * Game.SCALE);
	
	public static int xLevelOffset, yLevelOffset;
	private int leftBorder = (int)(0.46 * Game.GAME_WIDTH);
	private int rightBorder = (int)(0.54 * Game.GAME_WIDTH);
	private int levelTilesWide = LoadSave.GetLevelData(LoadSave.LEVEL_ONE_DATA)[0].length;
	private int maxTilesOffsetX = levelTilesWide - Game.TILES_IN_WIDTH;
	private int maxLevelOffsetX = maxTilesOffsetX * Game.TILES_SIZE;
	
	private int topBorder = (int)(0.4 * Game.GAME_HEIGHT);
	private int bottomBorder = (int)(0.6 * Game.GAME_HEIGHT);
	private int levelTilesHigh = LoadSave.GetLevelData(LoadSave.LEVEL_ONE_DATA)[1].length;
	private int maxTilesOffsetY = levelTilesHigh - Game.TILES_IN_WIDTH;
	private int maxLevelOffsetY = maxTilesOffsetY * Game.TILES_SIZE;
	
	private static int subTick=0, tick=0;
	
	private boolean textBoxTrigger = false;

	private BufferedImage 
	backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND),
	backgroundImageTwo = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDTWO),
	backgroundImageTree = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDTREE),
	backgroundImageFour = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDFOUR),
	backgroundImageFive = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDFIVE),

	houseGrass = LoadSave.GetSpriteAtlas("/HouseGrass.png"),
	houseCommon = LoadSave.GetSpriteAtlas("/HouseCommon.png"),
	houseBroken = LoadSave.GetSpriteAtlas("/HouseBroken.png"),
	tent = LoadSave.GetSpriteAtlas("/Tent.png"),
	
	
	trashBig = LoadSave.GetSpriteAtlas("/TrashBig.png"),
	trashSmall = LoadSave.GetSpriteAtlas("/TrashBig.png");
	
	
	
	//private int playerInitialX=300;
	//private int playerInitialY=2700;
	public static int playerInitialX=1600;
	public static int playerInitialY=2580;
	
	public Playing(Game game) 
	{
		super(game);
		initClasses();
	}
	
	
	
	
	
	
	//Desenhar casas, ou qualquer outra parte do cenário
	private void drawProps(Graphics g) 
	{
		Props.draw(g, houseGrass, 1360, 2624, xLevelOffset, yLevelOffset);
		Props.draw(g, tent, 2096, 2496, xLevelOffset, yLevelOffset);
		
		
	}

	//Criar os Lixos
	private void createTrash()
	{
		objTrashOne = new Trash("/TrashFive.png", 1472, 2624, player, miniplayer);
		objTrashTwo = new Trash("/TrashFour.png", 2070, 2496, player, miniplayer);
		objTrashTree = new Trash("/TrashFour.png", 0, 0, player, miniplayer);
	}
	
	//Desenhar os Lixos
	private void drawTrash(Graphics g) 
	{
		objTrashOne.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashTwo.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
	}
	
	//Criar Checkpoints
	private void createCheckPoint()
	{
		checkPointOne = new CheckPoint(1616, 2592, player, miniplayer);
		checkPointTwo = new CheckPoint(1792, 2640, player, miniplayer);
	}
	
	//Desenhar os Checkpoints
	private void drawCheckpoint(Graphics g) 
	{
		checkPointOne.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		checkPointTwo.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
	}

	
	
	
	private void allTrashAndCheckpointUpdate() 
	{
		trashUpdateAnimationTick();
		
		objTrashOne.update();
		objTrashTwo.update();
//		objTrashTree.update();
//		objTrashFour.update();
//		objTrashFive.update();
//		objTrashSix.update();
//		objTrashSeven.update();
//		objTrashEight.update();
		
		checkPointOne.update();
		checkPointTwo.update();
//		checkPointTree.update();
//		checkPointFour.update();
//		checkPointFive.update();
//		checkPointSix.update();
	}
	
	
	
	public void unpauseGame() {
		paused = false;
	}

	private void initClasses() 
	{

		levelManager = new LevelManager(game, LoadSave.LEVEL_ONE_DATA);
		
		createTrash();
		createCheckPoint();

		player = new Player(playerInitialX * Game.SCALE , playerInitialY * Game.SCALE, (int)(18 * Game.SCALE), (int)(18 * Game.SCALE));
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		
		pauseOverlay = new PauseOverlay(this);
	}

	public void windowFocusLost() 
	{
		player.resetDirectionBooleans();
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public void update() {
		//Fazer algum sistema de camera lenta
		//Game.UPS-=1;
		
		//Se não estiver pausado, o jogo está rodando
		if(!paused)
		{
			levelManager.update();
			player.update();
			checkCloseToBorderX();
			checkCloseToBorderY();
			
			if (textBoxTrigger)
				TextBox.update();
			else
				TextBox.resetTick();
		}
		//Se estiver, então nada acontece, o jogo está pausado
		else
		{
			pauseOverlay.update();
		}
		
		allTrashAndCheckpointUpdate();

	} 

	private void trashUpdateAnimationTick() 
	{
		Playing.trashAnimationTick++;
		if (Playing.trashAnimationTick >= Playing.trashAnimationSpeed) 
		{
			Playing.trashAnimationTick = 0;
			Playing.trashAnimationIndex++;

				if (Playing.trashAnimationIndex >= 4)
				{
					Playing.trashAnimationIndex = 0;
				}
		}
	}
	
	@Override
	public void draw(Graphics g) {
		
		//remover eventualmente
		//System.out.println(tick);
		
		backGroundParallax(g);
		
		levelManager.draw(g, xLevelOffset, yLevelOffset);

		drawProps(g);
		drawTrash(g);
		drawCheckpoint(g);
		Props.draw(g, trashBig, 1472 - 1, 2624, xLevelOffset, yLevelOffset);

		player.render(g);
		
		if(paused)
		{
			pauseOverlay.pauseTrigger=true;
		}

		pauseOverlay.draw(g);
	}

	
	private void backGroundParallax(Graphics g) 
	{
		g.drawImage(backgroundImage, 0,0,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageTwo,0 , (int)(distance - yLevelOffset*0.1) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageTree,0, (int)(distance - yLevelOffset*0.12) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageFour,0, (int)(distance - yLevelOffset*0.14) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageFive,0, (int)(distance - yLevelOffset*0.16) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
	}

	private void checkCloseToBorderX() {
		int playerX = (int)(player.getHitbox().x);
		int diff = playerX - xLevelOffset;
		
		if(diff > rightBorder) 
		{
			xLevelOffset += diff - rightBorder;
		}
		else if(diff < leftBorder)
		{
			xLevelOffset += diff - leftBorder;
		}
		
		if(xLevelOffset > maxLevelOffsetX)
		{
			xLevelOffset = maxLevelOffsetX;
		}
		else if(xLevelOffset < 0)
		{
			xLevelOffset = 0;
		}
		
		//Esse cara quem determina o quanto que mexe, da pra fazer coisas
//		xLevelOffset+=3;
	}
	
	
	private void checkCloseToBorderY() {
		int playerY = (int)(player.getHitbox().y);
		int diff = playerY - yLevelOffset;
		
		if(diff > bottomBorder) 
		{
			yLevelOffset += diff - bottomBorder;
		}
		else if(diff < topBorder)
		{
			yLevelOffset += diff - topBorder;
		}
		
		if(yLevelOffset > maxLevelOffsetY)
		{
			yLevelOffset = maxLevelOffsetY;
		}
		else if(yLevelOffset < 0)
		{
			yLevelOffset = 0;
		}
	}
	




	@Override
	public void mousePressed(MouseEvent e) 
	{
		if (paused)
			pauseOverlay.mousePressed(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if (paused)
			pauseOverlay.mouseReleased(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		if (paused)
			pauseOverlay.mouseMoved(e);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) 
		{
			//Trocar o estado de pausado ou não
			case KeyEvent.VK_ESCAPE:
			{
				paused = !paused;
				pauseOverlay.pauseTrigger = !pauseOverlay.pauseTrigger;
				break;
			}
		
			//Tecla para Esquerda
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
			{
				player.setLeft(true);
				break;
			}
			
			//Tecla para Baixo
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
			{
				player.setDown(true);
				break;
			}
			
			//Tecla para Direita
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
			{
				player.setRight(true);
				break;
			}
			
			//Tecla para Cima
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
			{
				player.setUp(true);
				break;
			}
			
			//Tecla para Pular
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_Z:
			{
				player.setJump(true);
				break;
			}
				
			//Tecla para Dash
			case KeyEvent.VK_F:
			case KeyEvent.VK_E:
			case KeyEvent.VK_X:
			{
				if ((player.getDash() == 0) && (player.getDashPressed() == false) && (player.dashEnable==true))
				{
					player.setDash(1);
					player.setDashPressed(true);
				}
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) 
		{
			//Tecla para Esquerda
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
			{
				player.setLeft(false);
				break;
			}
				
			//Tecla para Baixo
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
			{
				player.setDown(false);
				break;
			}
				
			//Tecla para Direita
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
			{
				player.setRight(false);
				break;
			}
			
			//Tecla para Cima
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
			{
				player.setUp(false);
				break;
			}
				
			//Tecla para Pular
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_Z:
			{
				if (player.getJumpSpeed() < -0.25)
				{
					player.setJump(false);
					player.setJumpTrigger(false);
				}
				break;
			}
			
			//Tecla para Dash
			case KeyEvent.VK_F:
			case KeyEvent.VK_E:
			case KeyEvent.VK_X:
			{
				player.setDashPressed(false);
				break;
			}
		}
	}
}
