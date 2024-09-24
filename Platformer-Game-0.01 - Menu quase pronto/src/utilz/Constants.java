package utilz;

import main.Game;

public class Constants 
{
	public static class UI
	{
		public static class Buttons
		{
			//Tamanho de cada botão do menu;
			public static final int BUTTON_WIDTH_DEFAULT = 80;
			public static final int BUTTON_HEIGHT_DEFAULT = 32;
			
			public static final int BUTTON_WIDTH = (int) (BUTTON_WIDTH_DEFAULT * Game.SCALE);
			public static final int BUTTON_HEIGHT = (int) (BUTTON_HEIGHT_DEFAULT * Game.SCALE);
		}
	}
	
	public static class Directions 
	{
		public static final int LEFT = 	0;
		public static final int UP = 	1;
		public static final int RIGHT = 2;
		public static final int DOWN = 	3;
	}

	public static class PlayerConstants
	{
		public static final int IDLE 	= 0;
		public static final int DASH	= 1;
		public static final int CROUNCH = 2;
		public static final int JUMP 	= 3;
		public static final int FALLING = 4;
		public static final int RUNNING = 5;
		public static final int CLIMB 	= 6;
		public static final int HIT 	= 7;
		public static final int GROUND 	= 8;
		
		public static int GetSpriteAmount(int player_action) 
		{
			switch (player_action) 
			{
			case JUMP:
			case DASH:
			case CLIMB:
				return 1;
				
			case FALLING:
				return 3;

			case CROUNCH:
				return 5;
				
			case IDLE:
				return 6;
				
			case RUNNING:
				return 12;
				
			default:
				return 1;
			}
		}
	}
}
