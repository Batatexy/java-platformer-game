package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

import utilz.LoadSave;

public class GameWindow {
	private JFrame jframe;

	public GameWindow(GamePanel gamePanel) {

		jframe = new JFrame();
		jframe.add(gamePanel);

		//Definir titulo e icone da janela
		jframe.setTitle("Gari");
		//jframe.setIconImage(LoadSave.GetSpriteAtlas(LoadSave.ICON_ATLAS));
		
		//Configuracoes de iniciar a tela no meio do monitor, fechar programa automatico e tornar a tela visivel
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//jframe.setLocationRelativeTo(null);
		jframe.setResizable(false);
		jframe.pack();
		
		jframe.addWindowFocusListener
		(new WindowFocusListener() 
			{
				@Override
				public void windowLostFocus(WindowEvent e) 
				{
					System.out.println("Window Focus Lost");
					gamePanel.getGame().windowFocusLost();
				}
	
				@Override
				public void windowGainedFocus(WindowEvent e) 
				{
					System.out.println("Window Gained Focus");
				}
			}
		);

		jframe.setVisible(true);
	}

}
