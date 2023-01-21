package envisionEngine.gameEngine.world.worldTiles.categories.nature;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;

public class CrackedDirt extends WorldTile {
	
	public CrackedDirt() {
		super(TileIDs.CRACKED_DIRT);
		setTexture(NatureTextures.cracked_dirt);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new CrackedDirt());
	}
	
}
