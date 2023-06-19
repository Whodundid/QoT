package qot.world_tiles.categories.nature;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class IcySnow extends WorldTile {
	
	public IcySnow() {
		super(TileIDs.ICY_SNOW);
		setTexture(NatureTextures.icy_snow);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new IcySnow());
	}
	
}