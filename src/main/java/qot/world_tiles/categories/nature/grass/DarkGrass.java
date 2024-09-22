package qot.world_tiles.categories.nature.grass;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class DarkGrass extends WorldTile {
    
    public DarkGrass() {
        super(TileIDs.DARK_GRASS);
        setSprite(GrassTextures.grassSheet.getSprite(15));
        setMiniMapColor(0xff007800);
    }
    
    @Override
    public WorldTile copy() {
        return copyFields(this, new DarkGrass());
    }
    
}
