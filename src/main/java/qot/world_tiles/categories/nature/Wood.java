package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.wood.WoodFloorTextures;
import qot.world_tiles.TileIDs;

public class Wood extends WorldTile {
	
	public Wood() {
		super(TileIDs.WOOD);
		setSprite(new Sprite(WoodFloorTextures.wood_floor));
		setMiniMapColor(0xff987839);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Wood());
	}
	
}