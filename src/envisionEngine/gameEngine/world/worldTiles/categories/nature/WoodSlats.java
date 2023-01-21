package envisionEngine.gameEngine.world.worldTiles.categories.nature;

import envisionEngine.gameEngine.world.worldTiles.TileIDs;
import envisionEngine.gameEngine.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.wood.WoodFloorTextures;

public class WoodSlats extends WorldTile {
	
	public WoodSlats() { this(-1); }
	public WoodSlats(int id) {
		super(TileIDs.WOOD_SLATS);
		numVariants = WoodFloorTextures.wood_slats.getChildren().size();
		
		if (id < 0) {
			setTexture(WoodFloorTextures.wood_slats.getRandVariant());
		}
		else {
			setTexture(WoodFloorTextures.wood_slats.getChild(id));
		}
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new WoodSlats());
	}
	
}
