package qot.world_tiles.categories.dungeon;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class DungFloor extends WorldTile {
	
	public DungFloor() {
		super(TileIDs.DUNG_FLOOR);
		setSprite(new Sprite(StoneFloorTextures.dung_floor));
		setMiniMapColor(0xff363636);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DungFloor());
	}
	
}