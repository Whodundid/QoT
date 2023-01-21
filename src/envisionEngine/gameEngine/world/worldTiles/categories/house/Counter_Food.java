package envisionEngine.gameEngine.world.worldTiles.categories.house;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.doodads.house.HouseTextures;

public class Counter_Food extends WorldTile {
	
	public Counter_Food() {
		super(TileIDs.COUNTER_FOOD);
		setTexture(HouseTextures.counter_food);
		blocksMovement = true;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Counter_Food());
	}
	
}