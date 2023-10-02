package qot.world_tiles.categories.nature;

import envision.engine.rendering.textureSystem.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.wood.WoodFloorTextures;
import qot.world_tiles.TileIDs;

public class WoodSlats extends WorldTile {
	
	public WoodSlats() { this(-1); }
	public WoodSlats(int id) {
		super(TileIDs.WOOD_SLATS);
		numVariants = WoodFloorTextures.wood_slats.getChildren().size();
		
		if (id < 0) {
		    setSprite(new Sprite(WoodFloorTextures.wood_slats.getRandVariant()));
		}
		else {
		    setSprite(new Sprite(WoodFloorTextures.wood_slats.getChild(id)));
		}
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new WoodSlats());
	}
	
}
