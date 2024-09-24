package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import gamestates.Playing;
import main.Game;

public class Spike extends Entity
{
	private Player player;
	private boolean spikeTrigger=false;
	private int spikeValue=80, blackValueOne=10, blackValueTwo=10, spikeTimer=spikeValue;
	
	private int[][] levelData;
	
	//Configurações do tamanho da Hitbox
	private float xDrawOffset = 1f * Game.SCALE;
	private float yDrawOffset = 4f * Game.SCALE;
	
	public Spike(float x, float y, int width, int height, Player player) 
	{
		super((int)(x * (Game.SCALE)), (int)(y * (Game.SCALE)), (int)(width * (Game.SCALE)), (int)(height * (Game.SCALE)));
		//Tamanho da hitbox do espinho
		initHitbox((x+3)  * Game.SCALE, (y+10)  * Game.SCALE,(int) (((width*15) + 4) * Game.SCALE), (int) ((height*6) * Game.SCALE));
		this.player = player;
	}

	public void draw(Graphics g, int xLevelOffset, int yLevelOffset, Player player)
	{
		this.player = player;
		drawHitbox(g, Color.RED);
	}
	
	public void update()
	{
		checkSpikeTouched(player.getHitbox());
			if (spikeTrigger)
			{
				spikeTimer--;
				
				if (spikeTimer>spikeValue/2)
					Playing.increaseBlackSreen(blackValueOne);
				else if (spikeTimer==spikeValue/2)
				{
					Game.deathCount++;
					Player.restartGame();
				}
				else
					Playing.decreaseBlackSreen(blackValueTwo);

				if (spikeTimer<=0)
				{
					spikeTimer=spikeValue;
					spikeTrigger=false;
					Playing.decreaseBlackSreen(255);
				}
			}
		
	}

	public void checkSpikeTouched(Rectangle2D.Float hitbox)
	{
		if (this.hitbox.intersects(hitbox))
		{
			spikeTrigger=true;
			Playing.active=false;
		}
	}
	
	
}