package qot.world_tiles.categories.nature.rock;

import envision.engine.loader.built.game.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class Stone extends WorldTile {
    
    public Stone() {
        super(TileIDs.STONE);
        setSprite(new Sprite(StoneFloorTextures.stone_pad));
        setBlocksMovement(true);
        minTileHeight = 0.25f;
        maxTileHeight = 0.75f;
        randomizeRotation = true;
        randomizeTileHeight = true;
        
        randomizeValues();
        setMiniMapColor(0xff707070);
    }
    
    @Override
    public WorldTile copy() {
        return copyFields(this, new Stone());
    }
    
}
