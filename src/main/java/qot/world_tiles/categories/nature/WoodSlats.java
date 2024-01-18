package qot.world_tiles.categories.nature;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.wood.WoodFloorTextures;
import qot.world_tiles.TileIDs;

public class WoodSlats extends WorldTile {
	
	public WoodSlats() { this(-1); }
	public WoodSlats(int id) {
		super(TileIDs.WOOD_SLATS, id);
		numVariants = WoodFloorTextures.wood_slats.getChildren().size();
		
		randomizeValues();
		setMiniMapColor(0xff5A3A0E);
	}
	
	@Override
	public void randomizeValues() {
	    if (meta < 0) {
            setSprite(new Sprite(WoodFloorTextures.wood_slats.getRandVariant()));
        }
        else {
            setSprite(new Sprite(WoodFloorTextures.wood_slats.getChild(meta)));
        }
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new WoodSlats());
	}
	
}
