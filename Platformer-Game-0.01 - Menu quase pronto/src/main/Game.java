package main;

import java.awt.Graphics;
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
	private final int FPS_SET = 1000;
	private final int UPS_SET = 120;
	
	private Introduction introduction;
	private Menu menu;
	private Playing playing;
	
	//private Playing playing;
	
	////Variaveis para definir o tamanho da tela, dos sprites e dos tamanhos deles
	//Quase 1280/720
	public final static float SCALE = 3f;
	
	public final static int TILES_DEFAULT_SIZE = 16;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
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
		//playing = new Menu(this);
	}
	
	//Inicializacao do Loop do Jogo
	private void startGameLoop() 
	{
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() 
	{
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
			case OPTIONS:
			{
				//menu.update();
				break;
			}
			case PLAYING:
			{
				playing.update();
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

		default:
			break;
		
		}
	}
	
	@Override
	public void run() 
	{
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

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
//		if(Gamestate.state == Gamestate.PLAYING)
//		{
//			playing.getPlayer().resetDirectionBooleans();
//		}
	}

	public Introduction getIntroduction()
	{
		return introduction;
	}
	
	public Menu getMenu()
	{
		return menu;
	}

//	public Playing getPlaying()
//	{
//		return playing;
//	}

}