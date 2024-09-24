package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import entities.Player;
import levels.LevelManager;
import levels.Props;
import main.Game;
import ui.PauseOverlay;
import ui.TextBox;
import utilz.LoadSave;

public class Playing extends State implements Statemethods
{
	private LevelManager levelManager;
	private Player player;
	
	private PauseOverlay pauseOverlay;
	private boolean paused = false;
	
	//New Game Coordenadas: 260 * Game.SCALE, 2786 * Game.SCALE
	//Testar o começo do jogo: 300, 2700
	private int distance = 850;
	private int playerInitialX = 300;
	private int playerInitialY = 2710;
	
	public static int xLevelOffset, yLevelOffset;
	private int leftBorder = (int)(0.46 * Game.GAME_WIDTH);
	private int rightBorder = (int)(0.54 * Game.GAME_WIDTH);
	private int levelTilesWide = LoadSave.GetLevelData(LoadSave.LEVEL_ONE_DATA)[0].length;
	private int maxTilesOffsetX = levelTilesWide - Game.TILES_IN_WIDTH;
	private int maxLevelOffsetX = maxTilesOffsetX * Game.TILES_SIZE;
	
	private int topBorder = (int)(0.3 * Game.GAME_HEIGHT);
	private int bottomBorder = (int)(0.7 * Game.GAME_HEIGHT);
	private int levelTilesHigh = LoadSave.GetLevelData(LoadSave.LEVEL_ONE_DATA)[1].length;
	private int maxTilesOffsetY = levelTilesHigh - Game.TILES_IN_WIDTH;
	private int maxLevelOffsetY = maxTilesOffsetY * Game.TILES_SIZE;
	
	private static int subTick=0, tick=0;

	private boolean textBoxTrigger = false;
	
	//TextBox.loadBoxImages("/TextBoxMedium.png");
	//TextBox.loadBoxImages("/TextBoxMedium.png");
	
	private BufferedImage backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND);
	private BufferedImage backgroundImageTwo = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDTWO);
	private BufferedImage backgroundImageTree = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDTREE);
	private BufferedImage backgroundImageFour = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDFOUR);
	private BufferedImage backgroundImageFive = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDFIVE);
	
	
	private BufferedImage CasaMato = LoadSave.GetSpriteAtlas("/CasaMato.png");
	
	public Playing(Game game) 
	{
		super(game);
		initClasses();
	}
	
	public void unpauseGame() {
		paused = false;
	}

	private void initClasses() 
	{
		levelManager = new LevelManager(game, LoadSave.LEVEL_ONE_DATA);
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
	} 
	
	@Override
	public void draw(Graphics g) {
		
		//remover eventualmente
		//System.out.println(tick);
		//System.out.println(xLevelOffset);

		g.drawImage(backgroundImage, 0,0,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageTwo,0 , (int)(distance - yLevelOffset*0.1) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageTree,0, (int)(distance - yLevelOffset*0.12) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageFour,0, (int)(distance - yLevelOffset*0.14) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImageFive,0, (int)(distance - yLevelOffset*0.16) ,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		
		//(int)(1000 - xLevelOffset*0.3)
		
		levelManager.draw(g, xLevelOffset, yLevelOffset);

		drawProps(g);
		
		player.render(g);
		
		if(paused)
		{
			g.setColor(new Color(0,0,0,150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		}
		else
		{
			//drawTextBoxes(g);
		}	
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
	
	private void drawProps(Graphics g) {
		Props.draw(g, CasaMato, 100, 200, xLevelOffset, yLevelOffset);
		
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
				if ((player.getDash() == 0) && (player.getDashPressed() == false))
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
