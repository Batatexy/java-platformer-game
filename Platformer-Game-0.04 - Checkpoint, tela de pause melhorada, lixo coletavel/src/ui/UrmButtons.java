package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

public class UrmButtons extends PauseButtons
{
	private BufferedImage[] images;
	private int rowIndex, index;
	private boolean mouseOver, mousePressed;
	int x;
	
	public UrmButtons(int x, int y, int width, int height, int rowIndex)
	{
		super(x,y,width,height);
		this.rowIndex = rowIndex;
		loadImages();
		
		this.x=x;
	}
	
	private void loadImages()
	{
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
		images = new BufferedImage[3];
		
		for (int i=0; i<images.length;i++)
			images[i]=temp.getSubimage(i * BUTTON_WIDTH_DEFAULT, rowIndex * BUTTON_HEIGHT_DEFAULT, BUTTON_WIDTH_DEFAULT , BUTTON_HEIGHT_DEFAULT);
		
	}
	
	public void update()
	{
		index = 0;
		
		if(mouseOver)
			index = 1;
		
		if(mousePressed)
			index = 2;
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(images[index], x, y, URM_WIDTH, URM_HEIGHT, null);	
	}
	
	public void setX(int x)
	{
		this.x=x;
	}
	
	public void resetBooleans()
	{
		mouseOver = false;
		mousePressed = false;
	}


	
	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}
}
