package entities;

import gamestates.Playing;
import main.Game;

public class TriggerSkyChange
{
	private Player player;
	
	//Caverna de terra:
	private int brownSkyRed = 143;
	private int brownSkyGreen = 88;
	private int brownSkyBlue = 61;
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
				Playing.red = changeColor(Playing.red, brownSkyRed);
				Playing.green = changeColor(Playing.green, brownSkyGreen);
				Playing.blue = changeColor(Playing.blue, brownSkyBlue);
				Playing.alpha = changeColor(Playing.alpha, brownSkyAlpha);
			}

		}
		else if(player.getTempX() >= 2750 && (player.getTempX() <= 4258))
		{
			Playing.red = changeColor(Playing.red, graySkyRed);
			Playing.green = changeColor(Playing.green, graySkyGreen);
			Playing.blue = changeColor(Playing.blue, graySkyBlue);
			Playing.alpha = changeColor(Playing.alpha, graySkyAlpha);
		}
		else
		{
			Playing.red = changeColor(Playing.red, Playing.brightSkyRed);
			Playing.green = changeColor(Playing.green, Playing.brightSkyGreen);
			Playing.blue = changeColor(Playing.blue, Playing.brightSkyBlue);
			Playing.alpha = changeColor(Playing.alpha, Playing.brightSkyAlpha);
		}
	}
	
	public int changeColor(int color, int value)
	{
		if (color > value)
		{
			color--;
		}
		else if (color < value)
		{
			color++;
		}
		else
		{
			color = value;
		}
		
		return color;
	}
	
	public static void resetAllTick()
	{
		resetAllTick=true;
	}
}