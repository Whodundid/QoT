package world.worldTiles.categories.nature;

import assets.textures.world.floors.wood.WoodFloorTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

public class Wood extends WorldTile {
	
	public Wood() {
		super(TileIDs.WOOD);
		setTexture(WoodFloorTextures.wood_floor);
	}
	
}