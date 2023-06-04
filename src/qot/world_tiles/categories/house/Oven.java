package qot.world_tiles.categories.house;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.world_tiles.TileIDs;

public class Oven extends WorldTile {
	
	public Oven() {
		super(TileIDs.OVEN);
		setTexture(HouseTextures.oven);
		blocksMovement = true;
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Oven());
	}
	
}
