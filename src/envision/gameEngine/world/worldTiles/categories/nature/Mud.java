package envision.gameEngine.world.worldTiles.categories.nature;

import envision.gameEngine.world.gameWorld.IGameWorld;
import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.NatureTextures;

public class Mud extends WorldTile {
	
	public Mud() {
		super(TileIDs.MUD);
		setTexture(NatureTextures.mud);
		setWall(true);
		wallHeight = -0.025;
	}
	
	@Override
	public void draw(IGameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		super.draw(world, x, y, w, h, brightness, mouseOver);
	}
	
	@Override
	public void onWorldTick() {
		
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Mud());
	}
	
}
