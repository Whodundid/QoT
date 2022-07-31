package world.worldTiles.categories.nature;

import assets.textures.WorldTextures;
import world.GameWorld;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class Mud extends WorldTile {
	
	public Mud() {
		super(TileIDs.MUD);
		setTexture(WorldTextures.mud);
		setWall(true);
		wallHeight = -0.025;
	}
	
	@Override
	public void renderTile(GameWorld world, double x, double y, double w, double h, int brightness) {
		super.renderTile(world, x, y, w, h, brightness);
	}
	
	@Override
	public void onWorldTick() {
		
	}
	
}
