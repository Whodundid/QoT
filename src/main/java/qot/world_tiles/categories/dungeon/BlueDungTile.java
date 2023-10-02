package qot.world_tiles.categories.dungeon;

import envision.engine.rendering.textureSystem.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class BlueDungTile extends WorldTile {
	
	public BlueDungTile() {
		super(TileIDs.BLUE_DUNG_TILE);
		setSprite(new Sprite(StoneFloorTextures.blue_dung_tile));
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new BlueDungTile());
	}
	
}