package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.MenuButtons;
import utilz.LoadSave;



public class Menu extends State implements Statemethods
{
	private MenuButtons[] buttons = new MenuButtons[3];
	private BufferedImage background;
	
	private LevelManager levelManager;
	private Player player;
	
	public Menu(Game game) 
	{
		super(game);
		initClasses();
		
		background = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		loadButtons();
	}

	private void loadButtons()
	{
		//Posicao dos botoes na tela de menu
		
		//Novo Jogo
		buttons[0] = new MenuButtons((int) (32 * Game.SCALE), (int) (112 * Game.SCALE), 0, Gamestate.CUTSCENE);
		//Continuar
		buttons[1] = new MenuButtons((int) (112 * Game.SCALE), (int) (112 * Game.SCALE), 1, Gamestate.PLAYING);
		//Sair do Jogo
		buttons[2] = new MenuButtons((int) (192 * Game.SCALE), (int) (112 * Game.SCALE), 3, Gamestate.QUIT);
	}

	private void initClasses() 
	{
		levelManager = new LevelManager(game, LoadSave.MENU_DATA);
		player = new Player(Game.GAME_WIDTH/2 , 100 * Game.SCALE, (int) (18 * Game.SCALE), (int) (18 * Game.SCALE));
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
	}

	public Player getPlayer() 
	{
		return player;
	}

	@Override
	public void update() 
	{
		levelManager.update();
		player.update();
		
		for(MenuButtons mb : buttons)
		{
			mb.update();
		

		}
	}

	@Override
	public void draw(Graphics g) 
	{
		g.drawImage(background, 0, 0, (int)(background.getWidth() * Game.SCALE), (int)(background.getHeight() * Game.SCALE),null);
		levelManager.draw(g,0,0);
		
		for(MenuButtons mb : buttons)
			mb.draw(g);
		
		Playing.xLevelOffset=0;
		Playing.yLevelOffset=0;
		player.render(g);
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		for(MenuButtons mb : buttons)
		{
			if (isIn(e, mb))
			{
				mb.setMousePressed(true);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		for(MenuButtons mb : buttons)
		{
			if (isIn(e, mb)) 
			{
				if (mb.isMousePressed())
				{
					//Troca de tela
					mb.applyGamestate();
					Player.restartGame();	
				}
				break;
			}
		}

		resetButtons();
	}

	private void resetButtons() 
	{
		for(MenuButtons mb : buttons)
		{
			mb.resetBooleans();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		for(MenuButtons mb : buttons)
			mb.setMouseOver(false);
		
		for(MenuButtons mb : buttons)
			if (isIn(e, mb))
			{
				mb.setMouseOver(true);
				break;
			}
	}
	
	@Override
	public void mouseClicked(MouseEvent e){}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		switch (e.getKeyCode()) 
		{	
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
