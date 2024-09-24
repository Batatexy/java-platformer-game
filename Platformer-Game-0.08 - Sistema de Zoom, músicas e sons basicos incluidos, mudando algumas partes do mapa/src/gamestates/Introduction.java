package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.Game;

public class Introduction extends State implements Statemethods 
{
	private int black = 255;
	private int timer = 800;

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
	public void update() 
	{
		Gamestate.state = Gamestate.MENU;
		
		if (black>0)
		{
			black--;
		}
		else
		{
			timer--;
		}
		
		if (timer<0)
		{
			Gamestate.state = Gamestate.MENU;
		}
	}
	
	@Override
	public void draw(Graphics g) 
	{
		g.setColor(new Color(0,0,0,black));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
	}
	
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