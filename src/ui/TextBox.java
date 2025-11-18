package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class TextBox 
{
	private static int tick = 0, index = 0;
	private static int width = 582, height = 156;
	private static BufferedImage imageHeight;
	private static BufferedImage[] image;
	private static int frames;
	private static int black = 255;
	public static boolean place;
	
	public static void loadBoxImages(String local)
	{
		if (Game.potatoMode)
		{
			imageHeight = LoadSave.GetSpriteAtlas(local);
			int yFrame = imageHeight.getHeight()-height;
			
			image = null;
			BufferedImage temp = imageHeight;
			image = new BufferedImage[1];

			for (int i=0; i< image.length; i++)
				image[i] = temp.getSubimage(0,yFrame , width, height);
		}
		else
		{
			imageHeight = LoadSave.GetSpriteAtlas(local);
			frames = imageHeight.getHeight()/height;
			
			image = null;
			BufferedImage temp = imageHeight;
			image = new BufferedImage[frames];
			
			for (int i=0; i< image.length; i++)
				image[i] = temp.getSubimage(0,i * height , width, height);
		}
	}
	
	public static void update() 
	{
		if(!Playing.paused)
			tick++;
		
		if (index<(frames)-1)
		{
			if (tick>6)
			{
				index+=1;
				tick=0;
			}
		}
		else
		{
			if (tick>Game.ups*9999)
			{
				index+=1;
				tick=0;
			}
		}
	}
	
	public static void resetTick()
	{
		tick =  0;
		index = 0;
	}

	public static void draw(Graphics g)
	{
		if (place==true)// Em cima
		{
			g.drawImage(image[index], (int)(Game.GAME_WIDTH / 2) - (int)((width / 8 )* Game.SCALE), (int)(5 * Game.SCALE), 
									  (int)(width * Game.SCALE/4), (int)(height * Game.SCALE/4), null);
		}
		else // Em baixo
		{
			g.drawImage(image[index], (int)(Game.GAME_WIDTH / 2) - (int)((width / 8 )* Game.SCALE), (int)(133 * Game.SCALE), 
					  (int)(width * Game.SCALE/4), (int)(height * Game.SCALE/4), null);
		}
	}
}