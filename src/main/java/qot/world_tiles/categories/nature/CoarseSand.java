package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.sand.SandTextures;
import qot.world_tiles.TileIDs;

public class CoarseSand extends WorldTile {
    
    public CoarseSand() { this(-1); }
    public CoarseSand(int id) {
        super(TileIDs.COARSE_SAND);
        numVariants = 3;
        
        randomizeValues();
        setMiniMapColor(0xffE5D5A4);
    }
    
    @Override
    public void randomizeValues() {
        if (meta < 0) {
            setSprite(new Sprite(SandTextures.coarse_sand.getRandVariant()));
        }
        else {
            setSprite(new Sprite(SandTextures.coarse_sand.getChild(meta)));
        }
    }
    
    @Override public boolean hasVariation() { return true; }
    
    @Override
    public WorldTile copy() {
        return copyFields(this, new CoarseSand());
    }
    
}
