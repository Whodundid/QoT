package qot.world_tiles.categories.nature;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class IcySnow extends WorldTile {
	
	public IcySnow() {
		super(TileIDs.ICY_SNOW);
		setSprite(new Sprite(NatureTextures.icy_snow));
		setMiniMapColor(0xff9ADAF0);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new IcySnow());
	}
	
}
