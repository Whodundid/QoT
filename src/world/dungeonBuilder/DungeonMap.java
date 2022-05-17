package world.dungeonBuilder;

import eutil.misc.Direction;
import world.EntitySpawn;
import world.GameWorld;
import world.resources.WorldTile;

/** A map manager which allows multiple builders to be able to add onto the same map object
 *  at the same time without overlap. */
public class DungeonMap {
	
	int w, h;
	int roomSize;
	private int remaining = 0;
	/** The working map array for which builders add to. */
	private DungeonPiece[][] map;
	/** Keeps track of whether or not a spot is available to be claimed. */
	private boolean[][] availability;
	/** Keeps track of the builder that has claimed a spot. */
	private int[][] claimerID;
	
	public DungeonMap(DungeonSize sizeIn, int roomSizeIn, int roomNum) {
		w = sizeIn.width / roomSizeIn;
		h = sizeIn.height / roomSizeIn;
		roomSize = roomSizeIn;
		remaining = roomNum;
		
		map = new DungeonPiece[w][h];
		availability = new boolean[w][h];
		claimerID = new int[w][h];
		
		//set every space to true to indicate that the space is available
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				availability[i][j] = true;
				claimerID[i][j] = -1; //-1 indicates no builder assigned 
			}
		}
	}
	
	public synchronized boolean anyRemaining() {
		return remaining > 0;
	}
	
	/** Returns true if the spot is currently available. */
	public synchronized boolean checkSpot(int x, int y) {
		return availability[x][y] && remaining > 0;
	}
	
	public synchronized void claimSpot(int x, int y, int builderID) {
		if (availability[x][y]) {
			availability[x][y] = false;
			claimerID[x][y] = builderID;
			remaining--;
		}
	}
	
	public synchronized void setSpot(int x, int y, int builderID, DungeonPiece piece) {
		//ensure that the correct builder is assigning the spot
		if (claimerID[x][y] == builderID) {
			//assign the spot
			map[x][y] = piece;
		}
	}
	
	/** Ignores availability concerns. */
	public void forceSet(int x, int y, DungeonPiece piece) {
		map[x][y] = piece;
		availability[x][y] = (piece == null);
	}
	
	private String getIDError(int x, int y, int wrongID) {
		int expected = claimerID[x][y];
		return "Expected builderID: '" + expected + "' but got '" + wrongID + "' instead!";
	}
	
	public boolean inMap(int x, int y) {
		return (x >= 0 && x < w) && (y >= 0 && y < h);
	}
	
	public synchronized void reduceRemaining() {
		remaining--;
	}
	
	public synchronized int getRemaining() {
		return remaining;
	}
	
	public void display() {
		for (int i = 0; i < w; i++) {
			String line = "|";
			for (int j = 0; j < h; j++) {
				DungeonPiece p = map[i][j];
				line += (p != null) ? "x" : " ";
				line += (j >= h - 1) ? "" : " ";
			}
			System.out.println(line + "|");
		}
	}
	
	public GameWorld buildMap() {
		//make the world with the given dimensions
		GameWorld theWorld = new GameWorld("dungeon", w * roomSize, h * roomSize);
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				DungeonPiece p = map[i][j];
				if (p != null) {
					boolean top, left, right, down;
					
					top = check(i, j - 1);
					left = check(i - 1, j);
					right = check(i + 1, j);
					down = check(i, j + 1);
					
					if (top) p.addOpen(Direction.N);
					if (left) p.addOpen(Direction.W);
					if (right) p.addOpen(Direction.E);
					if (down) p.addOpen(Direction.S);
					
					p.build();
				}
			}
		}
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				DungeonPiece p = map[i][j];
				if (p != null) {
					//add world tiles
					addRoom(theWorld, p, i * roomSize, j * roomSize);
					//add entities
					for (EntitySpawn spawn : p.getEntities()) {
						spawn.setPosition(spawn.getX() + (i * roomSize), spawn.getY() + (j * roomSize));
						theWorld.addEntitySpawn(spawn);
					}
				}
			}
		}
		
		return theWorld;
	}
	
	private boolean check(int x, int y) {
		return inMap(x, y) && map[x][y] != null;
	}
	
	private static void addRoom(GameWorld world, DungeonPiece room, int sX, int sY) {
		WorldTile[][] startTiles = room.getTiles();
		for (int i = 0; i < room.w; i++) {
			for (int j = 0; j < room.h; j++) {
				world.setTileAt(sX + i, sY + j, startTiles[i][j]);
			}
		}
	}
	
}
