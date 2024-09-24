package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import entities.CheckPoint;
import entities.MiniPlayer;
import entities.OrbDash;
import entities.Player;
import entities.Spike;
import entities.Trampoline;
import entities.Trash;
import entities.TriggerEvent;
import levels.LevelManager;
import levels.Props;
import main.Game;
import ui.PauseOverlay;
import ui.TextBox;
import utilz.LoadSave;

public class Playing extends State implements Statemethods
{
	public static int trashAnimationTick, trashAnimationIndex, trashAnimationSpeed = 16;
	public static boolean newGame=true;
	
	private LevelManager levelManager;
	private Player player;
	private MiniPlayer miniplayer;
	
	private PauseOverlay pauseOverlay;
	public static boolean paused = false, active = true;
	
	private int distance = (int) (212 * Game.SCALE);
	
	private CheckPoint checkPointOne, checkPointTwo, checkPointTree, checkPointFour, checkPointFive, 
	checkPointSix,checkPointSeven, checkPointEight, checkPointNine, checkPointTen;
	
	private Spike spikeOne, spikeTwo, spikeTree, spikeFour, spikeFive, spikeSix, spikeSeven, spikeEight, 
	spikeNine, spikeTen, spikeEleven, spikeTwelve, spikeThirteen, spikeFourteen, spikeFifteen, spikeSixteen;
	
	private Trash objTrashOne, objTrashTwo, objTrashTree, objTrashFour, objTrashFive, objTrashSix, objTrashSeven,
	objTrashEight, objTrashNine, objTrashTen, objTrashEleven, objTrashTwelve, objTrashThirTeen, objTrashFourteen;
	
	private Trampoline trampolineOne, trampolineTwo, trampolineTree, trampolineFour, trampolineFive, trampolineSix,
	trampolineSeven, trampolineEight, trampolineNine, trampolineTen;
	
	private OrbDash orbdashOne, orbdashTwo, orbdashTree, orbdashFour, orbdashFive, orbdashSix, orbdashSeven,
	orbdashEight, orbdashNine, orbdashTen, orbdashEleven, orbdashTwelve, orbdashThirteen, orbdashFourteen;
	
	private TriggerEvent triggereventOne, triggereventTwo, triggereventTree, triggereventFour, triggereventFive,
	triggereventSix;
	
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
	private static int slowMotionTickValue=Game.UPS_SET/Game.slowMotion, slowMotionTick=slowMotionTickValue;
	
	private boolean textBoxTrigger = false;
	public static int black=0;
	
	//Céu azul padrão:
	private int brightSkyRed = 70;
	private int brightSkyGreen = 200;
	private int brightSkyBlue = 240;
	
	//Céu com a cor padrão:
	private int red = brightSkyRed, blue = brightSkyBlue, green = brightSkyGreen;

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

	//private int playerInitialX=264;
	//private int playerInitialY=2800;
	public static int playerInitialX=2272 + 160;
	public static int playerInitialY=2000;
	
	//Criar os Lixos
	private void createTrash()
	{
		objTrashOne = new Trash("/TrashFive.png", 1472 + 160, 2624, player, miniplayer);
		objTrashTwo = new Trash("/TrashFour.png", 2070 + 160, 2496, player, miniplayer);
		objTrashTree = new Trash("/TrashOne.png", 3664 + 160, 2208, player, miniplayer);
		
		objTrashFour = new Trash("/TrashOne.png", 0, 0, player, miniplayer);
		objTrashFive = new Trash("/TrashOne.png", 0, 0, player, miniplayer);
		objTrashSix = new Trash("/TrashOne.png", 0, 0, player, miniplayer);
		objTrashSeven = new Trash("/TrashOne.png", 0, 0, player, miniplayer);
		objTrashEight = new Trash("/TrashOne.png", 0, 0, player, miniplayer);
		objTrashNine = new Trash("/TrashOne.png", 0, 0, player, miniplayer);
		objTrashTen = new Trash("/TrashOne.png", 0, 0, player, miniplayer);
		objTrashEleven = new Trash("/TrashOne.png", 0, 0, player, miniplayer);
		objTrashTwelve = new Trash("/TrashOne.png", 0, 0, player, miniplayer);
		objTrashThirTeen = new Trash("/TrashOne.png", 0, 0, player, miniplayer);
		objTrashFourteen = new Trash("/TrashOne.png", 0, 0, player, miniplayer);
	}
	
