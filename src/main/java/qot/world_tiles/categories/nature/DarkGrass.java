package qot.world_tiles.categories.nature;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class DarkGrass extends WorldTile {
	
	public DarkGrass() {
		super(TileIDs.DARK_GRASS);
		setSprite(new Sprite(GrassTextures.dark_grass));
		setMiniMapColor(0xff007800);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DarkGrass());
	}
	
}
