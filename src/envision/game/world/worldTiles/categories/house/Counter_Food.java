package envision.game.world.worldTiles.categories.house;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.doodads.house.HouseTextures;

public class Counter_Food extends WorldTile {
	
	public Counter_Food() {
		super(TileIDs.COUNTER_FOOD);
		setTexture(HouseTextures.counter_food);
		blocksMovement = true;
	}
	
}