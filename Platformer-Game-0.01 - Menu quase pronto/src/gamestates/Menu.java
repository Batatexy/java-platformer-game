package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.MenuButton;

public class Menu extends State implements Statemethods
{
	private MenuButton[] buttons = new MenuButton[4];
	
	private LevelManager levelManager;
	private Player player;
	
	//private int buttonDelayValue = 100;
	//private int buttonDelay = buttonDelayValue;
	//private boolean buttonTrigger = false;
	
	public Menu(Game game) 
	{
		super(game);
		initClasses();
		loadButtons();
	}

	private void loadButtons()
	{
		//Posicao dos botoes na tela de menu
		
		//Novo Jogo
		buttons[0] = new MenuButton((int) (128 * Game.SCALE), (int) (32 * Game.SCALE), 0, Gamestate.PLAYING, true);
		//Continuar
		buttons[1] = new MenuButton((int) (224 * Game.SCALE), (int) (32 * Game.SCALE), 1, Gamestate.PLAYING, true);
		//Opcoes
		buttons[2] = new MenuButton((int) (48 * Game.SCALE), (int) (32 * Game.SCALE), 2, Gamestate.OPTIONS, false);
		//Sair
		buttons[3] = new MenuButton((int) (320 * Game.SCALE), (int) (32 * Game.SCALE), 3, Gamestate.QUIT, false);
	}
	
	
	
	private void initClasses() 
	{
		levelManager = new LevelManager(game);
		player = new Player(Game.GAME_WIDTH/2 , -10 * Game.SCALE, (int) (18 * Game.SCALE), (int) (18 * Game.SCALE));
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());

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
	public void update() 
	{
		levelManager.update();
		player.update();
		
		for(MenuButton mb : buttons)
			mb.update();
		
		
		//tentativa de fazer um pequeno delay no botao
//		if (buttonTrigger == true)
//		{
//			buttonDelay--;
//			
//			if (buttonDelay <= 0)
//				System.out.println("New Game");
//				for(MenuButton mb : buttons)
//					buttonTrigger=false;
//		}
	}

	@Override
	public void draw(Graphics g) 
	{
		levelManager.draw(g);
		player.render(g);
		
		for(MenuButton mb : buttons)
			mb.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		for(MenuButton mb : buttons)
		{
			if (isIn(e, mb))
			{
				mb.setMousePressed(true);
				
				//O meu funcionou colocando aqui, mas nao saber se dara problema
				//mas ele inicia de forma instantanea, portanto utilizei de gambiarra para fazer um efeito legal
				//buttonTrigger = true; //nao deu certo
				mb.setTrigger(true);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		for(MenuButton mb : buttons)
		{
			if (isIn(e, mb))
				if(mb.isMousePressed())
				{
					//Deveria estar aqui, mas estando aqui nao acontecia nada ent n sei
					//mb.applyGamestate();
					
				
				}
			break;
		}
		resetButtons();
	}

	private void resetButtons() 
	{
		for(MenuButton mb : buttons)
		{
			mb.resetBooleans();
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		for(MenuButton mb : buttons)
			mb.setMouseOver(false);
		
		for(MenuButton mb : buttons)
			if (isIn(e, mb))
			{
				mb.setMouseOver(true);
				break;
			}
	}
	
	
	

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
