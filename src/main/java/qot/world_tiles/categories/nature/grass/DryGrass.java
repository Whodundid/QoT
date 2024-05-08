package qot.world_tiles.categories.nature.grass;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class DryGrass extends WorldTile {
    
    public DryGrass() { this(-1); }
    public DryGrass(int id) {
        super(TileIDs.DRY_GRASS, id);
        setSideSprite(GrassTextures.grassSheet.getSprite(0));
        numVariants = 5;
        randomizeValues();
        setMiniMapColor(0xff4B9924);
    }
    
    @Override
    public void randomizeValues() {
        if (meta < 0) {
            setSprite(GrassTextures.grassSheet.getRandom(0, 4));
        }
        else {
            setSprite(GrassTextures.grassSheet.getSprite(0 + meta));
        }
    }
    
    @Override
    public WorldTile copy() {
        return copyFields(this, new DryGrass());
    }
    
}
