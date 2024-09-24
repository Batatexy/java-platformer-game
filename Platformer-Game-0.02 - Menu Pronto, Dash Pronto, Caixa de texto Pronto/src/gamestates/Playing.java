package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.PauseOverlay;

public class Playing extends State implements Statemethods
{
	private LevelManager levelManager;
	private Player player;
	
	private PauseOverlay pauseOverlay;
	private boolean paused = false;
	
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
		levelManager = new LevelManager(game);
		player = new Player(Game.GAME_WIDTH/2 , 190 * Game.SCALE, (int)(18 * Game.SCALE), (int)(18 * Game.SCALE));
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
		}
		//Se estiver, então nada acontece, o jogo está pausado
		else
		{
			pauseOverlay.update();
		}
	} 
	
	@Override
	public void draw(Graphics g) {
		levelManager.draw(g);
		player.render(g);
		
		if(paused)
			pauseOverlay.draw(g);
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
