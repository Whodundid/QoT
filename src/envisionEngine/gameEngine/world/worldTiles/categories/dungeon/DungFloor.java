package envision.gameEngine.world.worldTiles.categories.dungeon;

import envision.gameEngine.world.worldTiles.TileIDs;
import envision.gameEngine.world.worldTiles.WorldTile;
import game.assets.textures.world.floors.stone.StoneFloorTextures;

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