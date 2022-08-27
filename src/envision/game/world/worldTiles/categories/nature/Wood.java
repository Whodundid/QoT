package envision.game.world.worldTiles.categories.nature;

import envision.game.world.worldTiles.TileIDs;
import envision.game.world.worldTiles.WorldTile;
import game.assets.textures.world.floors.wood.WoodFloorTextures;

public class Wood extends WorldTile {
	
	public Wood() {
		super(TileIDs.WOOD);
		setTexture(WoodFloorTextures.wood_floor);
	}
	
}