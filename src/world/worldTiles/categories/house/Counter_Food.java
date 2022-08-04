package world.worldTiles.categories.house;

import assets.textures.doodads.house.HouseTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class Counter_Food extends WorldTile {
	
	public Counter_Food() {
		super(TileIDs.COUNTER_FOOD);
		setTexture(HouseTextures.counter_food);
		blocksMovement = true;
	}
	
}