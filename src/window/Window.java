package window;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;
import input.InputHandler;

public class Window extends JFrame{
	
	public static void main(String[] args){

		Window window = new Window();

	}

	public Window(){
	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("~~~~~~~");

		GameCanvas canvas = new GameCanvas();
		this.add(canvas);
		canvas.setPreferredSize(new Dimension(1280, 720));
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		canvas.createBuffer();

		canvas.run();
	}
}