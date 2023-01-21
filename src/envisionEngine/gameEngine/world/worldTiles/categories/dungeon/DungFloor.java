package envisionEngine.gameEngine.world.worldTiles.categories.dungeon;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;

public class DungFloor extends WorldTile {
	
	public DungFloor() {
		super(TileIDs.DUNG_FLOOR);
		setTexture(StoneFloorTextures.dung_floor);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new DungFloor());
	}
	
}