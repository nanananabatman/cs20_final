package level.worldgen;

//NOT USED IN PROJECT -- WAS JUST AN IDEA ON DUNGEON GENERATION

//Takes a rectangle (x * y) and recursively splits into 'leaves'
//never got it past that point

import java.util.Random;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

public class BSPNode {
	
	private static Random random = new Random();
	private static int minX = 16;
	private static int minY = 12;

	private int width;
	private int height;
	private int x;
	private int y;

	private BSPNode right;
	private BSPNode left;
	private Rectangle room;

	private Color c = Color.GREEN;

	private SplitDirection direction;
	protected boolean tooSmall;

	public BSPNode(int x, int y, int width, int height){

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		split();

		if (width < 8 || height < 8){

			tooSmall = true;
			
		}
		
		if (!tooSmall && (left == null && right == null)){
			
			int roomWidth;
			int roomHeight;
			
			do {
				
				roomWidth = random.nextInt(width);
				roomHeight = random.nextInt(height);
			
			} while (roomWidth < 6 || roomHeight < 6);
			
			room = new Rectangle(x + random.nextInt(width - roomWidth), y + random.nextInt(height - roomHeight), roomWidth, roomHeight);
			
		}
	}

	public void draw(Graphics2D g){

		if (right == null && left == null){

			g.setColor((tooSmall) ? Color.BLACK : Color.DARK_GRAY);
			for (int i = 0; i < height; i++){
				
				g.drawLine(7 * x, 7 * (i + y), 7 * (x + width), 7 * (i + y));
				
			}
			
			for (int j = 0; j < width; j++){
				
				g.drawLine(7 * (j + x), 7 * y, 7 * (j + x), 7 * (y + height));
				
			}
			
			if (room != null) {
				
				g.setColor(Color.WHITE);
				g.drawRect(7 * room.x, 7 * room.y, 7 * room.width, 7 * room.height);
			
			}
			
			g.setColor(c);
			g.draw(new Rectangle((int)(x * 7), (int)(y * 7), (int)(width * 7), (int)(height * 7)));
			
		} else {

			if (left != null){

				left.draw(g);

			}

			if (right != null){

				right.draw(g);

			}
		}
	}
	
	private void split(){

		if ((right != null || left != null)){

			direction = null;
			return;

		}

		if ((height >= width * 1.25) && canSplit()){

			direction = SplitDirection.VERTICAL;

		} else if ((width >= height * 1.25) && canSplit()){

			direction = SplitDirection.HORIZONTAL;

		} else if (!canSplit()){

			return;

		} else {

			direction = (random.nextInt(1) == 1) ? SplitDirection.HORIZONTAL : SplitDirection.VERTICAL;

		}

		int splitPoint;

		switch (direction){

			case VERTICAL:

				splitPoint = random.nextInt(height / 2) + (height / 5);
				left = new BSPNode(x, y, width, splitPoint);
				right = new BSPNode(x, y + splitPoint, width, height - splitPoint);
				break;

			case HORIZONTAL:

				splitPoint = random.nextInt(width / 2) + (width / 5);
				left = new BSPNode(x, y, splitPoint, height);
				right = new BSPNode(x + splitPoint, y, width - splitPoint, height);
				break;

		}

		//if (left.tooSmall) {left = null;}
		//if (right.tooSmall) {right = null;}
	}

	private boolean canSplit(){

		if (width > minX * 1.5 ||
			height > minY * 1.5){

			return true;

		}

		return false;

	}

	private enum SplitDirection {

		HORIZONTAL,
		VERTICAL;

	}
}