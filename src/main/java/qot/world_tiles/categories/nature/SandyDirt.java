package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class SandyDirt extends WorldTile {
	
	public SandyDirt() {
		super(TileIDs.SANDY_DIRT);
		setTexture(NatureTextures.sandy_dirt);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new SandyDirt());
	}
	
}