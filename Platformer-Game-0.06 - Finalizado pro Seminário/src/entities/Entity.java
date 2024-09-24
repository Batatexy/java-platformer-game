package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import gamestates.Playing;
import main.Game;

public abstract class Entity
{
	protected float x, y;
	protected int width, height;
	
	//Rectangle para a hitbox de entidades, Player, sacos de lixo coletaveis ou espinhos
	protected Rectangle.Float hitbox;
	
	public Entity(float x, float y, int width, int height) 
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	//Ver as hitboxes em funcionamento
	protected void drawHitbox(Graphics g, Color color, int xLevelOffset, int yLevelOffset) 
	{
		if (Playing.debugDrawHitbox)
		{
			g.setColor(color);
			g.fillRect((int) hitbox.x - xLevelOffset, (int) hitbox.y - yLevelOffset, (int) hitbox.width, (int) hitbox.height);
		}
	}
	
	protected void initHitbox(float x, float y, int width, int height) 
	{
		hitbox = new Rectangle2D.Float(x, y, width, height);
	}
	
	public Rectangle2D.Float getHitbox() 
	{
		return hitbox;
	}
	
	public void setHitbox(Rectangle.Float hitbox) 
	{
		this.hitbox = hitbox;
	}
	
	public void setWidthandHeight(int width, int height)
	{
		this.width = (int) (width * Game.SCALE);
		this.height = (int) (height * Game.SCALE);
	}
}
