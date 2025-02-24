package qot.world_tiles.categories.nature.rock;

import envision.engine.loader.built.game.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.rock.RockTextures;
import qot.world_tiles.TileIDs;

public class RockyStone extends WorldTile {
    
    public RockyStone() {
        super(TileIDs.ROCKY_STONE);
        setSprite(new Sprite(RockTextures.rocky_stone));
        setBlocksMovement(true);
        minTileHeight = 0.25f;
        maxTileHeight = 0.75f;
        randomizeRotation = true;
        randomizeTileHeight = true;
        
        randomizeValues();
        setMiniMapColor(0xff7F7F7F);
    }
    
    @Override
    public WorldTile copy() {
        return copyFields(this, new RockyStone());
    }
    
}
