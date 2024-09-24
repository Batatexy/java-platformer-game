package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.Game;

public class Introduction extends State implements Statemethods 
{

	public Introduction(Game game) 
	{
		super(game);
	}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_SPACE);
			//Mudar para outras fases
			Gamestate.state = Gamestate.MENU;
	}
	
	@Override
	public void update() {}
	
	@Override
	public void draw(Graphics g) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
}