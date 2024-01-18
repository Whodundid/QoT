package qot.world_tiles.categories.nature;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.rock.RockTextures;
import qot.world_tiles.TileIDs;

public class RockyStone extends WorldTile {
	
	public RockyStone() {
		super(TileIDs.ROCKY_STONE);
		setSprite(new Sprite(RockTextures.rocky_stone));
		setBlocksMovement(true);
		setWall(true);
		minWallHeight = 0.25f;
        maxWallHeight = 0.75f;
        randomizeRotation = true;
        randomizeWallHeight = true;
        
        randomizeValues();
        setMiniMapColor(0xff7F7F7F);
    }
    
	@Override
	public WorldTile copy() {
		return copyFields(this, new RockyStone());
	}
	
}
