package world.worldTiles.categories.dungeon;

import assets.textures.world.floors.stone.StoneFloorTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class DungFloor extends WorldTile {
	
	public DungFloor() {
		super(TileIDs.DUNG_FLOOR);
		setTexture(StoneFloorTextures.dung_floor);
	}
	
}