package game.worldTiles.tileCategories.nature;

import assets.textures.WorldTextures;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;
import world.GameWorld;

public class Water extends WorldTile {
	
	public Water() {
		super(TileIDs.WATER);
		setBlocksMovement(true);
		setTexture(WorldTextures.water);
	}
	
	@Override
	public void renderTile(GameWorld world, double x, double y, double w, double h, int brightness) {
		super.renderTile(world, x, y, w, h, brightness);
	}
	
}
