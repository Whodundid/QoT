package qot.world_tiles.categories.house;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.roofs.RoofTextures;
import qot.world_tiles.TileIDs;

public class Roof extends WorldTile {
    
	public Roof() {
		super(TileIDs.ROOF);
		setBlocksMovement(true);
		randomizeValues();
		setMiniMapColor(0xff5a5753);
	}
	
    @Override
    public void randomizeValues() {
        if (meta < 0) {
            setSprite(new Sprite(RoofTextures.gray_roof.getRandVariant()));
        }
        else {
            setSprite(new Sprite(RoofTextures.gray_roof.getChild(meta)));
        }
    }
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new Roof());
	}
	
}
