package world.dungeonBuilder;

import eutil.datatypes.EArrayList;
import eutil.misc.Direction;
import eutil.random.RandomUtil;
import world.EntitySpawn;
import world.resources.WorldTile;

public abstract class DungeonPiece {
	
	protected WorldTile[][] tiles;
	protected EArrayList<EntitySpawn> entitySpawns = new EArrayList();
	protected int w, h;
	protected EArrayList<Direction> open = new EArrayList();
	protected boolean isSpawn;
	
	protected DungeonPiece(int w, int h, boolean spawn) {
		tiles = new WorldTile[this.h = h][this.w = w];
		isSpawn = spawn;
	}
	
	public abstract void build();
	
	public WorldTile[][] getTiles() { return tiles; }
	public WorldTile getTileAt(int x, int y) { return tiles[y][x]; }
	public EArrayList<EntitySpawn> getEntities() { return entitySpawns; }
	public void setTileAt(int x, int y, WorldTile t) { tiles[y][x] = t; }
	public void addOpen(Direction d) { open.addIfNotContains(d); }
	public void addEntity(EntitySpawn s) { entitySpawns.addIfNotNull(s); }
	public boolean isSpawn() { return isSpawn; }
	
	public int getWidth() { return w; }
	public int getHeight() { return h; }
	
	protected static int randInt(int low, int high) { return RandomUtil.getRoll(low, high); }
	
}
