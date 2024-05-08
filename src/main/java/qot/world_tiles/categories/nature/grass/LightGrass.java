package qot.world_tiles.categories.nature.grass;

import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class LightGrass extends WorldTile {
	
	public LightGrass() { this(-1); }
	public LightGrass(int id) {
		super(TileIDs.LIGHT_GRASS, id);
		numVariants = 4;
		
        randomizeValues();
        this.setMiniMapColor(0xff51C12C);
    }
    
    @Override
    public void randomizeValues() {
        if (meta < 0) {
            setSprite(GrassTextures.grassSheet.getRandom(10, 13));
        }
        else {
            setSprite(GrassTextures.grassSheet.getSprite(10 + meta));
        }
    }
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new LightGrass());
	}
	
}
