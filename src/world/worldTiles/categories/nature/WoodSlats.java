package world.worldTiles.categories.nature;

import assets.textures.world.floors.wood.WoodFloorTextures;
import world.worldTiles.TileIDs;
import world.worldTiles.WorldTile;

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
	
}
