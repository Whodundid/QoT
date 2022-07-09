package game.worldTiles.tileCategories.nature;

import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;
import world.GameWorld;

public class Mud extends WorldTile {
	
	public Mud() {
		super(TileIDs.MUD);
	}
	
	@Override
	public void renderTile(GameWorld world, double x, double y, double w, double h, int brightness) {
		
	}
	
	@Override
	public void onWorldTick() {
		
	}
	
}
