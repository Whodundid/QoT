package envision.game.world.worldTiles.categories.nature;

import envision.game.world.gameWorld.IGameWorld;
import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.water.WaterTextures;

public class Water extends WorldTile {
	
	public Water() {
		super(TileIDs.WATER);
		setBlocksMovement(true);
		setTexture(WaterTextures.water);
		setWall(true);
		wallHeight = -0.05;
	}
	
	@Override
	public void draw(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		super.draw(world, x, y, w, h, brightness, mouseOver);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Water());
	}
	
}
