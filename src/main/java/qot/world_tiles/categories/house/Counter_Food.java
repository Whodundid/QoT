package qot.world_tiles.categories.house;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.world_tiles.TileIDs;

public class Counter_Food extends WorldTile {
	
	public Counter_Food() {
		super(TileIDs.COUNTER_FOOD);
		setSprite(new Sprite(HouseTextures.counter_food));
		blocksMovement = true;
		setMiniMapColor(0xff6B7585);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Counter_Food());
	}
	
}