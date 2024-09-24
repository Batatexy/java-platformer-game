package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

public class PauseOverlay {
	
	private Playing playing;
	private BufferedImage backgroundImage;
	private int bgX, bgY, bgW, bgH;
	private UrmButtons menuB, replayB, unpauseB;
	
	public PauseOverlay(Playing playing)
	{
		this.playing = playing;
		loadBackground();
		createUrmButtons();
	}
	
	private void createUrmButtons() {
		//Posicionar os botões na mesma coluna
		int x = (int) ((Game.GAME_WIDTH/2) - (32 * Game.SCALE));
		
		//Mas em linhas diferentes
		int continueY = (int) (42 * Game.SCALE);
		int replayY = (int) (84 * Game.SCALE);
		int quitY = (int) (126 * Game.SCALE);
		
		unpauseB = new UrmButtons(x, continueY, URM_WIDTH, URM_HEIGHT, 0);
		replayB = new UrmButtons(x, replayY, URM_WIDTH, URM_HEIGHT, 1);
		menuB = new UrmButtons(x, quitY, URM_WIDTH, URM_HEIGHT, 2);
	}

	private void loadBackground() 
	{
		backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgW = (int) (backgroundImage.getWidth() * Game.SCALE);
		bgH = (int) (backgroundImage.getHeight() * Game.SCALE);
		
		bgX = (Game.GAME_WIDTH / 2) - (bgW / 2);
		bgY = (Game.GAME_HEIGHT / 2) - (bgH / 2);
	}

	public void update()
	{
		menuB.update();
		replayB.update();
		unpauseB.update();
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(backgroundImage, bgX, bgY, bgW, bgH, null);
		
		menuB.draw(g);
		replayB.draw(g);
		unpauseB.draw(g);
	}

	public void mousePressed(MouseEvent e) 
	{
		if (isIn(e, unpauseB))
			unpauseB.setMousePressed(true);
		
		else if (isIn(e, replayB))
			replayB.setMousePressed(true);
		
		else if (isIn(e, menuB))
			menuB.setMousePressed(true);
	}

	public void mouseReleased(MouseEvent e)
	{
		if (isIn(e, menuB)) 
		{
			if (menuB.isMousePressed()) 
			{
				Gamestate.state = Gamestate.MENU;
				playing.unpauseGame();
			}
		} 
		else if (isIn(e, replayB)) 
		{
			if (replayB.isMousePressed())
				System.out.println("replay lvl!");
		} 
		else if (isIn(e, unpauseB)) 
		{
			if (unpauseB.isMousePressed())
				playing.unpauseGame();
		}
		
		menuB.resetBooleans();
		replayB.resetBooleans();
		unpauseB.resetBooleans();
	}

	public void mouseMoved(MouseEvent e) 
	{
		menuB.setMouseOver(false);
		replayB.setMouseOver(false);
		unpauseB.setMouseOver(false);
		
		if (isIn(e, menuB))
			menuB.setMouseOver(true);
		
		else if (isIn(e, replayB))
			replayB.setMouseOver(true);
		
		else if (isIn(e, unpauseB))
			unpauseB.setMouseOver(true);
	}

	private boolean isIn(MouseEvent e, PauseButtons button)
	{
		return button.getBounds().contains(e.getX(), e.getY());
	}

}
