package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class LeafyGrass extends WorldTile {
	
	public LeafyGrass() { this(-1); }
	public LeafyGrass(int id) {
		super(TileIDs.LEAFY_GRASS, id);
		numVariants = GrassTextures.leafy_grass.getChildren().size();
        randomizeValues();
        setMiniMapColor(0xff504018);
    }
    
    @Override
    public void randomizeValues() {
        //rotation = Rotation.random();
        
        if (meta < 0) {
            setSprite(new Sprite(GrassTextures.leafy_grass.getRandVariant()));
        }
        else {
            setSprite(new Sprite(GrassTextures.leafy_grass.getChild(meta)));
        }
    }
	
    @Override public boolean hasVariation() { return true; }
	
	@Override
	public WorldTile copy() {
		return copyFields(this, new LeafyGrass());
	}
	
}
