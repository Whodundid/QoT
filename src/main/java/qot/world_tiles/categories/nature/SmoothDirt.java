package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class SmoothDirt extends WorldTile {
	
	public SmoothDirt() {
		super(TileIDs.SMOOTH_DIRT);
		setTexture(NatureTextures.smooth_dirt);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new SmoothDirt());
	}
	
}
