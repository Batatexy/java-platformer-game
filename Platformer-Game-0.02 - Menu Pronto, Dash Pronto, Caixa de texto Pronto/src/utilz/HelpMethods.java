package utilz;

import java.awt.geom.Rectangle2D;

import entities.Player;
import main.Game;
import main.GamePanel;

public class HelpMethods {
	
	private static GamePanel gamePanel;
	
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
		if (!IsSolid(x, y, levelData))
			if (!IsSolid(x + width, y + height, levelData))
				if (!IsSolid(x + width, y, levelData))
					if (!IsSolid(x, y + height, levelData))
						return true;
		return false;
	}
	
	
	
	private static boolean IsSolid(float x, float y, int[][] levelData) 
	{
		int maxWidth = levelData[0].length * Game.TILES_SIZE;
		
		if (x < 0 || x >= Game.GAME_WIDTH)
			return true;
		
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;

		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		int value = levelData[(int) yIndex][(int) xIndex];

		//Todos os tiles são considerados solidos, menos o primeiro tile, que representa o vazio
		if (value != 0)
			return true;
		
		return false;
	}
	
	
	
	//Tentar usar este metodo pra cirar walljump
	public static float GetEntityXPositionNextToWall(Rectangle2D.Float hitbox, float xSpeed) 
	{
		//A velocidade deve ser maior ou menor que zero, se for zero, a entidade esta parado
		//Positivo para a direita e negativo para a esquerda
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		
		//Se o if cair aqui ou no else, significa que tocou na parede, ent daria pra resetar o walljump?
		if (xSpeed > 0)
		{
			//Parede da Direita
			int tileXPos = currentTile * Game.TILES_SIZE;
			float xOffset = (int) (Game.TILES_SIZE - hitbox.width);

			return tileXPos + xOffset - 1;
		} 
		else
		{
			//Parede da Esquerda
			return currentTile * Game.TILES_SIZE;
		}
	}
	
	
	
	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) 
	{
		//A velocidade deve ser maior ou menor que zero, se for zero, a entidade esta parada
		//Positivo para cima e negativo para baixo
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		//Se o if cair aqui ou no else, significa que ele tocou no chao ou no teto
		if (airSpeed > 0) 
		{
			// Caindo / No chao
			int tileYPosition = currentTile *  Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE  - hitbox.height);
			
			return tileYPosition + yOffset - 1;
		}
		else
		{
			// Pulando
			return currentTile * Game.TILES_SIZE;
		}
	}
	
	
	
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		//Verificar o pixel de baixo esquerdo e direito da entidade
			if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
				if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}
}