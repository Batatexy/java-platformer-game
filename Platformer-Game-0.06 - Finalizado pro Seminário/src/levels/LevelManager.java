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
	
	private Level levelOne;
	
	public LevelManager(Game game, String levelData) 
	{
		this.game = game;
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
		int rangeX = 0;
		int rangeY = 0;
		
		rangeX = xLevelOffset/Game.TILES_SIZE;
		rangeY = yLevelOffset/Game.TILES_SIZE;
		
		forTiles(g,rangeX, rangeY, xLevelOffset, yLevelOffset);
	}
	
	private void forTiles(Graphics g, int rangeX, int rangeY, int xLevelOffset, int yLevelOffset) 
	{
		for (int j = rangeY+Playing.debugLevelDraw; j < rangeY+12-Playing.debugLevelDraw; j++)
			for (int i = rangeX+Playing.debugLevelDraw; i < rangeX+19-Playing.debugLevelDraw; i++)
			{
				int index = levelOne.getSpriteIndex(i, j);
				g.drawImage(levelSprite[index], 
							Game.TILES_SIZE * i - xLevelOffset, 
							Game.TILES_SIZE * j - yLevelOffset, 
							Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
	}
	
	public void update() {}
	
	public Level getCurrentLevel() 
	{
		return levelOne;
	}
}
