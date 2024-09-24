package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import entities.Player;
import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.UI.MenuButtons.*;

public class MenuButtons 
{
	private int xPosition, yPosition, rowIndex, index;
	private Gamestate state;
	private BufferedImage[] images;
	private Rectangle bounds;
	
	private boolean mouseOver, mousePressed;
	
	public MenuButtons(int xPosition, int yPosition, int rowIndex, Gamestate state)
	{
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.state = state;
		
		if (state == Gamestate.PLAYING && Playing.newGame)
			this.rowIndex = rowIndex=2;
		else
			this.rowIndex = rowIndex;

		loadImages();
		initBounds();
	}
	
	private void initBounds() 
	{
		bounds = new Rectangle(xPosition, yPosition, (int)(BUTTON_WIDTH - (16 * Game.SCALE)), BUTTON_HEIGHT);
	}
	
	private void loadImages() 
	{
		images = new BufferedImage[3];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
		
		for (int i=0; i< images.length; i++)
			images[i] = temp.getSubimage(i * BUTTON_WIDTH_DEFAULT, rowIndex * BUTTON_HEIGHT_DEFAULT, BUTTON_WIDTH_DEFAULT, BUTTON_HEIGHT_DEFAULT);
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(images[index], xPosition, yPosition, BUTTON_WIDTH, BUTTON_HEIGHT, null);
	}
	
	public void update()
	{
		index = 0;
		
		if (mouseOver)
		{
			index = 1;
		}
		if (mousePressed)
		{
			index = 2;
		}
		
		if (state == Gamestate.PLAYING && !Playing.newGame && rowIndex==2)
		{
			rowIndex=1;
			loadImages();
		}
	}
	
	public void applyGamestate() 
	{
		switch(state)
		{
			case PLAYING:
			{
				if (!Playing.newGame)
				{
					Gamestate.state = state;
					Player.restartGame();	
				}
				break;
			}
			case CUTSCENE:
			{
				Gamestate.state = state;
				break;
			}
			case QUIT:
			{
				Gamestate.state = state;
				break;
			}
		}
	}
	
	public void resetBooleans()
	{
		mouseOver=false;
		mousePressed=false;
	}
	
	public boolean isMouseOver() 
	{
		return mouseOver;
	}
	
	public void setMouseOver(boolean mouseOver) 
	{
		this.mouseOver = mouseOver;
	}
	
	public boolean isMousePressed() 
	{
		return mousePressed;
	}
	
	public void setMousePressed(boolean mousePressed) 
	{
		this.mousePressed = mousePressed;
	}
	
	public Rectangle getBounds() 
	{
		return bounds;
	}
	
	public void setBounds(Rectangle bounds) 
	{
		this.bounds = bounds;
	}
}