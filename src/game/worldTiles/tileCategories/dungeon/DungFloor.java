package game.worldTiles.tileCategories.dungeon;

import assets.textures.WorldTextures;
import game.worldTiles.TileIDs;
import game.worldTiles.WorldTile;

public class DungFloor extends WorldTile {
	
	public DungFloor() {
		super(TileIDs.DUNG_FLOOR);
		setTexture(WorldTextures.dungFloor);
	}
	
}