package world.worldTiles.categories.dungeon;

import assets.textures.WorldTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class DungFloor extends WorldTile {
	
	public DungFloor() {
		super(TileIDs.DUNG_FLOOR);
		setTexture(WorldTextures.dungFloor);
	}
	
}