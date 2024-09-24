package main;

import java.awt.Graphics;

import entities.CheckPoint;
import entities.Player;
import entities.Trash;
import gamestates.CutScene;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import gamestates.Introduction;

public class Game implements Runnable 
{
	//Dentro de Game chamamos todas as funções do jogo:
	//A janela, que corresponde como uma borda para uma pintura
	//O panel, que corresponde à pintura em si
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	
	//Constante de quantas atualizacoes de repaint() o jogo tera
	public final static int UPS_SET = 120;
	public static int fps = 120;
	public static int ups = UPS_SET;
	
	public static int slowMotion = UPS_SET;
	
	public static int collectedTrashes=0;
	public static int deathCount=0;
	public static int attemptCount=0;
	
	double timePerFrame = 1000000000.0 / fps;
	double timePerUpdate = 1000000000.0 / ups;
	
	private Introduction introduction;
	private Menu menu;
	private Playing playing;
	private CutScene cutscene;
	
	//private Playing playing;
	
	////Variaveis para definir o tamanho da tela, dos sprites e dos tamanhos deles
	//Quase 1280 / 720
	public final static float SCALE = 4f;

	//Tamanho da tela em pixels: 288 / 176
	public final static int TILES_DEFAULT_SIZE = 16;
	public final static int TILES_IN_WIDTH = 18;
	public final static int TILES_IN_HEIGHT = 11;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
	public static boolean potatoMode = false;
	
	public Game()
	{
		initClasses();

		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();

		startGameLoop();
	}

	private void initClasses() 
	{
		introduction = new Introduction(this);
		menu = new Menu(this);
		playing = new Playing(this);
		cutscene = new CutScene(this);
	}
	
	//Inicializacao do Loop do Jogo
	private void startGameLoop() 
	{
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() 
	{
		timePerFrame = 1000000000.0 / fps;
		timePerUpdate = 1000000000.0 / ups;
		
		//Forma de trocar entre o menu e as fases
		switch(Gamestate.state)
		{
			case INTRODUCTION:
			{
				introduction.update();
				break;
			}
			case MENU:
			{
				menu.update();
				break;
			}
			case PLAYING:
			{
				playing.update();
				break;
			}
			case CUTSCENE:
			{
				Trash.restartAllTrashes();
				cutscene.update();
				Trash.trashActive=true;
				Player.lastX=(int) (Playing.playerInitialX * Game.SCALE);
				Player.lastY=(int) (Playing.playerInitialY * Game.SCALE);
				CheckPoint.restartAllCheckpoints();
				break;
			}
			case QUIT:
			default:
				System.exit(0);
				break;
		}
	}

	//Método que desenha na tela a fase atual, como o menu ou mesmo uma fase do jogo
	public void render(Graphics g) 
	{
		switch(Gamestate.state)
		{
		case INTRODUCTION:
		{
			introduction.draw(g);
			break;
		}
		case MENU:
		{
			menu.draw(g);
			break;
		}
		case PLAYING:
		{
			playing.draw(g);
			break;
		}
		case CUTSCENE:
		{
			cutscene.draw(g);
			break;
		}

		default:
			break;
		
		}
	}
	
	@Override
	public void run() 
	{
		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}

	public void windowFocusLost() 
	{
		System.out.println("Window Focus Lost");
		playing.getPlayer().resetDirectionBooleans();
		menu.getPlayer().resetDirectionBooleans();
	}

	public Introduction getIntroduction()
	{
		return introduction;
	}
	
	public Menu getMenu()
	{
		return menu;
	}

	public Playing getPlaying()
	{
		return playing;
	}

	public CutScene getCutscene() {
		return cutscene;
	}

}