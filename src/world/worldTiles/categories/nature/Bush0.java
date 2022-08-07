package world.worldTiles.categories.nature;

import assets.textures.world.nature.grass.GrassTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class Bush0 extends WorldTile {
	
	public Bush0() {
		super(TileIDs.BUSH0);
		setTexture(GrassTextures.bush_0);
		setSideTexture(GrassTextures.grass.getRandVariant());
	}
	
}
