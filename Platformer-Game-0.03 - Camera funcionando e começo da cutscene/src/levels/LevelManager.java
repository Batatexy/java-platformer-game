package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class LevelManager 
{

	private Game game;
	private BufferedImage[] levelSprite;
	
	//Vai mudar depois
	private Level levelOne;

	public LevelManager(Game game, String levelData) 
	{
		this.game = game;
		//levelSprite=LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		importGlobalTileSet();
		levelOne = new Level(LoadSave.GetLevelData(levelData));
	}

	private void importGlobalTileSet() 
	{
		//Altura x Comprimento = []
		BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[260];
		
		//Mudar o tamanho de blocos possiveis
		for (int j = 0; j < 26; j++)
			for (int i = 0; i < 10; i++) 
			{
				//O 10 representa a parte horizontal do SpriteSheet
				int index = j * 10 + i;
				levelSprite[index] = image.getSubimage(i * 16, j * 16, 16, 16);
			}
	}

	public void draw(Graphics g, int xLevelOffset,  int yLevelOffset)
	{
		int rangeX = Playing.xLevelOffset/Game.TILES_SIZE;
		int rangeY = Playing.yLevelOffset/Game.TILES_SIZE;

		for (int j = rangeY; j < rangeY+12; j++)
			for (int i = rangeX; i < rangeX+19; i++)
			{
				int index = levelOne.getSpriteIndex(i, j);
				g.drawImage(levelSprite[index], 
							Game.TILES_SIZE * i - xLevelOffset, 
							Game.TILES_SIZE * j - yLevelOffset, 
							Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
	}

	public void update() 
	{

	}

	public Level getCurrentLevel() 
	{
		return levelOne;
	}

}
