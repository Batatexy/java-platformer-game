package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class InvisibleWall extends Entity
{
	private Player player;
	private int[][] levelData;
	
	private int black = 0, maxBlack=30;
	private float x,y;
	private int width, height;
	BufferedImage image;
	
	public InvisibleWall(BufferedImage image, float x, float y, int width, int height, Player player) 
	{
		super((int)(x * (Game.SCALE)), (int)(y * (Game.SCALE)), (int)(width * (Game.SCALE)), (int)(height * (Game.SCALE)));
		//Tamanho da hitbox
		initHitbox((x)  * Game.SCALE, (y)  * Game.SCALE,(int) ((width) * Game.SCALE), (int) ((height) * Game.SCALE));
		this.player = player;
		
		this.width=width;
		this.height=height;
		
		this.x=x;
		this.y=y;
		
		this.image=image;
	}
	
	public void update()
	{
		if (this.hitbox.intersects(player.getHitbox()))
		{
			if (black<maxBlack)
				black++;
			else
				black=maxBlack;
		}
		else
		{
			if (black>0)
				black--;
			else
				black=0;
		}
	}
	
	public void draw(Graphics g, int xLevelOffset, int yLevelOffset, Player player)
	{
		this.player = player;
		drawHitbox(g, Color.MAGENTA, xLevelOffset, yLevelOffset);
		
		g.drawImage(image, (int)((x * Game.SCALE) - xLevelOffset), (int)(((y) * Game.SCALE) - yLevelOffset), 
				   		   (int)(width * Game.SCALE), (int)(height * Game.SCALE), null);
		
		g.setColor(new Color(0,0,0,black));
		g.fillRect((int)(x* Game.SCALE)-xLevelOffset, (int)(y* Game.SCALE)-yLevelOffset, 
				   (int)(width* Game.SCALE), (int)(height* Game.SCALE));
	}
}