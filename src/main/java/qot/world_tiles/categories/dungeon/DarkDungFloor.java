package qot.world_tiles.categories.dungeon;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class DarkDungFloor extends WorldTile {
	
	public DarkDungFloor() {
		super(TileIDs.DARK_DUNG_FLOOR);
		setSprite(new Sprite(StoneFloorTextures.dark_dung_floor));
		setMiniMapColor(0xff0B191A);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DarkDungFloor());
	}
	
}