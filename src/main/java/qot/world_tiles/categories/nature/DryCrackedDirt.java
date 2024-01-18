package qot.world_tiles.categories.nature;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class DryCrackedDirt extends WorldTile {
	
	public DryCrackedDirt() {
		super(TileIDs.DRY_CRACKED_DIRT);
		setSprite(new Sprite(NatureTextures.dry_cracked_dirt));
		setMiniMapColor(0xff8F612F);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DryCrackedDirt());
	}
	
}
