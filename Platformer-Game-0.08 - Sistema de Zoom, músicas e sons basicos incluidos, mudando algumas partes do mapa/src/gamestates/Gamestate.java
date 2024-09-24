package gamestates;

public enum Gamestate 
{
	INTRODUCTION, MENU, PLAYING, QUIT, CUTSCENE, END;
	public static Gamestate state = INTRODUCTION;
}