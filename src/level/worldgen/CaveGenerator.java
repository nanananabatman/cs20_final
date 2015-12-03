package level.worldgen;

import java.util.Random;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.Dimension;

import level.Level;
import level.Tile;

public class CaveGenerator {
	
	private static Random r;

	public CaveGenerator(int seed){

		r = new Random(seed);

	}

	public CaveGenerator(){

		r = new Random();

	}

	public Level createLevel(int width, int height, int seed, int steps, float percentLand){
		
		CaveNode[][] cave = generateMap(width, height, steps, percentLand);
		Level l = new Level(width, height, "Cave", "snow.png");
		l.addLayer();
		
		for (int i = 0; i < cave.length; i++){
			
			for (int j = 0; j < cave[0].length; j++){
				
				l.addTile(interpretTile(j, i, cave), j, i, 0);
			}
		}
		
		return l;
	}

	private Tile interpretTile(int x, int y, CaveNode[][] map){

		if (map[y][x].empty){

			return new Tile(44);

		}

		boolean nw = (x > 0 && y > 0) ? !map[y - 1][x - 1].empty : true;                      //North-West neighbour
		boolean n  = (y > 0) ? !map[y - 1][  x  ].empty : true;                               //North neighbour
		boolean ne = (x < map[0].length - 1 && y > 0) ? !map[y - 1][x + 1].empty : true;          //North-East neighbour
		boolean w  = (x > 0) ? !map[  y  ][x - 1].empty : true;                               //West neighbour
		boolean e  = (x < map[0].length - 1) ? !map[  y  ][x + 1].empty : true;                   //East neighbour
		boolean se = (x < map[0].length - 1 && y < map.length - 1) ? !map[y + 1][x + 1].empty : true;             //South-East neighbour
		boolean s  = (y < map.length - 1) ? !map[y + 1][  x  ].empty : true;                      //South neighbour
		boolean sw = (x > 0 && y < map.length - 1) ? !map[y + 1][x - 1].empty : true; //South-West neighbour

		Tile tile = new Tile(31);

		//THIS IS SUPER CONFUSING AND HARD!!
		
		// ###
		// #X#
		// OOO
		if (n & e & w & !s){

			tile = new Tile(3);

		} else if (n & e & w & s & !se & !sw){

			tile = new Tile(40);

		}

		// OOO
		// #X#
		// ###
		if (!n & e & w & s & sw){

			tile = new Tile(21);

		} else if (n & e & w & s & !ne & !nw){

			tile = new Tile(33);

		}

		// O##
		// OX#
		// O##
		if ((n & e & !w & s) || (n & e & w & s & !nw & !sw)){

			tile = new Tile(13);

		}

		// ##O
		// #XO
		// ##O
		if ((n & w & !e & s) || (n & e & w & s & !ne & !se)){

			tile = new Tile(11);

		}

		// OOO
		// OX#
		// O##
		if ((!n & e & !w & s) || 
			(!n & e & w & s & !nw & !sw)){

			tile = new Tile(0);

		}

		// OOO
		// #XO
		// ##O
		if (!n & !e & w & s){

			tile = new Tile(1);

		}

		// O##
		// OX#
		// OOO
		if (n & e & !w & !s){

			tile = new Tile(9);

		}

		// ##O
		// #XO
		// OOO
		if (n & !e & w & !s){

			tile = new Tile(10);

		}

		// ###
		// #X#
		// ###
		if (n & s & e & w && ((ne & nw & se & sw) || !(ne & nw & se & sw))){

			tile = new Tile(40);

		}

		// X##
		// #OO
		// #OO
		if (n & e & w & s & !se & sw & ne & nw){

			tile = new Tile(11);

		}

		// ##X
		// OO#
		// OO#
		if (n & e & w & s & se & !sw & ne & nw){

			tile = new Tile(13);

		}

		if ((n & !e & !w & s) || 
			(n & e & !w & s & ((ne ^ se) || (!ne & !se))) || 
			(n & !e & w & s & ((nw ^ sw) || (!nw & !sw))) || 
			(n & e & w & s & !ne & !nw & !se & !sw)){

			tile = new Tile(32);

		}

		if (!n & !e & !w & s){

			tile = new Tile(23);

		}

		if (n & !s & !e & !w){

			tile = new Tile(41);

		}

		return tile;

	}
	
