package envision.game.world.worldTiles.categories.house;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.doodads.house.HouseTextures;

public class Counter extends WorldTile {
	
	public Counter() {
		super(TileIDs.COUNTER);
		setTexture(HouseTextures.counter);
		blocksMovement = true;
	}
	
}