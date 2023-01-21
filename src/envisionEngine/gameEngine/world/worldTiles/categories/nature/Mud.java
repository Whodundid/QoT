package envisionEngine.gameEngine.world.worldTiles.categories.nature;

import envisionEngine.gameEngine.world.gameWorld.IGameWorld;
import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import envisionEngine.gameEngine.world.worldUtil.WorldCamera;
import qot.assets.textures.world.nature.NatureTextures;

public class Mud extends WorldTile {
	
	public Mud() {
		super(TileIDs.MUD);
		setTexture(NatureTextures.mud);
		setWall(true);
		wallHeight = -0.025;
	}
	
	@Override
	public void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY, int distX, int distY) {
		super.draw(world, camera, midDrawX, midDrawY, midX, midY, distX, distY);
	}
	
	@Override
	public void onWorldTick() {
		
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Mud());
	}
	
}
