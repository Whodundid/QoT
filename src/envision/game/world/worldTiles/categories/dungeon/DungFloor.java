package envision.game.world.worldTiles.categories.dungeon;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.world.floors.stone.StoneFloorTextures;

public class DungFloor extends WorldTile {
	
	public DungFloor() {
		super(TileIDs.DUNG_FLOOR);
		setTexture(StoneFloorTextures.dung_floor);
	}
	
}