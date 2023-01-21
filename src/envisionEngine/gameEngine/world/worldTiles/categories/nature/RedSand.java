package envisionEngine.gameEngine.world.worldTiles.categories.nature;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.sand.SandTextures;

public class RedSand extends WorldTile {
	
	public RedSand() {
		super(TileIDs.RED_SAND);
		setTexture(SandTextures.red_sand);
		setWall(true);
		wallHeight = 0.05;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new RedSand());
	}
	
}
