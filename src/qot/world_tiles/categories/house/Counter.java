package qot.world_tiles.categories.house;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.world_tiles.TileIDs;

public class Counter extends WorldTile {
	
	public Counter() {
		super(TileIDs.COUNTER);
		setTexture(HouseTextures.counter);
		blocksMovement = true;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Counter());
	}
	
}