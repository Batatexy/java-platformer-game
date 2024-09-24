package entities;

import gamestates.Playing;
import main.Game;

public class TriggerSkyChange
{
	private Player player;
	
	//Caverna de terra:
//	private int brownSkyRed = 143;
//	private int brownSkyGreen = 88;
//	private int brownSkyBlue = 61;
//	private int brownSkyAlpha = 220;
	
	private int brownSkyRed = 140;
	private int brownSkyGreen = 90;
	private int brownSkyBlue = 60;
	private int brownSkyAlpha = 220;
	
	
	
	
	
	private int graySkyRed = 145;
	private int graySkyGreen = 151;
	private int graySkyBlue = 176;
	private int graySkyAlpha = 220;
	
	public static boolean resetAllTick=false;
	
	public TriggerSkyChange(Player player)
	{
		this.player=player;
	}
	
	public void update()
	{			
		if (resetAllTick)
		{
			Playing.red=Playing.brightSkyRed;
			Playing.green=Playing.brightSkyGreen;
			Playing.blue =Playing.brightSkyBlue;
			Playing.alpha=Playing.brightSkyAlpha;
			
			resetAllTick=false;
		}
		
		if (player.getTempX() >= 2096 && (player.getTempX() <= 2352))
		{
			if(player.getTempY() >= 2224)
			{
				Playing.red = changeColor(Playing.red, brownSkyRed,1);
				Playing.green = changeColor(Playing.green, brownSkyGreen,1);
				Playing.blue = changeColor(Playing.blue, brownSkyBlue,2);
				Playing.alpha = changeColor(Playing.alpha, brownSkyAlpha,1);
			}
		}
		else if(player.getTempX() >= 2750 && (player.getTempX() <= 4258))
		{
			Playing.red = changeColor(Playing.red, graySkyRed,1);
			Playing.green = changeColor(Playing.green, graySkyGreen,1);
			Playing.blue = changeColor(Playing.blue, graySkyBlue,1);
			Playing.alpha = changeColor(Playing.alpha, graySkyAlpha,1);
		}
		else
		{
			Playing.red = changeColor(Playing.red, Playing.brightSkyRed,1);
			Playing.green = changeColor(Playing.green, Playing.brightSkyGreen,1);
			Playing.blue = changeColor(Playing.blue, Playing.brightSkyBlue,1);
			Playing.alpha = changeColor(Playing.alpha, Playing.brightSkyAlpha,1);
		}
	}
	
	public int changeColor(int color, int value, int add)
	{
		if (color > value)
		{
			color -= add;
		}
		else if (color < value)
		{
			color += add;
		}
		
		return color;
	}
	
	public static void resetAllTick()
	{
		resetAllTick=true;
	}
}