package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener 
{
	private GamePanel gamePanel;

	public MouseInputs(GamePanel gamePanel) 
	{
		this.gamePanel = gamePanel;
	}

	//Segurar e arrastar
	@Override
	public void mouseDragged(MouseEvent e) 
	{

	}

	//Movimentação comum do Mouse
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		switch (Gamestate.state) 
		{
		case MENU:
		{
			gamePanel.getGame().getMenu().mouseMoved(e);
			break;
		}

		case PLAYING:
			//gamePanel.getGame().getPlaying().mouseMoved(e);
			break;
		
		default:
			break;
		}
	}

	//Clique do Mouse
	@Override
	public void mouseClicked(MouseEvent e) 
	{

	}

	//Mouse Pressionado
	@Override
	public void mousePressed(MouseEvent e) 
	{
		switch(Gamestate.state)
		{
		case INTRODUCTION:
		{
			gamePanel.getGame().getIntroduction().mousePressed(e);
			break;
		}
		case MENU:
		{
			gamePanel.getGame().getMenu().mousePressed(e);
			break;
		}
//		case PLAYING:
//		{
//			gamePanel.getGame().getPlaying().mouseClicked(e);
//			break;
//		}
		default:
			break;
		}
	}
	
	//Soltar o Mouse
	@Override
	public void mouseReleased(MouseEvent e) 
	{

	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{

	}

	@Override
	public void mouseExited(MouseEvent e) 
	{

	}
}
