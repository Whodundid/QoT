package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.world.nature.rock.RockTextures;
import qot.world_tiles.TileIDs;

public class RockyStone extends WorldTile {
	
	public RockyStone() {
		super(TileIDs.ROCKY_STONE);
		setSprite(new Sprite(RockTextures.rocky_stone));
		setBlocksMovement(true);
		setWall(true);
        
        randomizeValues();
        setMiniMapColor(0xff7F7F7F);
    }
    
    @Override
    public void randomizeValues() {
        wallHeight = ERandomUtil.getRoll(0.25, 0.75);
        rotation = Rotation.random();
    }
	
    @Override public boolean hasVariation() { return true; }
    
	@Override
	public WorldTile copy() {
		return copyFields(this, new RockyStone());
	}
	
}
