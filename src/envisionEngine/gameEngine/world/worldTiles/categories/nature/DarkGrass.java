package envision.gameEngine.world.worldTiles.categories.nature;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.grass.GrassTextures;

public class DarkGrass extends WorldTile {
	
	public DarkGrass() {
		super(TileIDs.DARK_GRASS);
		setTexture(GrassTextures.dark_grass);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DarkGrass());
	}
	
}
