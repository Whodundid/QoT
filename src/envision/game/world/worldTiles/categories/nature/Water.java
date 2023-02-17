package envision.game.world.worldTiles.categories.nature;

import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.water.WaterTextures;

public class Water extends WorldTile {
	
	public Water() {
		super(TileIDs.WATER);
		setBlocksMovement(true);
		setTexture(WaterTextures.water);
		setWall(true);
		wallHeight = -0.05;
	}
	
	@Override
	public void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY, int distX, int distY) {
		super.draw(world, camera, midDrawX, midDrawY, midX, midY, distX, distY);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Water());
	}
	
}
