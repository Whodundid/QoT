package qot.world_tiles.categories.stone;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import eutil.random.ERandomUtil;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class ClayPad extends WorldTile {
    
	public ClayPad() { this(-1); }
	public ClayPad(int id) {
		super(TileIDs.CLAY_PAD, id);
		
		meta = id;
		setWall(true);
		wallHeight = 0.1;
		numVariants = StoneFloorTextures.clay_pad.getChildren().size();
		
		randomizeValues();
		setMiniMapColor(0xff744B35);
	}
	
	@Override
	public void randomizeValues() {
	    //rotation = Rotation.random();
        drawFlipped = ERandomUtil.randomBool();
        
        if (meta < 0) {
            setSprite(new Sprite(StoneFloorTextures.clay_pad.getRandVariant()));
        }
        else {
            setSprite(new Sprite(StoneFloorTextures.clay_pad.getChild(meta)));
        }
	}
	
	@Override public boolean hasVariation() { return true; }
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new ClayPad());
	}
	
}