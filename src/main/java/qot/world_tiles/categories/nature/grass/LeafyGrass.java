package qot.world_tiles.categories.nature.grass;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class LeafyGrass extends WorldTile {
    
    public LeafyGrass() { this(-1); }
    public LeafyGrass(int id) {
        super(TileIDs.LEAFY_GRASS, id);
        numVariants = 2;
        randomizeValues();
        setMiniMapColor(0xff605828);
    }
    
    @Override
    public void randomizeValues() {
        if (meta < 0) {
            setSprite(GrassTextures.grassSheet.getRandom(8, 9));
        }
        else {
            setSprite(GrassTextures.grassSheet.getSprite(8 + meta));
        }
    }
    
    @Override
    public WorldTile copy() {
        return copyFields(this, new LeafyGrass());
    }
    
}
