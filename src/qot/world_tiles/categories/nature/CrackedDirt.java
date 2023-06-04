package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

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
