package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class CoarseDirt extends WorldTile {
	
	public CoarseDirt() {
		super(TileIDs.COARSE_DIRT);
		setSprite(new Sprite(NatureTextures.coarse_dirt));
		setMiniMapColor(0xff3E3F1D);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new CoarseDirt());
	}
	
}
