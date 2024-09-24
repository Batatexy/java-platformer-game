package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import utilz.LoadSave;

public class GameWindow 
{
	private JFrame jframe;

	public GameWindow(GamePanel gamePanel) 
	{
		jframe = new JFrame();

		//Configuracoes de iniciar a tela no meio do monitor, fechar programa automatico e tornar a tela visivel
		//Dependendo da ordem em que os itens abaixo sao organizados, podem dar uma bugada em algumas funcoes no jogo
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);

		jframe.setResizable(false);
		jframe.pack();
		jframe.setLocationRelativeTo(null);
		jframe.setVisible(true);

		jframe.addWindowFocusListener
		(new WindowFocusListener() 
			{
				@Override
				public void windowLostFocus(WindowEvent e) 
				{
					gamePanel.getGame().windowFocusLost();
				}
	
				@Override
				public void windowGainedFocus(WindowEvent e) 
				{
					System.out.println("Window Gained Focus");
				}
			}
		);

		//Definir titulo e icone da janela
		BufferedImage icon = LoadSave.GetSpriteAtlas(LoadSave.ICON_ATLAS);
		//Lembrar de pensar em um nome melhor kkkkk
		jframe.setTitle("APS Super Gari Jumper");
		jframe.setIconImage(icon);
	}

}
