package levels;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Props 
{
	public static void update() 
	{
		
	}
	
	public static void draw(Graphics g, BufferedImage image, int x, int y, int xLevelOffset, int yLevelOffset)
	{
		int width = image.getWidth();
		int height = image.getHeight();

		//g.drawImage(image, x, y, null);
		g.drawImage(image, (int)((x * Game.SCALE) - xLevelOffset), 
						   (int)((y * Game.SCALE) - yLevelOffset), 
						   (int)(width * Game.SCALE), (int)(height * Game.SCALE), null);
	}
}