	public CaveNode[][] generateMap(int width, int height, int steps, float percentLand){

		double timer = System.currentTimeMillis();

		boolean[][] world = new boolean[height][width];

		for (int i = 0; i < height; i++){

			for (int j = 0; j < width; j++){

				world[i][j] = !(r.nextFloat() < percentLand);

			}
		}

		//Run run cells then transfer data to CaveNodes
		world = run(steps, world);
		CaveNode[][] cave = new CaveNode[height][width];

		for (int i = 0; i< height; i++){

			for (int j = 0; j < width; j++){

				cave[i][j] = new CaveNode();
				cave[i][j].empty = world[i][j];

			}
		}

		//Use flood-fill to tag all separate cave systems
		int currentTag = 0;

		for (int i = 0; i< height; i++){

			for (int j = 0; j < width; j++){

				if (cave[i][j].empty && cave[i][j].tag == 0){

					currentTag++;
					flood(j, i, currentTag, 0, 0, cave);

				}
			}
		}


		//Get rid of all caves except largest
		int mostTagged = getLargestArea(cave);

		for (int i = 0; i< height; i++){

			for (int j = 0; j < width; j++){

				if (cave[i][j].tag != mostTagged){

					cave[i][j].tag = 0;
					cave[i][j].empty = false;

				}
			}
		}

		System.out.println(System.currentTimeMillis() - timer + " milliseconds");
		return cave;

	}

	//Simulates a cell generation
	private boolean[][] run(int steps, boolean[][] initialGrid){

		for (int i = 0; i < steps; i++){

			boolean[][] nextGen = new boolean[initialGrid.length][initialGrid[0].length];

			for (int j = 0; j < initialGrid.length; j++){

				for (int k = 0; k < initialGrid[0].length; k++){

					nextGen[j][k] = nextGeneration(k, j, initialGrid);

				}
			}

			initialGrid = nextGen;

		}

		for (int i = 0; i < initialGrid.length; i++){

			for (int j = 0; j < initialGrid[0].length; j++){

				if (i == 0 ||
					j == 0 ||
					i == initialGrid.length - 1 ||
					j == initialGrid[0].length - 1){

					initialGrid[i][j] = false;

				}
			}
		}

		return initialGrid;

	}

	private boolean nextGeneration(int x, int y, boolean[][] grid){

		int neighbours = 0;

		for (int i = (y - 1); i <= (y + 1); i++){

			for (int j = (x - 1); j <= (x + 1); j++){

				try {

					neighbours += (grid[i][j] && !((i == y) && (j == x))) ? 1 : 0;

				} catch (Exception e){}
			}
		}

		if (neighbours == 4 && grid[y][x]){

			return true;

		} else if (neighbours > 4){

			return true;

		} else {

			return false;

		}
	}

	//tags all connected cave tiles matching target tag recursivley
	private void flood(int x, int y, int newTag, int targetTag, int depth, CaveNode[][] map){

		if ((x < 0) && (y < 0) && (x >= map[0].length) && (y >= map.length)){
			
			return;
			
		} else if (!(map[y][x].tag == targetTag) || !(map[y][x].empty) || (depth > 4800)){
			
			return;
			
		} else {
			
			map[y][x].tag = newTag;
			flood(x + 1, y, newTag, targetTag, depth + 1, map);
			flood(x - 1, y, newTag, targetTag, depth + 1, map);
			flood(x, y - 1, newTag, targetTag, depth + 1, map);
			flood(x, y + 1, newTag, targetTag, depth + 1, map);
			
		}
	}

	//goes through all tags and returns the largest
	private int getLargestArea(CaveNode[][] map){

		HashMap<Integer, Integer> tags = new HashMap<Integer, Integer>();

		for (int i = 0; i < map.length; i++){

			for (int j = 0; j < map[0].length; j++){

				if (map[i][j].tag != 0){

					if (tags.get(map[i][j].tag) == null){

						tags.put(map[i][j].tag, 1);

					} else {

						tags.put(map[i][j].tag, tags.get(map[i][j].tag) + 1);

					}
				}
			}
		}

		Iterator i = tags.keySet().iterator();
		int highestTag = 0;
		int highestTagAmount = 0;

		while (i.hasNext()){

			int tag = (int) i.next();
			
			//System.out.println("Tag: " + tag + " | " + tags.get(tag));

			if (tags.get(tag) > highestTagAmount){

				highestTagAmount = tags.get(tag);
				highestTag = tag;

			}

		}

		//System.out.println("Highest Tag: " + highestTag + " (" + tags.get(highestTag) + ")");
		return highestTag;

	}

	//class to just hold the tags for each array item
	public class CaveNode {

		public int tag;
		public boolean empty;

	}
}