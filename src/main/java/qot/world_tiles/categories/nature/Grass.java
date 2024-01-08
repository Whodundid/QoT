package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
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
        //rotation = Rotation.random();
        
        if (meta  < 0) {
            setSprite(new Sprite(GrassTextures.grass.getRandVariant()));
        }
        else {
            setSprite(new Sprite(GrassTextures.grass.getChild(meta)));
        }
    }
	
    @Override public boolean hasVariation() { return true; }
    
	@Override
	public WorldTile copy() {
		return copyFields(this, new Grass());
	}
	
}
