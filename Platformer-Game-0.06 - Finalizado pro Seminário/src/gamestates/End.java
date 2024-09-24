package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import entities.MiniPlayer;
import entities.Player;
import entities.Trash;
import entities.Vehicle;
import levels.LevelManager;
import levels.Props;
import main.Game;
import ui.TextBox;
import utilz.LoadSave;

public class End extends State implements Statemethods
{
	private LevelManager levelManager;
	private MiniPlayer miniplayer;
	private Player player;
	private Trash objTrashOne, objTrashTwo, objTrashTree, objTrashFour, objTrashFive;
	private Vehicle garbagetruck, othervehiclesOne, othervehiclesTwo;
	
	private BufferedImage 
	skull = LoadSave.GetSpriteAtlas("/Skull.png"),
	replay = LoadSave.GetSpriteAtlas("/Replay.png"),
	endgame = LoadSave.GetSpriteAtlas("/EndGame.png");
	
	private boolean space = false;
	private int skipPercentageValue=150;
	private int skipPercentage=skipPercentageValue;
	private int skipValue=1;
	
	private int events=0;
	private int black = 255;
	
	public static int xLevelOffset = 0, yLevelOffset=(int)(160 * Game.SCALE);
	private static int subTick=0, tick=0;
	
	private int value = 4, valueX=-5;
	
	public End(Game game) 
	{
		super(game);
		initClasses();
		TextBox.loadBoxImages("/TextBoxNull.png");
	}
	
	private void initClasses() 
	{
		levelManager = new LevelManager(game, LoadSave.END_DATA);
		
		objTrashOne = new Trash("/TrashOne.png", (int)((31.6-21.7 + valueX)* Game.SCALE), (int)((6.3+value) * Game.SCALE), player, miniplayer);
		
		miniplayer = new MiniPlayer(0, 0 , (int)(5 * Game.SCALE), (int)(10 * Game.SCALE));
		miniplayer.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		
		player = new Player(-100, -100 , (int)(5 * Game.SCALE), (int)(10 * Game.SCALE));
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		
		garbagetruck = new Vehicle(true,(int)(30 * Game.SCALE), (int)(31.5 * Game.SCALE));
	}
	
	public MiniPlayer getminiplayer()
	{
		return miniplayer;
	}
	
	@Override
	public void update() 
	{
		xLevelOffset+=1;
		
		objTrashOne.update();
		levelManager.update();
		miniplayer.update();
		skipToMenu();
		
		updateTick();

		if (tick>=200)
		{
			if (black<255)
				black++;
			else
			{
				black=255;
				events=1;
			}
		}
		else
		{
			if (black>0)
				black--;
			else
				black=0;
		}
		
		switch (events)
		{
			case 1:
			{
				xLevelOffset=0;
				subTick=0;
				tick=0;
				skipPercentage=skipPercentageValue;
				events=0;
				Playing.dashEnable = false;
				Playing.newGame=true;
				Player.active=true;
				Gamestate.state = Gamestate.MENU;
				break;
			}
		}
	} 
	
	private void resetThings()
	{
		tick=0;
		subTick=0;
		TextBox.resetTick();
	}
	
	private void updateTick()
	{
		subTick++;
		if (subTick>10)
		{
			tick++;
			subTick=0;
		}
		garbagetruck.update();
	}
	
	@Override
	public void draw(Graphics g) 
	{
		levelManager.draw(g, xLevelOffset, yLevelOffset);
		
		garbagetruck.draw(g, 0, 0);
		
		g.setColor(new Color(0,0,0,100));
		g.fillRect(0, 0,(int)(Game.GAME_WIDTH/3.5), Game.GAME_HEIGHT);
		
		miniplayer.render(g);
		garbagetruck.draw(g, xLevelOffset, yLevelOffset);
		
		objTrashOne.draw(g, 0, 0, player, miniplayer);
		Props.draw(g, skull, (int)((32-22 + valueX) * Game.SCALE), (int)( (11+value) * Game.SCALE), 0, 0);
		Props.draw(g, replay,(int)((31.8-22 + valueX) * Game.SCALE), (int)( (16+value) * Game.SCALE), 0, 0);
		
		Props.draw(g, endgame,(int)((34 + valueX) * Game.SCALE), (int)( (3+value) * Game.SCALE), 0, 0);
		
		g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
		
		g.setColor(Color.BLACK);
		g.drawString(Game.collectedTrashes + " / 10", (int)((44 + valueX) * Game.SCALE), (int)((51 + value) * Game.SCALE));
		
		g.setColor(Color.BLACK);
		g.drawString(Game.deathCount + "", (int)((44 + valueX) * Game.SCALE), (int)((70 + value) * Game.SCALE));
		
		g.setColor(Color.BLACK);
		g.drawString(Game.attemptCount + "", (int)((44 + valueX) * Game.SCALE), (int)((89 + value) * Game.SCALE));
		
		g.setColor(new Color(0,0,0,black));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
	}
	
	private void skipToMenu()
	{	
		if (space)
			skipPercentage-=skipValue;
		else
			skipPercentage+=skipValue;
		
		if (skipPercentage>skipPercentageValue)
			skipPercentage=skipPercentageValue;
		
		if (skipPercentage<=0)
		{
			tick+=1000;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		switch (e.getKeyCode()) 
		{
			//Tecla de espaço
			case KeyEvent.VK_SPACE:
			{
				setSpace(true);
				break;
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) 
	{
		switch (e.getKeyCode()) 
		{
			//Tecla para Esquerda
			case KeyEvent.VK_SPACE:
			{
				setSpace(false);
				break;
			}
		}
	}
	
	public void setSpace(boolean space)
	{
		this.space = space;
	}
}
