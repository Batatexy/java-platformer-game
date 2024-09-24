package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class TextBox {
	private static int tick = 0, index = 0;
	private static int width = 582, height = 156;
	private static BufferedImage imageWidth;
	private static int frames;
	private static boolean trigger=false;
	
	public static BufferedImage[] loadBoxImages(String local)
	{
		widthLimit(local);
		
		BufferedImage[] image = null;
		
		BufferedImage temp = imageWidth;
		image = new BufferedImage[frames];
		
		
		for (int i=0; i< image.length; i++)
			image[i] = temp.getSubimage(i * width,0 , width, height);
		return image;
	}
	
	public static void widthLimit(String local)
	{
		imageWidth = LoadSave.GetSpriteAtlas(local);
		frames = imageWidth.getWidth()/width;
	}
	
	public static void update() 
	{
		tick++;
		
		if (index<(frames)-6)
		{
			if (tick>6)
			{
				index+=1;
				tick=0;
			}
		}
		else
		{
			if (tick>Game.FPS_SET*100)
			{
				index+=1;
				tick=0;
			}
			//Quando terminar a animação, dai destrava alguma variavel
			
			
		}
	}
	
	public static void resetTick()
	{
		//if (trigger=true)
		tick =  0;
		index = 0;
	}

	public static void draw(Graphics g, BufferedImage[] image)
	{
		//TextBox.resetTick();
		//int lastNumber=1;
		//int actualNumber=2;
		//Se ele mais 1, for igual ao actualNumber, não reseta, caso contrario, reseta e sobe 1
		g.drawImage(image[index], Game.GAME_WIDTH/2*0, (int)(120*Game.SCALE), width, height, null);
	}
}


