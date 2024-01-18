package qot.world_tiles.categories.nature;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class SmoothDirt extends WorldTile {
	
	public SmoothDirt() {
		super(TileIDs.SMOOTH_DIRT);
		setSprite(new Sprite(NatureTextures.smooth_dirt));
		setMiniMapColor(0xff88582D);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new SmoothDirt());
	}
	
}
