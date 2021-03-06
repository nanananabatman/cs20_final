package gamescreen;

import java.awt.Graphics2D;

public interface GameScreen {
	
	public void update(double elapsedMilliseconds);

	public void draw(Graphics2D g);

	public void processInput();

	public String getTitle();

	public void loadResources();

	public void unloadResources();
}