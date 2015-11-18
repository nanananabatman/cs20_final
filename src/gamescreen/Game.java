package gamescreen;

import java.awt.Color;
import java.awt.Graphics2D;

import game.BSPRect;
import game.WorldGenerator;

public class Game implements GameScreen {
	
	BSPRect b;
	
	public Game(){
		
		loadResources();
		
	}
	
	@Override
	public void update(double elapsedMilliseconds){



	}

	@Override
	public void draw(Graphics2D g){

		for (BSPRect rect : b.subRectangles){
		
			g.setColor(Color.RED);
			rect.draw(g);

		}
	}

	@Override
	public void processInput(){



	}

	@Override
	public String getTitle(){

		return "Yo! The Game.";

	}

	@Override
	public void loadResources(){
		
		WorldGenerator g = new WorldGenerator(1234);
		b = g.createSpaces(15);

	}

	@Override
	public void unloadResources(){



	}

}