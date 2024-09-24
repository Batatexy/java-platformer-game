package levels;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;

public class Props 
{
	private static int width, height;
	private static BufferedImage image;

	public static void loadPropImage(String local)
	{
		image = LoadSave.GetSpriteAtlas(local);
		width = image.getWidth();
		height = image.getHeight();
	}
	
	public static void update() 
	{
		
	}
	
	public static void draw(Graphics g, int x, int y)
	{
		//g.drawImage(image, x, y, null);
		g.drawImage(image, x, y, width, height, null);
	}
}


