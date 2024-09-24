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
		int rangeX = 0;
		int rangeY = 0;
	
		rangeX = xLevelOffset/Game.TILES_SIZE;
		rangeY = yLevelOffset/Game.TILES_SIZE;
		

		forTiles(g,rangeX, rangeY, xLevelOffset, yLevelOffset);
	}

	private void forTiles(Graphics g, int rangeX, int rangeY, int xLevelOffset, int yLevelOffset) 
	{
		//Sistema de carregar apenas o que está dentro da tela
		//Mude debug para 1 ou maior para visualizar em jogo
		int debug = 0;
		for (int j = rangeY+debug; j < rangeY+12-debug; j++)
			for (int i = rangeX+debug; i < rangeX+19-debug; i++)
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
