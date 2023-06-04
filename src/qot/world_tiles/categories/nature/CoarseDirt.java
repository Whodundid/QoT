package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class CoarseDirt extends WorldTile {
	
	public CoarseDirt() {
		super(TileIDs.COARSE_DIRT);
		setTexture(NatureTextures.coarse_dirt);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new CoarseDirt());
	}
	
}
