package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class LevelManager 
{

	private Game game;
	private BufferedImage[] levelSprite;
	
	//Vai mudar depois
	private Level levelOne;

	public LevelManager(Game game) 
	{
		this.game = game;
		//levelSprite=LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		importGlobalTileSet();
		levelOne = new Level(LoadSave.GetLevelData());
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

	public void draw(Graphics g) 
	{
		for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
			for (int i = 0; i < Game.TILES_IN_WIDTH; i++) 
			{
				int index = levelOne.getSpriteIndex(i, j);
				g.drawImage(levelSprite[index], Game.TILES_SIZE * i, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
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
