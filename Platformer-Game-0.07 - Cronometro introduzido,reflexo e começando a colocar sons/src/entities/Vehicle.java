package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import levels.Props;
import main.Game;
import utilz.LoadSave;

public class Vehicle extends Entity
{
	//Matriz de localizacao dos sprites
	private BufferedImage[][] index;
	BufferedImage garbageTruck = LoadSave.GetSpriteAtlas("/GarbageTruck.png");
	
	private int horizontalIndex, verticalIndex;
	private float xSpeed = 0;
	private boolean car;
	
	public Vehicle(boolean carValue, int x, int y) 
	{
		super(x, y, 70, 30);
		loadAnimations(carValue);
		//Tamanho da hitbox do veiculo
		initHitbox(x, y,(int) (30 * Game.SCALE), (int) (70 * Game.SCALE));
	}
	
	private void loadAnimations(boolean carValue) 
	{
		car = carValue;
		//hitbox.y=480;
		BufferedImage image = LoadSave.GetSpriteAtlas("/AllVehicles.png");
		
		int chance = getRandomNumber(0,10);
		if (chance>6)
			verticalIndex = getRandomNumber(3,5);
		else
			verticalIndex = getRandomNumber(0,3);

		horizontalIndex = getRandomNumber(0,10);
		
		//Matriz dos sprites
		index = new BufferedImage[5][10];
		for (int j = 0; j < index.length; j++)
			for (int i = 0; i < index[j].length; i++)
				index[j][i] = image.getSubimage(i * 70, j * 30, 70, 30);
	}
	
	public void update() 
	{
		hitbox.x += xSpeed;
	}
	
	public int getRandomNumber(int min, int max) 
	{
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	public void draw(Graphics g, int xLevelOffset, int yLevelOffset)
	{
		if (car)
		{
			Props.draw(g, garbageTruck, (int)(hitbox.x), (int)(hitbox.y), xLevelOffset, yLevelOffset);
		}
		else
			g.drawImage(index[verticalIndex][horizontalIndex],
					(int) (((hitbox.x * Game.SCALE)) + xSpeed),
					(int) (hitbox.y * Game.SCALE) - yLevelOffset,
					(int)(width * Game.SCALE), (int)(height * Game.SCALE), null);
	}
	
	public void setXSpeed(float xSpeed) 
	{
		this.xSpeed = xSpeed;
	}
}