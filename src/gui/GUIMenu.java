package gui;

import resources.*;
import input.InputHandler;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GUIMenu extends GUIComponent {
	
	protected Point locationRelativeToMouse;
	protected BufferedImage background;
	protected boolean dragged;
	protected ArrayList<Button> menuButtons;
	protected SpriteFont font;
	private String title;

	public GUIMenu(String spriteSheet, int x, int y, int width, int height){

		super(spriteSheet, x, y, width, height);

		SpriteSheet s = ResourceManager.getSpriteSheet(spriteSheet);

		this.background = ImageTools.combineImages(width, height, s.getAllImages());
		this.dragged = false;

		this.font = ResourceManager.getFont("font_large.png");
		this.title = null;
		menuButtons = new ArrayList<Button>();
	}

	public GUIMenu(String spriteSheet, int x, int y, int width, int height, String title){

		super(spriteSheet, x, y, width, height);
	
		SpriteSheet s = ResourceManager.getSpriteSheet(spriteSheet);

		this.background = ImageTools.combineImages(width, height, s.getAllImages());
		this.dragged = false;

		this.font = ResourceManager.getFont("font_large.png");
		this.title = title;
		menuButtons = new ArrayList<Button>();
	}

	public void addButton(Button button){

		button.setLocation(button.getLocation().x + location.x, button.getLocation().y + location.y);
		menuButtons.add(button);

	}

	@Override
	public void draw(Graphics2D g){

		g.drawImage(background, location.x, location.y, null);

		if (title != null){

			font.setColor(java.awt.Color.WHITE);
			font.drawText(title, location.x + 2, location.y + 2, g);

		}

		if (menuButtons == null) {return;}

		for (Button button : menuButtons){

			button.draw(g);

		}

	}

	@Override
	public void update(double elapsedMilliseconds){

		/*if ((this.location.x + this.size.width) > 640){

			this.location.x -= (640 - this.location.x);

		}*/

		if (menuButtons == null) {return;}

		for (Button button : menuButtons){

			button.update(elapsedMilliseconds);

		}
	}

	@Override
	public void processInput(){

		if (InputHandler.MOUSE_CLICKED && !InputHandler.MOUSE_DRAGGED) {

			if (isInsideBoundaries(InputHandler.MOUSE_LOCATION)){

				locationRelativeToMouse = new Point(location.x - InputHandler.MOUSE_LOCATION.x, location.y - InputHandler.MOUSE_LOCATION.y);
				dragged = true;

			}

		} else if (InputHandler.MOUSE_DRAGGED && dragged){

			Point oldLocation = location;
			Point newLocation =  new Point(InputHandler.MOUSE_LOCATION.x + locationRelativeToMouse.x,
				InputHandler.MOUSE_LOCATION.y + locationRelativeToMouse.y);

			
			location = fixLocation(newLocation);

			for (Button button : menuButtons){

				button.setLocation(button.getLocation().x + (location.x - oldLocation.x), button.getLocation().y + (location.y - oldLocation.y));

			}

		} else {

			dragged = false;
			locationRelativeToMouse = null;

		}

		if (menuButtons == null) {return;}

		for (Button button : menuButtons){

			button.processInput();

		}

	}

	private Point fixLocation(Point fixedPoint){

		if (fixedPoint.x + size.width > window.GameCanvas.size.width){

			fixedPoint = new Point (window.GameCanvas.size.width - this.size.width, fixedPoint.y);

		}

		if (fixedPoint.y + size.height > window.GameCanvas.size.height){

			fixedPoint = new Point (fixedPoint.x, window.GameCanvas.size.height - this.size.height);

		}

		if (fixedPoint.y < 0){

			fixedPoint = new Point(fixedPoint.x, 0);

		}

		if (fixedPoint.x < 0){

			fixedPoint.x = 0;

		}
		return fixedPoint;
	}
}