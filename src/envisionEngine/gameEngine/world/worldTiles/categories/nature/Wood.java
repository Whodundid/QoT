package envisionEngine.gameEngine.world.worldTiles.categories.nature;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.wood.WoodFloorTextures;

public class Wood extends WorldTile {
	
	public Wood() {
		super(TileIDs.WOOD);
		setTexture(WoodFloorTextures.wood_floor);
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Wood());
	}
	
}