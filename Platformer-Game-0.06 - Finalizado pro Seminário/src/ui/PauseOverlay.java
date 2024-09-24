package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import entities.Player;
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
	
	public static boolean pauseTrigger=false;
	private int black=0, pauseValue = 100, pauseTimer=pauseValue, changeButtonsX, changeBackgroundX=0;
	
	public PauseOverlay(Playing playing)
	{
		this.playing = playing;
		loadBackground();
		createUrmButtons();
	}
	
	private void loadBackground() 
	{
		backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgW = (int) (backgroundImage.getWidth() * Game.SCALE);
		bgH = (int) (backgroundImage.getHeight() * Game.SCALE);
		
		bgX = -470;
		bgY = 0;
	}
	
	private void createUrmButtons() 
	{
		//Posicionar os botões na mesma coluna
		int x = (int)(((Game.GAME_WIDTH/5) -370) - (32 * Game.SCALE));
		changeButtonsX=x;
		
		//Mas em linhas diferentes
		int continueY = (int) (42 * Game.SCALE);
		int replayY = (int) (84 * Game.SCALE);
		int quitY = (int) (126 * Game.SCALE);
		
		unpauseB = new UrmButtons(x, continueY, URM_WIDTH, URM_HEIGHT, 0);
		replayB = new UrmButtons(x, replayY, URM_WIDTH, URM_HEIGHT, 1);
		menuB = new UrmButtons(x, quitY, URM_WIDTH, URM_HEIGHT, 2);
	}

	public void update()
	{
		menuB.update();
		replayB.update();
		unpauseB.update();
		
		animation();
	}
	
	private void animation() {
		int buttonSpeed=18, bgSpeed=21, blackSpeed=7;
		
		if (pauseTrigger)
		{
			pauseTimer--;
			if (black<=149)
					black+=blackSpeed;
			else
				black=150;
			
			if (changeButtonsX <= (int) (((Game.GAME_WIDTH/5)) - (32 * Game.SCALE)))
				changeButtonsX+=buttonSpeed;

			if (bgX <= -bgSpeed)
				bgX+=bgSpeed;
			else
				bgX=0;

		}
		else
		{
			if (changeButtonsX >= (int) (((Game.GAME_WIDTH/5) -372) - (32 * Game.SCALE)))
				changeButtonsX-=buttonSpeed;
			
			if (bgX >= -470)
				bgX-=bgSpeed;
			
			if (black>=blackSpeed)
				black-=blackSpeed;
			else
				black=0;
		}
	}

	public void draw(Graphics g)
	{
		g.setColor(new Color(0,0,0,black));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		
		g.drawImage(backgroundImage, bgX, bgY, bgW, bgH, null);

		menuB.draw(g);
		replayB.draw(g);
		unpauseB.draw(g);

		menuB.setX(changeButtonsX);
		replayB.setX(changeButtonsX);
		unpauseB.setX(changeButtonsX);
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
				changeButtonsX=(int) (((Game.GAME_WIDTH/5) -372) - (32 * Game.SCALE));
				black=0;
				bgX =-470;
				playing.unpauseGame();
				pauseTrigger=false;
				TextBox.loadBoxImages("/TextBoxNull.png");
				TextBox.resetTick();
				Game.slowMotion=Game.UPS_SET;
				Gamestate.state = Gamestate.MENU;
			}
		} 
		else if (isIn(e, replayB)) 
		{
			if (replayB.isMousePressed())
			{
				Player.restartGame();
				playing.unpauseGame();
				TextBox.loadBoxImages("/TextBoxNull.png");
				TextBox.resetTick();
				Game.slowMotion=Game.UPS_SET;
				pauseTrigger=false;
			}
		} 
		else if (isIn(e, unpauseB)) 
		{
			if (unpauseB.isMousePressed())
				playing.unpauseGame();
				pauseTrigger=false;
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
