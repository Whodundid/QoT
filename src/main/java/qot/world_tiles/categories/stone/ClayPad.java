package qot.world_tiles.categories.stone;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class ClayPad extends WorldTile {
    
	public ClayPad() { this(-1); }
	public ClayPad(int id) {
		super(TileIDs.CLAY_PAD, id);
		
		meta = id;
		setWall(true);
		wallHeight = 0.1f;
		numVariants = StoneFloorTextures.clay_pad.getChildren().size();
		randomizeDrawFlipped = true;
		
		randomizeValues();
		setMiniMapColor(0xff744B35);
	}
	
	@Override
	public void randomizeValues() {
	    super.randomizeValues();
	    
        if (meta < 0) {
            setSprite(new Sprite(StoneFloorTextures.clay_pad.getRandVariant()));
        }
        else {
            setSprite(new Sprite(StoneFloorTextures.clay_pad.getChild(meta)));
        }
	}
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new ClayPad());
	}
	
}