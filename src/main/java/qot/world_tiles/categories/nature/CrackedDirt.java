package qot.world_tiles.categories.nature;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class CrackedDirt extends WorldTile {
	
	public CrackedDirt() {
		super(TileIDs.CRACKED_DIRT);
		setSprite(new Sprite(NatureTextures.cracked_dirt));
		setMiniMapColor(0xff6D3A1E);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new CrackedDirt());
	}
	
}
