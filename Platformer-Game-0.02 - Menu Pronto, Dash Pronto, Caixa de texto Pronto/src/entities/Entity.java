package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public abstract class Entity
{
	
	protected float x, y;
	protected int width, height;
	
	//Rectangle pra hitbox do personagem
	protected Rectangle.Float hitbox;

	public Entity(float x, float y, int width, int height) 
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

	}

	//Ver as hitboxes em funcionamento
	protected void drawHitbox(Graphics g) 
	{
		//Visualizar a hitbox
		g.setColor(Color.GREEN);
		g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
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

}