	//Criar Checkpoints
	private void createCheckPoint()
	{
		checkPointOne = new CheckPoint(1616+160, 2592, player);
		checkPointTwo = new CheckPoint(2272 + 160, 2208, player);
		checkPointTree = new CheckPoint(2928 + 160, 2320, player);
		checkPointFour = new CheckPoint(0, 0, player);
		checkPointFive = new CheckPoint(0, 0, player);
		checkPointSix = new CheckPoint(0, 0, player);
		checkPointSeven = new CheckPoint(0, 0, player);
		checkPointEight = new CheckPoint(0, 0, player);
		checkPointNine = new CheckPoint(0, 0, player);
		checkPointTen = new CheckPoint(0, 0, player);
	}
	
	//Criar os Lixos
	private void createSpike()
	{
		spikeOne = new Spike(1680+160, 2624, 3, 1, player);
		spikeTwo = new Spike(2352+160, 2256, 3, 1, player);
		spikeTree = new Spike(2496+160, 2224, 1, 1, player);
		spikeFour = new Spike(2608+160, 2352, 9, 1, player);
		spikeFive = new Spike(2944+160, 2352, 10, 1, player);
		spikeSix = new Spike(3104+160, 2320, 3, 1, player);
		spikeSeven = new Spike(0, 5440-64, 40, 20, player);
		spikeEight = new Spike(0, 0, 1, 1, player);
		spikeNine = new Spike(0, 0, 1, 1, player);
		spikeTen = new Spike(0, 0, 1, 1, player);
		spikeEleven = new Spike(0, 0, 1, 1, player);
		spikeTwelve = new Spike(0, 0, 1, 1, player);
		spikeThirteen = new Spike(0, 0, 1, 1, player);
		spikeFourteen = new Spike(0, 0, 1, 1, player);
		spikeFifteen = new Spike(0, 0, 1, 1, player);
		spikeSixteen = new Spike(0, 0, 1, 1, player);
	}
	
	//Criar os Lixos
	private void createTrampoline()
	{
		trampolineOne = new Trampoline(2336+160, 2208, player);
		trampolineTwo = new Trampoline(2480+160, 2176, player);
		trampolineTree = new Trampoline(0, 0, player);
		trampolineFour = new Trampoline(0, 0, player);
		trampolineFive = new Trampoline(0, 0, player);
		trampolineSix = new Trampoline(0, 0, player);
		trampolineSeven = new Trampoline(0, 0, player);
		trampolineEight = new Trampoline(0, 0, player);
		trampolineNine = new Trampoline(0, 0, player);
		trampolineTen = new Trampoline(0, 0, player);
	}
	
	//Criar os Lixos
	private void createOrbdash()
	{
		orbdashOne = new OrbDash(3040 + 160 - 64, 2048, player);
		orbdashTwo = new OrbDash(100, 100, player);
		orbdashTree = new OrbDash(100, 100, player);
		orbdashFour = new OrbDash(0, 0, player);
		orbdashFive = new OrbDash(0, 0, player);
		orbdashSix = new OrbDash(0, 0, player);
		orbdashSeven = new OrbDash(0, 0, player);
		orbdashEight = new OrbDash(0, 0, player);
		orbdashNine = new OrbDash(0, 0, player);
		orbdashTen = new OrbDash(0, 0, player);
		orbdashEleven = new OrbDash(0, 0, player);
		orbdashTwelve = new OrbDash(0, 0, player);
		orbdashThirteen = new OrbDash(0, 0, player);
		orbdashFourteen = new OrbDash(0, 0, player);
	}
	
	//Criar os Lixos
	private void createTriggerEvent()
	{
		triggereventOne = new TriggerEvent( 2656+12 + 160, 2048-16, 1, 2, 1, player);
		triggereventTwo = new TriggerEvent( 2672-64 + 160, 2352-36, 12, 1, 2, player);
		triggereventTree = new TriggerEvent(256 - 32 + 160, 2800 - 32, 4, 4, 3, player);
		triggereventFour = new TriggerEvent(100, 100, 100, 100, 4, player);
		triggereventFive = new TriggerEvent(100, 100, 100, 100, 5, player);
		triggereventSix = new TriggerEvent( 100, 100, 100, 100, 6, player);
	}

	//Desenhar casas, ou qualquer outra parte do cenário
	private void drawProps(Graphics g)
	{
		Props.draw(g, houseGrass, 1360 + 160, 2624, xLevelOffset, yLevelOffset);
		Props.draw(g, tent, 2096 + 160, 2496, xLevelOffset, yLevelOffset);
	}
	
	private void initClasses() 
	{
		levelManager = new LevelManager(game, LoadSave.LEVEL_ONE_DATA);

		player = new Player(playerInitialX * Game.SCALE , playerInitialY * Game.SCALE, (int)(18 * Game.SCALE), (int)(18 * Game.SCALE));
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		
		createTrash();
		createCheckPoint();
		createSpike();
		createTrampoline();
		createOrbdash();
		createTriggerEvent();
		
		pauseOverlay = new PauseOverlay(this);
	}
	
