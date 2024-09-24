package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;

public class LoadSave {

	//public static final String ="/.png";
	public static final String ICON_ATLAS="/Icon.png";
	public static final String MENU_BUTTONS = "/MenuButtonAtlas.png";
	
	public static final String LEVEL_ATLAS = "/GlobalTileSet.png";
	public static final String PLAYER_ATLAS = "/GariSpriteSheet.png";
	
	public static final String MENU_DATA = "/MenuData.png";
	public static final String LEVEL_ONE_DATA = "/LevelOneData.png";
	
	//Enviamos uma string com o lugar da imagem que queremos puxar
	public static BufferedImage GetSpriteAtlas(String fileName) {
		
		//Se falhar o carregamento na imagem, ele retorna uma imagem nula
		BufferedImage image = null;
		
		//importar a imagem, onde o local já está armazenada na variavel enviada, fileName
		InputStream is = LoadSave.class.getResourceAsStream(fileName);
		
		//Precisa-se de um try, se por acaso a imagem nao existir, o terminal nos avisa o que esta faltando
		try 
		{
			image = ImageIO.read(is);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				is.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return image;
	}
	
	
	
	public static int[][] GetLevelData() 
	{
		int[][] levelData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
		BufferedImage image = GetSpriteAtlas(MENU_DATA);

		for (int j = 0; j < image.getHeight(); j++)
			for (int i = 0; i < image.getWidth(); i++) 
			{
				Color color = new Color(image.getRGB(i, j));
				int value = color.getRed();
				//Maximo de 256 objetos, ja que no espectro de cor, se vai do 0 ao 255
				if (value >= 255)
				{
					value = 0;
				}
				levelData[j][i] = value;
			}
		return levelData;
	}
}
