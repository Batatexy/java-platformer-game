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
	
	//Rectangle pra hitbox de entidades, Player ou sacos de lixo coletaveis
	protected Rectangle.Float hitbox;

	public Entity(float x, float y, int width, int height) 
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	//Ver as hitboxes em funcionamento
	protected void drawHitbox(Graphics g, Color color) 
	{
		//Visualizar a hitbox
		boolean debug=false;
		
		if (debug)
		{
			g.setColor(color);
			g.fillRect((int) hitbox.x - Playing.xLevelOffset, (int) hitbox.y - Playing.yLevelOffset, (int) hitbox.width, (int) hitbox.height);
		}
	}

	protected void initHitbox(float x, float y, int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, width, height);
	}

//	protected void updateHitbox() {
//		hitbox.x = (int) x;
//		hitbox.y = (int) y;
//	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
	
	public void setHitbox(Rectangle.Float hitbox) {
		this.hitbox = hitbox;
	}

	public void setWidthandHeight(int width, int height)
	{
		this.width = (int) (width * Game.SCALE);
		this.height = (int) (height * Game.SCALE);
	}
	
	

}
