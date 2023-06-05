package envision.game.world;

import envision.game.GameObject;
import envision.game.entities.Entity;
import envision.game.entities.EntitySpawn;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.util.EList;

public class WorldLayer {
	
	protected int width, height;
	protected WorldTile[][] worldData;
	
	protected EList<GameObject> worldObjects = EList.newList();
	protected EList<Entity> entityData = EList.newList();
	protected EList<EntitySpawn> entitySpawns = EList.newList();
	protected EList<Region> regionData = EList.newList();
	
	protected boolean isDrawn = true;
	
	public WorldLayer(int widthIn, int heightIn) {
		width = widthIn;
		height = heightIn;
		worldData = new WorldTile[height][width];
	}
	
	public void setDimensions(int widthIn, int heightIn) {
		width = widthIn;
		height = heightIn;
		worldData = new WorldTile[height][width];
	}
	
	public WorldLayer copyLayer() {
		WorldLayer copy = new WorldLayer(width, height);
		
		//copy tile data
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				var tile = getTileAt(x, y);
				if (tile == null) continue;
				copy.worldData[y][x] = WorldTile.copy(tile);
			}
		}
		
		return copy;
	}
	
	public void onWorldTick() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				WorldTile t = worldData[y][x];
				if (t != null) t.onWorldTick();
			}
		}
	}
	
	public WorldTile getTileAt(int xIn, int yIn) {
		return worldData[yIn][xIn];
	}
	
	public void setTileAt(WorldTile in, int xIn, int yIn) {
		if (in != null) in.setWorldPos(xIn, yIn);
		worldData[yIn][xIn] = in;
	}
	
	public void fillWith(WorldTile t) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				worldData[y][x] = WorldTile.randVariant(t).setWorldPos(x, y);
			}
		}
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public WorldTile[][] getWorldData() { return worldData; }
	
	public boolean isDrawn() { return isDrawn; }
	public void setDrawn(boolean val) { isDrawn = val; }
	
}
