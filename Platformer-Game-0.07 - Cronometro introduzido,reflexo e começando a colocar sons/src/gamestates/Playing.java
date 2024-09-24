package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import entities.CheckPoint;
import entities.InvisibleWall;
import entities.MiniPlayer;
import entities.OrbDash;
import entities.Player;
import entities.PlayerReflection;
import entities.Spike;
import entities.Trampoline;
import entities.Trash;
import entities.TriggerEvent;
import entities.TriggerSkyChange;
import entities.Vehicle;
import levels.AnimatedProp;
import levels.LevelManager;
import levels.Props;
import main.Game;
import ui.PauseOverlay;
import ui.TextBox;
import utilz.LoadSave;

public class Playing extends State implements Statemethods
{
	public static int trashAnimationTick, trashAnimationIndex, trashAnimationSpeed = 16;

	private LevelManager levelManager;
	private Player player;
	private PlayerReflection playerReflection;
	private MiniPlayer miniplayer;
	private TriggerSkyChange triggerskychange;
	private AnimatedProp eye, moveKeys, upKeys;
	private InvisibleWall wallOne, wallTwo;
	private Vehicle garbageTruck;
	
	private PauseOverlay pauseOverlay;
	public static boolean paused = false, active = true;
	
	private int distance = (int) (212 * Game.SCALE);
	
	public static int garbageTruckSpeed=0;
	
	private CheckPoint checkPointOne, checkPointTwo, checkPointThree, checkPointFour, checkPointFive, 
	checkPointSix,checkPointSeven;
	
	private Spike spikeOne, spikeTwo, spikeThree, spikeFour, spikeFive, spikeSix, spikeSeven, spikeEight, 
	spikeNine, spikeTen, spikeEleven, spikeTwelve, spikeThirteen, spikeFourteen, spikeFifteen, spikeSixteen,
	spikeSeventeen, spikeEighteen, spikeNineteen, spikeTwenty, spikeTwentyOne, spikeTwentyTwo, spikeTwentyThree, 
	spikeTwentyFour, spikeTwentyFive, spikeTwentySix, spikeTwentySeven, spikeTwentyEight, spikeTwentyNine, spikeThirty,
	spikeThirtyOne, spikeThirtyTwo, spikeThirtyThree, spikeThirtyFour, spikeThirtyFive, spikeThirtySix, spikeThirtySeven,
	spikeThirtyEight, spikeThirtyNine, spikeForty;
	
	private Trash objTrashOne, objTrashTwo, objTrashThree, objTrashFour, objTrashFive, objTrashSix, objTrashSeven,
	objTrashEight, objTrashNine, objTrashTen;
	
	private Trampoline trampolineOne, trampolineTwo, trampolineThree, trampolineFour, trampolineFive, trampolineSix;
	
	private OrbDash orbdashOne, orbdashTwo, orbdashThree, orbdashFour, orbdashFive, orbdashSix;
	
	private TriggerEvent triggereventOne, triggereventTwo, triggereventThree, triggereventFour, triggereventFive;
	
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

	//Céu azul padrão:
	public static int brightSkyRed = 70;
	public static int brightSkyGreen = 200;
	public static int brightSkyBlue = 240;
	public static int brightSkyAlpha = 220;
	
	//Player começa nesta coordenada
	public static int playerInitialX=424;
	public static int playerInitialY=2800;
	
	//Céu com a cor padrão:
	public static int red = brightSkyRed, blue = brightSkyBlue, green = brightSkyGreen, alpha = brightSkyAlpha;
	
