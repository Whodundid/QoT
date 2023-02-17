package envision.game.world.worldTiles.categories.nature;

import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
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