	//Programação burra:
	private void drawSpike(Graphics g)
	{
		spikeOne.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeTwo.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeTree.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeFour.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeFive.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeSix.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeSeven.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeEight.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeNine.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeTen.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeEleven.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeTwelve.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeThirteen.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeFourteen.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeFifteen.draw(g, maxLevelOffsetX, yLevelOffset, player);
		spikeSixteen.draw(g, maxLevelOffsetX, yLevelOffset, player);
	}
	
	//Desenhar os Lixos
	private void drawTrash(Graphics g) 
	{
		objTrashOne.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashTwo.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashTree.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashFour.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashFive.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashSix.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashSeven.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashEight.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashNine.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashTen.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashEleven.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashTwelve.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashThirTeen.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashFourteen.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
	}

	//Desenhar os Trampolins
	private void drawTrampoline(Graphics g) 
	{
		trampolineOne.draw(g, xLevelOffset, yLevelOffset, player,2);
		trampolineTwo.draw(g, xLevelOffset, yLevelOffset, player,2);
		trampolineTree.draw(g, xLevelOffset, yLevelOffset, player,0);
		trampolineFour.draw(g, xLevelOffset, yLevelOffset, player,0);
		trampolineFive.draw(g, xLevelOffset, yLevelOffset, player,0);
		trampolineSix.draw(g, xLevelOffset, yLevelOffset, player,0);
		trampolineSeven.draw(g, xLevelOffset, yLevelOffset, player,0);
		trampolineEight.draw(g, xLevelOffset, yLevelOffset, player,0);
		trampolineNine.draw(g, xLevelOffset, yLevelOffset, player,0);
		trampolineTen.draw(g, xLevelOffset, yLevelOffset, player,0);
	}

	//Desenhar os Checkpoints
	private void drawCheckpoint(Graphics g) 
	{
		checkPointOne.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointTwo.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointTree.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointFour.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointFive.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointSix.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointSeven.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointEight.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointNine.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointTen.draw(g, xLevelOffset, yLevelOffset, player);
	}
	
	private void drawOrbdash(Graphics g)
	{
		orbdashOne.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashTwo.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashTree.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashFour.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashFive.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashSix.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashSeven.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashEight.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashNine.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashTen.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashEleven.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashTwelve.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashThirteen.draw(g, maxLevelOffsetX, yLevelOffset, player);
		orbdashFourteen.draw(g, maxLevelOffsetX, yLevelOffset, player);
	}
	
	private void drawTriggerEvent(Graphics g)
	{
		triggereventOne.draw(g, maxLevelOffsetX, yLevelOffset, player);
		triggereventTwo.draw(g, maxLevelOffsetX, yLevelOffset, player);
		triggereventTree.draw(g, maxLevelOffsetX, yLevelOffset, player);
		triggereventFour.draw(g, maxLevelOffsetX, yLevelOffset, player);
		triggereventFive.draw(g, maxLevelOffsetX, yLevelOffset, player);
		triggereventSix.draw(g, maxLevelOffsetX, yLevelOffset, player);
	}

	private void updateAllThings()
	{	
		trashUpdateAnimationTick();

		spikeOne.update();
		spikeTwo.update();
		spikeTree.update();
		spikeFour.update();
		spikeFive.update();
		spikeSix.update();
		spikeSeven.update();
		spikeEight.update();
		spikeNine.update();
		spikeTen.update();
		spikeEleven.update();
		spikeTwelve.update();
		spikeThirteen.update();
		spikeFourteen.update();
		spikeFifteen.update();
		
		checkPointOne.update();
		checkPointTwo.update();
		checkPointTree.update();
		checkPointFour.update();
		checkPointFive.update();
		checkPointSix.update();
		checkPointSeven.update();
		checkPointEight.update();
		checkPointNine.update();
		checkPointTen.update();
		
		objTrashOne.update();
		objTrashTwo.update();
		objTrashTree.update();
		objTrashFour.update();
		objTrashFive.update();
		objTrashSix.update();
		objTrashSeven.update();
		objTrashEight.update();
		objTrashNine.update();
		objTrashTen.update();
		objTrashEleven.update();
		objTrashTwelve.update();
		objTrashThirTeen.update();
		objTrashFourteen.update();

		trampolineOne.update();
		trampolineTwo.update();
		trampolineTree.update();
		trampolineFour.update();
		trampolineFive.update();
		trampolineSix.update();
		trampolineSeven.update();
		trampolineEight.update();
		trampolineNine.update();
		trampolineTen.update();
		
		orbdashOne.update();
		orbdashTwo.update();
		orbdashTree.update();
		orbdashFour.update();
		orbdashFive.update();
		orbdashSix.update();
		orbdashSeven.update();
		orbdashEight.update();
		orbdashNine.update();
		orbdashTen.update();
		orbdashEleven.update();
		orbdashTwelve.update();
		orbdashThirteen.update();
		orbdashFourteen.update();
		
		triggereventOne.update();
		triggereventTwo.update();
		triggereventTree.update();
		triggereventFour.update();
		triggereventFive.update();
		triggereventSix.update();
	}

