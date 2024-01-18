package qot.world_tiles.categories.nature;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class LightGrass extends WorldTile {
	
	public LightGrass() { this(-1); }
	public LightGrass(int id) {
		super(TileIDs.LIGHT_GRASS, id);
		numVariants = GrassTextures.light_grass.getChildren().size();
		
        randomizeValues();
        this.setMiniMapColor(0xff51C12C);
    }
    
    @Override
    public void randomizeValues() {
        if (meta < 0) {
            setSprite(new Sprite(GrassTextures.light_grass.getRandVariant()));
        }
        else {
            setSprite(new Sprite((GrassTextures.light_grass.getChild(meta))));
        }
    }
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new LightGrass());
	}
	
}
