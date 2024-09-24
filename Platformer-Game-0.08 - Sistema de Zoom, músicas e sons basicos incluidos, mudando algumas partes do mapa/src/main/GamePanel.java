package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel 
{
	private MouseInputs mouseInputs;
	private Game game;
	
	public GamePanel(Game game) 
	{
		setPanelSize();
		this.game = game;
		
		//Inputs de mouse e teclado
		mouseInputs = new MouseInputs(this);
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}
	
	//Definir o tamanho da tela dentro da janela
	private void setPanelSize() 
	{
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
		
		System.out.println("Size: " + GAME_WIDTH + " | " + GAME_HEIGHT);
	}
	
	//Metodo pra desenhar o visual do jogo, proporcionando a alteracao de FPS,
	//ja que o  tempo de atualizacao da logica do jogo nao sera alterada
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		game.render(g);
	}
	
	public Game getGame() 
	{
		return game;
	}
}