	private BufferedImage 
	backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND),
	backgroundImageTwo = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDTWO),
	backgroundImageThree = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDTHREE),
	backgroundImageFour = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDFOUR),
	backgroundImageFive = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDFIVE),
	
	houseGrass = LoadSave.GetSpriteAtlas("/HouseGrass.png"),
	houseCommon = LoadSave.GetSpriteAtlas("/HouseCommon.png"),
	houseBroken = LoadSave.GetSpriteAtlas("/HouseBroken.png"),
	tent = LoadSave.GetSpriteAtlas("/Tent.png"),
	
	trashBig = LoadSave.GetSpriteAtlas("/TrashBig.png"),
	trashSmall = LoadSave.GetSpriteAtlas("/TrashBig.png"),
	cave = LoadSave.GetSpriteAtlas("/Cave.png"),
	treeSmall = LoadSave.GetSpriteAtlas("/TreeSmall.png"),
	treeBig = LoadSave.GetSpriteAtlas("/TreeBig.png"),
	
	ladder = LoadSave.GetSpriteAtlas("/Ladder.png"),
	bridge = LoadSave.GetSpriteAtlas("/Bidge.png"),
	parking = LoadSave.GetSpriteAtlas("/Parking.png"),
	
	invisibleWallOne = LoadSave.GetSpriteAtlas("/InvisibleWallOne.png"),
	invisibleWallTwo = LoadSave.GetSpriteAtlas("/InvisibleWallTwo.png"),
	dirtWall = LoadSave.GetSpriteAtlas("/DirtWall.png");
	
///////////////////////////////////////////////////////////////////////////////
	
	public static boolean newGame=false;
	
	//Ligar ou desligar o Dash
	public static boolean dashEnable = true;
	
	//Teleportar para qualquer checkpoint, 0 é o comeco e 7 é o ultimo
	private static int local = 6;
	public static int black=0;
	
	//Visualizar hitboxes
	public static boolean debugDrawHitbox=false;
	
	//Sistema de carregar apenas o que está dentro da tela
	//Mude debug para 1 ou maior para visualizar em jogo
	public static int debugLevelDraw = 0;//2
	public static int debugPropsDraw = 0;//4
	
