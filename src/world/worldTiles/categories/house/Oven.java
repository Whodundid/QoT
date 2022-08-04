package world.worldTiles.categories.house;

import assets.textures.doodads.house.HouseTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class Oven extends WorldTile {
	
	public Oven() {
		super(TileIDs.OVEN);
		setTexture(HouseTextures.oven);
		blocksMovement = true;
	}
	
}
