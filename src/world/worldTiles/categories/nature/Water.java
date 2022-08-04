package world.worldTiles.categories.nature;

import assets.textures.world.nature.water.WaterTextures;
import world.GameWorld;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class Water extends WorldTile {
	
	public Water() {
		super(TileIDs.WATER);
		setBlocksMovement(true);
		setTexture(WaterTextures.water);
		setWall(true);
		wallHeight = -0.05;
	}
	
	@Override
	public void renderTile(GameWorld world, double x, double y, double w, double h, int brightness) {
		super.renderTile(world, x, y, w, h, brightness);
	}
	
}
