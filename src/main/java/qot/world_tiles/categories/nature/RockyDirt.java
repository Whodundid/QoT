package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class RockyDirt extends WorldTile {
	
	public RockyDirt() {
		super(TileIDs.ROCKY_DIRT);
		setSprite(new Sprite(NatureTextures.rocky_dirt));
		setMiniMapColor(0xff6A3D1B);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new RockyDirt());
	}
	
}
