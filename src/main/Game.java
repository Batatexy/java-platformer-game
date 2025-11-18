package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import entities.CheckPoint;
import gamestates.CutScene;
import gamestates.End;
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
	
	//Constante de quantas atualizacoes de repaint()
	public final static int UPS_SET = 120;
	public static int fps = 1000;
	public static int ups = UPS_SET;
	
	public static int collectedTrashes=0;
	public static int deathCount=0;
	public static int attemptCount=0;
	public static float timeMil;
	public static float timeMilPause;
	public static float timeSec;
	public static float timeMin;
	
	double timePerFrame = 1000000000.0 / fps;
	double timePerUpdate = 1000000000.0 / ups;
	
	private Introduction introduction;
	private Menu menu;
	private Playing playing;
	private CutScene cutscene;
	private End end;
	private AudioPlayer audioPlayer;
	
	////Variaveis para definir o tamanho da tela, dos sprites e dos tamanhos deles
	public final static float SCALE = 4f;
	
	public final static int TILES_DEFAULT_SIZE = 16;
	public final static int TILES_IN_WIDTH = 18;
	public final static int TILES_IN_HEIGHT = 11;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
	public static boolean potatoMode = false;
	public static int slowMotion = UPS_SET;

	
	public Game()
	{
		initClasses();
		
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();

		//Inicializacao do Loop do Jogo
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	private void initClasses() 
	{
		audioPlayer = new AudioPlayer();
		introduction = new Introduction(this);
		menu = new Menu(this);
		playing = new Playing(this);
		cutscene = new CutScene(this);
		end = new End(this);
	}
	
	public void update() 
	{
		timePerFrame = 1000000000.0 / fps;
		timePerUpdate = 1000000000.0 / ups;
		
		//System.out.println(audioPlayer.getSongVolume() + "/" +audioPlayer.getCurrentSongId());
		//System.out.println(Playing.newGame);
		
		
		//Forma de trocar entre o menu e as fases
		switch(Gamestate.state)
		{
			case INTRODUCTION:
			{
				introduction.update();
				
				audioPlayer.setLevelSong(audioPlayer.MENU, 0.85f);
				
				break;
			}
			
			case MENU:
			{
				menu.update();
				
				if (!Playing.newGame)
				{
					audioPlayer.setLevelSong(audioPlayer.MENU, 0.9f);
				}
				else
				{
					audioPlayer.setLevelSong(audioPlayer.LEVEL_END, 0.9f);
				}
				
				break;
			}
			case PLAYING:
			{
				playing.update();
				
				if (!Playing.paused)
				{
					if (Playing.situation == 0)
						audioPlayer.setLevelSong(audioPlayer.LEVEL_ONE, 0.85f);
					else if (Playing.situation == 1)
						audioPlayer.setLevelSong(audioPlayer.LEVEL_TWO, 0.8f);
					else if (Playing.situation == 10)
						audioPlayer.setLevelSong(audioPlayer.LEVEL_END);
				}
				else
				{
					audioPlayer.subSongVolume(0.65f);
				}
				
				
				
				break;
			}
			case CUTSCENE:
			{
				cutscene.update();
				audioPlayer.setLevelSong(audioPlayer.CUTSCENE);
				
				CheckPoint.restartAllCheckpoints();
				break;
			}
			case END:
			{
				audioPlayer.setLevelSong(audioPlayer.LEVEL_END, 0.9f);
				
				end.update();
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
		case END:
		{
			end.draw(g);
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
				//System.out.println("FPS: " + frames + " | UPS: " + updates);
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
	
	public CutScene getCutscene() 
	{
		return cutscene;
	}
	
	public End getEnd() 
	{
		return end;
	}
	
	public AudioPlayer getAudioPlayer()
	{
		return audioPlayer;
	}
}