package envision.game.world.worldTiles.categories.nature;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.world.nature.NatureTextures;

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
