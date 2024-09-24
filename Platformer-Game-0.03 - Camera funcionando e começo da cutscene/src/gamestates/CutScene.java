package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import entities.MiniPlayer;
import levels.LevelManager;
import levels.Props;
import main.Game;
import ui.TextBox;
import utilz.LoadSave;

public class CutScene extends State implements Statemethods
{
	private LevelManager levelManager;
	private MiniPlayer miniplayer;
	
	private int events=0;
	private int black = 255;
	
	//New Game Coordenadas: 260 * Game.SCALE, 2786 * Game.SCALE
	//Testar o começo do jogo: 300, 2700
	private int distance = 0;
	private int miniplayerInitialX = 10;
	private int miniplayerInitialY = 10;
	
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
	
	private BufferedImage backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND);
	private BufferedImage backgroundImageTwo = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDTWO);
	private BufferedImage backgroundImageTree = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDTREE);
	private BufferedImage backgroundImageFour = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDFOUR);
	private BufferedImage backgroundImageFive = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUNDFIVE);
	
	
	private BufferedImage CasaMato = LoadSave.GetSpriteAtlas("/CasaMato.png");
	
	public CutScene(Game game) 
	{
		super(game);
		initClasses();
	}

	private void initClasses() 
	{
		levelManager = new LevelManager(game, LoadSave.CUTSCENE_DATA);
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
		
		//xLevelOffset+=1;


		if (events == 0)
		{
			if (black>0)
			{
				black-=1;
			}
			else
			{
				black=255;
				events=1;
			}
		}
		else if (events ==1)
		{
			resetThings();
			
			TextBox.loadBoxImages("/TextBoxMedium.png");
			events=2;
			
			
		}
		else if(events==2)
		{
			
			updateThings();
			
			if (tick>200)
				events=2;
		}
		
		
		
		
	} 
	
	
	private void resetThings()
	{
		tick=0;
		subTick=0;
		TextBox.resetTick();
	}
	
	private void updateThings()
	{
		subTick++;
		if (subTick>10)
		{
			tick++;
			subTick=0;
		}
		
		TextBox.update();
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
		
		miniplayer.render(g);
	
		if (events>1)
			TextBox.draw(g);
		
		if (events == 0)
		{
			g.setColor(new Color(0,0,0,black));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		}
	}
	
	private void drawProps(Graphics g) {
		Props.draw(g, CasaMato, 100, 200, xLevelOffset, yLevelOffset);
		
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
			//Tecla para Esquerda
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
			{
				miniplayer.setLeft(true);
				break;
			}
			
			//Tecla para Baixo
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
			{
				miniplayer.setDown(true);
				break;
			}
			
			//Tecla para Direita
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
			{
				miniplayer.setRight(true);
				break;
			}
			
			//Tecla para Cima
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
			{
				miniplayer.setUp(true);
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
				miniplayer.setLeft(false);
				break;
			}
				
			//Tecla para Baixo
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
			{
				miniplayer.setDown(false);
				break;
			}
				
			//Tecla para Direita
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
			{
				miniplayer.setRight(false);
				break;
			}
			
			//Tecla para Cima
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
			{
				miniplayer.setUp(false);
				break;
			}
		}
	}
}