///////////////////////////////////////////////////////////////////////////////
	
	private void initClasses()
	{
		levelManager = new LevelManager(game, LoadSave.LEVEL_ONE_DATA);

		player = new Player(playerInitialX * Game.SCALE , playerInitialY * Game.SCALE, (int)(18 * Game.SCALE), (int)(18 * Game.SCALE));
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		
		playerReflection = new PlayerReflection(4322 * Game.SCALE , 3104 * Game.SCALE, (int)(18 * Game.SCALE), (int)(18 * Game.SCALE), player);
		playerReflection.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		
		miniplayer = new MiniPlayer(playerInitialX * Game.SCALE , playerInitialY * Game.SCALE, (int)(-100 * Game.SCALE), (int)(-100 * Game.SCALE));
		miniplayer.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		
		triggerskychange = new TriggerSkyChange(player);
		
		eye = new AnimatedProp("/Eye.png", 4204, 2664, 5, 24, 8, 100);
		
		moveKeys = new AnimatedProp("/MoveKeys.png", 480, 2720-28, 32, 32, 2, 100);
		upKeys = new AnimatedProp("/UpKeys.png", 480+64, 2720-28, 16, 64, 4, 100);
		
		createTrash();
		createCheckPoint();
		createSpike();
		createTrampoline();
		createOrbdash();
		createTriggerEvent();
		
		pauseOverlay = new PauseOverlay(this);
		
		//Para debug, teleportar o player para locais especificos
		switch(local)
		{case 1:{Player.lastX=(int) checkPointOne.getX();Player.lastY=(int)   checkPointOne.getY();break;}
		 case 2:{Player.lastX=(int) checkPointTwo.getX();Player.lastY=(int)   checkPointTwo.getY();break;}
		 case 3:{Player.lastX=(int) checkPointThree.getX();Player.lastY=(int) checkPointThree.getY();break;}
		 case 4:{Player.lastX=(int) checkPointFour.getX();Player.lastY=(int)  checkPointFour.getY();break;}
		 case 5:{Player.lastX=(int) checkPointFive.getX();Player.lastY=(int)  checkPointFive.getY();break;}
		 case 6:{Player.lastX=(int) checkPointSix.getX();Player.lastY=(int)   checkPointSix.getY();break;}
		 case 7:{Player.lastX=(int) checkPointSeven.getX();Player.lastY=(int) checkPointSeven.getY();break;}
		}
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
		playerReflection.resetDirectionBooleans();
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public PlayerReflection getPlayerReflection()
	{
		return playerReflection;
	}
	
	@Override
	public void update() 
	{
		TextBox.update();
		
		eye.update();
		moveKeys.update();
		upKeys.update();
		
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
				playerReflection.update();
				slowMotionTick = slowMotionTickValue;
			}
		}
		else
			slowMotionTickValue=0;
		
		triggerskychange.update();

		pauseOverlay.update();
		
		updateAllThings();

	} 
	
	private void trashUpdateAnimationTick() 
	{
		if (!Playing.paused)
		{
			Playing.trashAnimationTick++;
			if (Playing.trashAnimationTick >= Playing.trashAnimationSpeed) 
			{
				Playing.trashAnimationTick = 0;
				Playing.trashAnimationIndex++;
	
					if (Playing.trashAnimationIndex >= 8)
					{
						Playing.trashAnimationIndex = 0;
					}
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
		
		backGroundParallax(g);
		
		levelManager.draw(g, xLevelOffset, yLevelOffset);
		
		drawProps(g);
		
		drawTrash(g);
		drawTrampoline(g);
		drawCheckpoint(g);
		drawSpike(g);
		
		Props.draw(g, trashBig, 1631, 2624, xLevelOffset, yLevelOffset);
		Props.draw(g, trashBig, 2240-40, 2080, xLevelOffset, yLevelOffset);
		
		drawOrbdash(g);
		
		eye.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		moveKeys.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		upKeys.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		
		player.render(g);
		playerReflection.render(g);
		
		garbageTruck.draw(g, 0, yLevelOffset);
		
		drawTriggerEvent(g);

		if (triggereventTwo.getTick() <= 13)
			TextBox.resetTick();
		
		else if (triggereventTwo.getTick() > 13 && triggereventTwo.getCollisionTrigger() !=2)
			TextBox.draw(g);
		
		pauseOverlay.draw(g);

		g.setColor(new Color(0,0,0,black));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
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
		g.drawImage(backgroundImageThree,0, (int)(distance - yLevelOffset*0.12) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageFour,0, (int)(distance - yLevelOffset*0.14) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageFive,0, (int)(distance - yLevelOffset*0.16) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		g.setColor(new Color(red,green,blue,alpha));
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
					playerReflection.setLeft(true);
					triggereventTwo.setLeft(true);
					break;
				}
				
				//Tecla para Baixo
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
				{
					player.setDown(true);
					playerReflection.setDown(true);
					triggereventTwo.setDown(true);
					break;
				}
				
				//Tecla para Direita
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
				{
					player.setRight(true);
					playerReflection.setRight(true);
					triggereventTwo.setRight(true);
					break;
				}
				
				//Tecla para Cima
				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
				{
					player.setUp(true);
					playerReflection.setUp(true);
					triggereventTwo.setUp(true);
					break;
				}
				
				//Tecla para Pular
				case KeyEvent.VK_SPACE:
				case KeyEvent.VK_Z:
				{
					player.setJump(true);
					playerReflection.setJump(true);
					break;
				}
					
				//Tecla para Dash
				case KeyEvent.VK_F:
				case KeyEvent.VK_E:
				case KeyEvent.VK_X:
				{
					triggereventTwo.setDash(true);
					
					if ((((player.getDash() == 0) || player.canDashAgain==true) && 
						(((player.getDashPressed() == false))) && (dashEnable==true)) && (Playing.active==true))
					{
						player.canDashAgain=false;
						player.setDash(1);
						player.setDashPressed(true);
						
						playerReflection.setDash(1);
						playerReflection.setDashPressed(true);
					}
					break;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		if (!player.getCantMove())
		{
			switch (e.getKeyCode()) 
			{
				//Tecla para Esquerda
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
				{
					player.setLeft(false);
					playerReflection.setLeft(false);
					triggereventTwo.setLeft(false);
					break;
				}
					
				//Tecla para Baixo
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
				{
					player.setDown(false);
					playerReflection.setDown(false);
					triggereventTwo.setDown(false);
					break;
				}
					
				//Tecla para Direita
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
				{
					player.setRight(false);
					playerReflection.setRight(false);
					triggereventTwo.setRight(false);
					break;
				}
				
				//Tecla para Cima
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
				{
					player.setUp(false);
					playerReflection.setUp(false);
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
						
						playerReflection.setJump(false);
						playerReflection.setJumpTrigger(false);
					}
					break;
				}
				
				//Tecla para Dash
				case KeyEvent.VK_F:
				case KeyEvent.VK_E:
				case KeyEvent.VK_X:
				{
					player.setDashPressed(false);
					playerReflection.setDashPressed(false);
					triggereventTwo.setDash(false);
					break;
				}
			}
		}
	}
	
	//Programação burra:
	private void drawSpike(Graphics g)
	{
		spikeOne.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTwo.draw(g, xLevelOffset, yLevelOffset, player);
		spikeThree.draw(g, xLevelOffset, yLevelOffset, player);
		spikeFour.draw(g, xLevelOffset, yLevelOffset, player);
		spikeFive.draw(g, xLevelOffset, yLevelOffset, player);
		spikeSix.draw(g, xLevelOffset, yLevelOffset, player);
		spikeSeven.draw(g, xLevelOffset, yLevelOffset, player);
		spikeEight.draw(g, xLevelOffset, yLevelOffset, player);
		spikeNine.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTen.draw(g, xLevelOffset, yLevelOffset, player);
		spikeEleven.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTwelve.draw(g, xLevelOffset, yLevelOffset, player);
		spikeThirteen.draw(g, xLevelOffset, yLevelOffset, player);
		spikeFourteen.draw(g, xLevelOffset, yLevelOffset, player);
		spikeFifteen.draw(g, xLevelOffset, yLevelOffset, player);
		spikeSixteen.draw(g, xLevelOffset, yLevelOffset, player);
		spikeSeventeen.draw(g, xLevelOffset, yLevelOffset, player);
		spikeEighteen.draw(g, xLevelOffset, yLevelOffset, player);
		spikeNineteen.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTwenty.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTwentyOne.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTwentyTwo.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTwentyThree.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTwentyFour.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTwentyFive.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTwentySix.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTwentySeven.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTwentyEight.draw(g, xLevelOffset, yLevelOffset, player);
		spikeTwentyNine.draw(g, xLevelOffset, yLevelOffset, player);
		spikeThirty.draw(g, xLevelOffset, yLevelOffset, player);
		spikeThirtyOne.draw(g, xLevelOffset, yLevelOffset, player);
		spikeThirtyTwo.draw(g, xLevelOffset, yLevelOffset, player);
		spikeThirtyThree.draw(g, xLevelOffset, yLevelOffset, player);
		spikeThirtyFour.draw(g, xLevelOffset, yLevelOffset, player);
		spikeThirtyFive.draw(g, xLevelOffset, yLevelOffset, player);
		spikeThirtySix.draw(g, xLevelOffset, yLevelOffset, player);
		spikeThirtySeven.draw(g, xLevelOffset, yLevelOffset, player);
		spikeThirtyEight.draw(g, xLevelOffset, yLevelOffset, player);
		spikeThirtyNine.draw(g, xLevelOffset, yLevelOffset, player);
		spikeForty.draw(g, xLevelOffset, yLevelOffset, player);
	}
	
	//Desenhar os Lixos
	private void drawTrash(Graphics g) 
	{
		objTrashOne.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashTwo.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashThree.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashFour.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashFive.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashSix.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashSeven.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashEight.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashNine.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
		objTrashTen.draw(g, xLevelOffset, yLevelOffset, player, miniplayer);
	}

	//Desenhar os Trampolins
	private void drawTrampoline(Graphics g) 
	{
		trampolineOne.draw(g, xLevelOffset, yLevelOffset, player,2);
		trampolineTwo.draw(g, xLevelOffset, yLevelOffset, player,2);
		trampolineThree.draw(g, xLevelOffset, yLevelOffset, player,3);
		trampolineFour.draw(g, xLevelOffset, yLevelOffset, player,3);
		trampolineFive.draw(g, xLevelOffset, yLevelOffset, player,3);
		trampolineSix.draw(g, xLevelOffset, yLevelOffset, player,2);
	}

	//Desenhar os Checkpoints
	private void drawCheckpoint(Graphics g) 
	{
		checkPointOne.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointTwo.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointThree.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointFour.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointFive.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointSix.draw(g, xLevelOffset, yLevelOffset, player);
		checkPointSeven.draw(g, xLevelOffset, yLevelOffset, player);
	}
	
	private void drawOrbdash(Graphics g)
	{
		orbdashOne.draw(g, xLevelOffset, yLevelOffset, player);
		orbdashTwo.draw(g, xLevelOffset, yLevelOffset, player);
		orbdashThree.draw(g, xLevelOffset, yLevelOffset, player);
		orbdashFour.draw(g, xLevelOffset, yLevelOffset, player);
		orbdashFive.draw(g, xLevelOffset, yLevelOffset, player);
		orbdashSix.draw(g, xLevelOffset, yLevelOffset, player);
	}
	
	private void drawTriggerEvent(Graphics g)
	{
		triggereventOne.draw(g, xLevelOffset, yLevelOffset, player);
		triggereventTwo.draw(g, xLevelOffset, yLevelOffset, player);
		triggereventThree.draw(g, xLevelOffset, yLevelOffset, player);
		triggereventFour.draw(g, xLevelOffset, yLevelOffset, player);
		triggereventFive.draw(g, xLevelOffset, yLevelOffset, player);
	}

	private void updateAllThings()
	{	
		trashUpdateAnimationTick();

		spikeOne.update();
		spikeTwo.update();
		spikeThree.update();
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
		spikeSixteen.update();
		spikeSeventeen.update();
		spikeEighteen.update();
		spikeNineteen.update();
		spikeTwenty.update();
		spikeTwentyOne.update();
		spikeTwentyTwo.update();
		spikeTwentyThree.update();
		spikeTwentyFour.update();
		spikeTwentyFive.update();
		spikeTwentySix.update();
		spikeTwentySeven.update();
		spikeTwentyEight.update();
		spikeTwentyNine.update();
		spikeThirty.update();
		spikeThirtyOne.update();
		spikeThirtyTwo.update();
		spikeThirtyThree.update();
		spikeThirtyFour.update();
		spikeThirtyFive.update();
		spikeThirtySix.update();
		spikeThirtySeven.update();
		spikeThirtyEight.update();
		spikeThirtyNine.update();
		spikeForty.update();
		
		checkPointOne.update();
		checkPointTwo.update();
		checkPointThree.update();
		checkPointFour.update();
		checkPointFive.update();
		checkPointSix.update();
		checkPointSeven.update();
		
		objTrashOne.update();
		objTrashTwo.update();
		objTrashThree.update();
		objTrashFour.update();
		objTrashFive.update();
		objTrashSix.update();
		objTrashSeven.update();
		objTrashEight.update();
		objTrashNine.update();
		objTrashTen.update();

		trampolineOne.update();
		trampolineTwo.update();
		trampolineThree.update();
		trampolineFour.update();
		trampolineFive.update();
		trampolineSix.update();
		
		orbdashOne.update();
		orbdashTwo.update();
		orbdashThree.update();
		orbdashFour.update();
		orbdashFive.update();
		orbdashSix.update();
		
		triggereventOne.update();
		triggereventTwo.update();
		triggereventThree.update();
		triggereventFour.update();
		triggereventFive.update();
		
		wallOne.update();
		wallTwo.update();
		
		garbageTruck.update();
		garbageTruck.setXSpeed(garbageTruckSpeed);
	}
	
	//Criar os Lixos
	private void createTrash()
	{
		objTrashOne = new Trash("/TrashFive.png", 1472 + 160, 2624, player, miniplayer);
		objTrashTwo = new Trash("/TrashFour.png", 2070 + 160, 2496, player, miniplayer);
		objTrashThree = new Trash("/TrashTwoB.png", 3264, 2320, player, miniplayer);
		objTrashFour = new Trash("/TrashOne.png", 1184, 3104, player, miniplayer);
		objTrashFive = new Trash("/TrashOne.png", 2736, 1904, player, miniplayer);
		objTrashSix = new Trash("/TrashFive.png", 2201, 2080, player, miniplayer);
		objTrashSeven = new Trash("/TrashFour.png", 4128+16, 2656, player, miniplayer);
		objTrashEight = new Trash("/TrashTwoB.png", 5328, 3200, player, miniplayer);
		objTrashNine = new Trash("/TrashFour.png", 5680, 3232, player, miniplayer);
		objTrashTen = new Trash("/TrashOne.png", 2752, 2336, player, miniplayer);
	}
	
	//Criar Checkpoints
	private void createCheckPoint()
	{
		checkPointOne = new CheckPoint(1776, 2592, player);
		checkPointTwo = new CheckPoint(2432, 2208, player);
		checkPointThree = new CheckPoint(3088, 2320, player);
		checkPointFour = new CheckPoint(3712, 1968, player);
		checkPointFive = new CheckPoint(4000, 2176, player);
		checkPointSix = new CheckPoint(4464, 3056, player);
		
		//Usar para testes:
		checkPointSeven = new CheckPoint(6200, 3472, player);
	}
	
	//Criar os Lixos
	private void createSpike()
	{
		spikeOne = new Spike(1840, 2624, 3, 1, player);
		spikeTwo = new Spike(2512, 2256, 3, 1, player);
		spikeThree = new Spike(2656, 2224, 1, 1, player);
		spikeFour = new Spike(2768, 2352, 9, 1, player);
		spikeFive = new Spike(3104, 2352, 10, 1, player);
		spikeSix = new Spike(3264+16, 2320, 2, 1, player);
		spikeSeven = new Spike(0, 5376, 40, 20, player);
		spikeEight = new Spike(3152, 1984 - 10, 7, 1, player);
		spikeNine = new Spike(3136, 1776, 3, 1, player);
		spikeTen = new Spike(3232, 1840, 1, 1, player);
		spikeEleven = new Spike(3248, 1872, 2, 1, player);
		spikeTwelve = new Spike(3312+2,1872-74, 5, 1, player);
		spikeThirteen = new Spike(3424, 1776, 2, 1, player);
		spikeFourteen = new Spike(3280, 1920, 5, 1, player);
		spikeFifteen = new Spike(3360, 1936, 5, 1, player);
		spikeSixteen = new Spike(3440, 1952, 11, 1, player);
		spikeSeventeen = new Spike(3616, 1920, 2, 1, player);
		spikeEighteen = new Spike(3616, 1920-42, 1, 1, player);
		spikeNineteen = new Spike(3808, 2016, 23, 1, player);
		spikeTwenty = new Spike(4224, 2176, 2, 1, player);
		spikeTwentyOne = new Spike(3840 , 1888, 1, 1, player);
		spikeTwentyTwo = new Spike(3904 , 1856, 1, 1, player);
		spikeTwentyThree = new Spike(3936 , 1888, 1, 1, player);
		spikeTwentyFour = new Spike(3840-8, 1888+7, 2, 5, player);
		spikeTwentyFive = new Spike(3904-8, 1856+7, 1, 7, player);
		spikeTwentySix = new Spike(3936+8, 1888+7, 1, 11, player);
		spikeTwentySeven = new Spike(4032, 2272, 8, 1, player);
		spikeTwentyEight = new Spike(3936-7, 2256-6, 1, 1, player);
		spikeTwentyNine = new Spike(3904, 2016-106, 1, 1, player);
		spikeThirty = new Spike(3952+7, 2192-6, 1, 1, player);
		spikeThirtyOne = new Spike(3952+7, 2320-6, 1, 1, player);
		spikeThirtyTwo = new Spike(3936-7, 2384-6, 1, 1, player);
		spikeThirtyThree = new Spike(3936-7, 2864-52, 1, 9, player);
		spikeThirtyFour = new Spike(3936-7, 2784-52, 1, 9, player);
		spikeThirtyFive = new Spike(3952+7, 2672-6, 1, 1, player);
		spikeThirtySix = new Spike(3952+7, 2624-6, 1, 1, player);
		spikeThirtySeven = new Spike(3949, 2432, 2, 1, player);
		spikeThirtyEight = new Spike(3936-7, 2560-36, 1, 6, player);
		spikeThirtyNine = new Spike(3936, 2896, 2, 1, player);
		spikeForty = new Spike(5904, 3404-16, 26, 7, player);
	}
	
	//Criar os Lixos
	private void createTrampoline()
	{
		trampolineOne = new Trampoline(2496, 2208, player);
		trampolineTwo = new Trampoline(2640, 2176, player);
		trampolineThree = new Trampoline(4064, 2240, player);
		trampolineFour = new Trampoline(3792, 1952, player);
		trampolineFive = new Trampoline(4048, 1984, player);
		trampolineSix = new Trampoline(4832, 3040, player);
	}
	
	//Criar os Lixos
	private void createOrbdash()
	{
		orbdashOne = new OrbDash(3106, 2016, player);
		orbdashTwo = new OrbDash(3504, 1808, player);
		orbdashThree = new OrbDash(3584, 1872, player);
		orbdashFour = new OrbDash(3888-22, 2016-144, player);
		orbdashFive = new OrbDash(3936+16, 1888-16, player);
		orbdashSix = new OrbDash(5152+96, 2832+12, player);
	}
	
	//Criar os Lixos
	private void createTriggerEvent()
	{
		triggereventOne = new TriggerEvent( 2828, 2032, 1, 2, 1, player);
		triggereventTwo = new TriggerEvent( 2768, 2316, 12, 1, 2, player);
		triggereventThree = new TriggerEvent(384, 2768, 4, 4, 3, player);
		triggereventFour = new TriggerEvent(6336-80, 3472-600, 40, 40, 4, player);
		triggereventFive = new TriggerEvent(6608-128, 4016-16, 100, 4, 5, player);
		
		wallOne = new InvisibleWall( invisibleWallOne ,3968, 2656-16, 304, 32, player);
		wallTwo = new InvisibleWall( invisibleWallTwo ,2864, 2048-16, 128, 32, player);
		
		garbageTruck = new Vehicle(true,-35,4013);
	}
	
	//Desenhar casas, ou qualquer outra parte do cenário
	private void drawProps(Graphics g)
	{
		wallOne.draw(g, xLevelOffset, yLevelOffset, player);
		wallTwo.draw(g, xLevelOffset, yLevelOffset, player);
		
		Props.draw(g, houseGrass, 1520, 2624, xLevelOffset, yLevelOffset);
		Props.draw(g, tent, 2256, 2496, xLevelOffset, yLevelOffset);
		Props.draw(g, houseBroken, 2672, 1904, xLevelOffset, yLevelOffset);
		Props.draw(g, houseCommon, 2272-32-40, 2080, xLevelOffset, yLevelOffset);
		Props.draw(g, cave, 4192-32, 2656, xLevelOffset, yLevelOffset);

		Props.draw(g, treeBig, 720, 2704, xLevelOffset, yLevelOffset);
		Props.draw(g, treeSmall, 624, 2704, xLevelOffset, yLevelOffset);
		Props.draw(g, treeSmall, 944, 2608, xLevelOffset, yLevelOffset);
		Props.draw(g, treeBig, 1200, 2576, xLevelOffset, yLevelOffset);
		Props.draw(g, treeBig, 1968, 2592, xLevelOffset, yLevelOffset);
		Props.draw(g, treeSmall, 2576, 2176, xLevelOffset, yLevelOffset);
		Props.draw(g, treeBig, 100, 100, xLevelOffset, yLevelOffset);
		
		Props.draw(g, bridge, 4508, 3056, xLevelOffset, yLevelOffset);
		Props.draw(g, ladder, 5200, 3200, xLevelOffset, yLevelOffset);
		Props.draw(g, treeBig, 4720, 3040, xLevelOffset, yLevelOffset);
		
		Props.draw(g, dirtWall, 5888-48, 3360+160-48, xLevelOffset, yLevelOffset);
	}
}
