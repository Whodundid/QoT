package qot.world_tiles.categories.nature.grass;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class Grass extends WorldTile {
    
    public Grass() { this(-1); }
    public Grass(int id) {
        super(TileIDs.GRASS, id);
        numVariants = 3;
        randomizeValues();
        setMiniMapColor(0xff10A500);
    }
    
    @Override
    public void randomizeValues() {
        if (meta < 0) {
            setSprite(GrassTextures.grassSheet.getRandom(5, 7));
        }
        else {
            setSprite(GrassTextures.grassSheet.getRandom(5, 7));
        }
    }
    
    @Override
    public WorldTile copy() {
        return copyFields(this, new Grass());
    }
    
}
