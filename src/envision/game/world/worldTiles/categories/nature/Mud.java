package envision.game.world.worldTiles.categories.nature;

import envision.game.world.GameWorld;
import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.NatureTextures;

public class Mud extends WorldTile {
	
	public Mud() {
		super(TileIDs.MUD);
		setTexture(NatureTextures.mud);
		setWall(true);
		wallHeight = -0.025;
	}
	
	@Override
	public void renderTile(GameWorld world, double x, double y, double w, double h, int brightness, boolean mouseOver) {
		super.renderTile(world, x, y, w, h, brightness, mouseOver);
	}
	
	@Override
	public void onWorldTick() {
		
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Mud());
	}
	
}
