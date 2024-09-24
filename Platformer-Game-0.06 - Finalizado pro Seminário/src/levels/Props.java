package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;

public class Props 
{
	public static void draw(Graphics g, BufferedImage image, int x, int y, int xLevelOffset, int yLevelOffset)
	{
		//Encotrar a largura e altura
		int width = image.getWidth();
		int height = image.getHeight();
		
		//Sistema de renderizar só quando estiver proximo da tela
		int rangeX = xLevelOffset/Game.TILES_SIZE;
		int rangeY = yLevelOffset/Game.TILES_SIZE;
		
		int xs = x/Game.TILES_SIZE;
		int ys = y/Game.TILES_SIZE;
		
		int xw = width/Game.TILES_SIZE;
		int yw = width/Game.TILES_SIZE;

		//Sistema de carregar apenas o que está dentro da tela
		//Mude debug para 1 ou maior para visualizar em jogo
		if (((xs+xw) > ((rangeX - 8 + Playing.debugPropsDraw)/Game.SCALE))         
		   && ((xs) < ((rangeX + 20 - Playing.debugPropsDraw)/Game.SCALE)))
			g.drawImage(image, (int)((x * Game.SCALE) - xLevelOffset), 
						   (int)(((y - height + 16) * Game.SCALE) - yLevelOffset), 
						   (int)(width * Game.SCALE), (int)(height * Game.SCALE), null);
	}
	
	public static void update() {}
}