package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class Props 
{
	public static void update() 
	{
		
	}
	
	public static void draw(Graphics g, BufferedImage image, int x, int y, int xLevelOffset, int yLevelOffset)
	{
		int width = image.getWidth();
		int height = image.getHeight();

		int rangeX = xLevelOffset/Game.TILES_SIZE;
		int rangeY = yLevelOffset/Game.TILES_SIZE;
		int xs=x/16;
		int ys=y/16;

		//Sistema de carregar apenas o que está dentro da tela
		//Mude debug para 1 ou maior para visualizar em jogo
		int debug = 0;
		if (rangeX-2+debug < ((x+width) /16) && rangeX+20-debug > (x/16))
				g.drawImage(image, (int)((x * Game.SCALE) - xLevelOffset), 
						   (int)(((y - height + 16) * Game.SCALE) - yLevelOffset), 
						   (int)(width * Game.SCALE), (int)(height * Game.SCALE), null);
	}
}