	public Playing(Game game) 
	{
		super(game);
		initClasses();
	}
	
	public void unpauseGame() 
	{
		paused = false;
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
		TextBox.update();
		
		checkCloseToBorderX();
		checkCloseToBorderY();
		
		//Se não estiver pausado, o jogo está rodando
		if(!paused)
		{
			levelManager.update();
		}
		
		//Sistema para fazer o player congelar no tempo quando necessario
		slowMotionTick--;
		if (Game.slowMotion>0)
		{
			slowMotionTickValue=Game.UPS_SET/Game.slowMotion;
			if (slowMotionTick<=0)
			{
				player.update();
				slowMotionTick = slowMotionTickValue;
			}
		}
		else
			slowMotionTickValue=0;
		


		pauseOverlay.update();
		
		updateAllThings();

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
	public void draw(Graphics g) 
	{
		if(paused)
		{
			pauseOverlay.pauseTrigger=true;
		}
		//remover eventualmente
		//System.out.println(tick);
		
		backGroundParallax(g);
		
		levelManager.draw(g, xLevelOffset, yLevelOffset);

		drawProps(g);
		drawTrash(g);
		drawTrampoline(g);
		drawCheckpoint(g);
		drawSpike(g);

		Props.draw(g, trashBig, 1471 + 160, 2624, xLevelOffset, yLevelOffset);
		
		drawOrbdash(g);
		player.render(g);
		
		drawTriggerEvent(g);

		g.setColor(new Color(0,0,0,black));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		
		if (triggereventTwo.getTick() <= 13)
			TextBox.resetTick();

		else if (triggereventTwo.getTick() > 13 && !triggereventTwo.getCantRepeatAgain())
			TextBox.draw(g);

		pauseOverlay.draw(g);
	}
	
	public static void increaseBlackSreen(int blackValue)
	{
		if (black<=255 - blackValue)
			black+=blackValue;
		else
			black=255;
	}
	
	public static void decreaseBlackSreen(int blackValue)
	{
		if (black>=0 + blackValue)
			black-=blackValue;
		else
			black=0;
	}

	private void backGroundParallax(Graphics g) 
	{
		g.drawImage(backgroundImage, 0,0,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageTwo,0 , (int)(distance - yLevelOffset*0.1) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageTree,0, (int)(distance - yLevelOffset*0.12) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageFour,0, (int)(distance - yLevelOffset*0.14) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageFive,0, (int)(distance - yLevelOffset*0.16) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		g.setColor(new Color(red,green,blue,220));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
	}

	private void checkCloseToBorderX() 
	{
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
	}
	
	private void checkCloseToBorderY() 
	{
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
		if (!player.getCantMove())
		{
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
					triggereventTwo.setLeft(true);
					break;
				}
				
				//Tecla para Baixo
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
				{
					player.setDown(true);
					triggereventTwo.setDown(true);
					break;
				}
				
				//Tecla para Direita
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
				{
					player.setRight(true);
					triggereventTwo.setRight(true);
					break;
				}
				
				//Tecla para Cima
				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
				{
					player.setUp(true);
					triggereventTwo.setUp(true);
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
					triggereventTwo.setDash(true);
					
					if ((((player.getDash() == 0) || player.canDashAgain==true) && 
						(((player.getDashPressed() == false))) && (player.dashEnable==true)) && (Playing.active==true))
					{
						player.canDashAgain=false;
						player.setDash(1);
						player.setDashPressed(true);
					}
					break;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!player.getCantMove())
		{
			switch (e.getKeyCode()) 
			{
				//Tecla para Esquerda
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
				{
					player.setLeft(false);
					triggereventTwo.setLeft(false);
					break;
				}
					
				//Tecla para Baixo
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
				{
					player.setDown(false);
					triggereventTwo.setDown(false);
					break;
				}
					
				//Tecla para Direita
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
				{
					player.setRight(false);
					triggereventTwo.setRight(false);
					break;
				}
				
				//Tecla para Cima
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
				{
					player.setUp(false);
					triggereventTwo.setUp(false);
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
					triggereventTwo.setDash(false);
					break;
				}
			}
		}
	}
	

}
