package qot.world_tiles.categories.nature;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.grass.GrassTextures;
import qot.world_tiles.TileIDs;

public class DryGrass extends WorldTile {
    
    public DryGrass() { this(-1); }
    public DryGrass(int id) {
        super(TileIDs.DRY_GRASS, id);
        setSideSprite(new Sprite(GrassTextures.dry_grass));
        numVariants = GrassTextures.dry_grass.getChildren().size();
        randomizeValues();
        setMiniMapColor(0xff4B9924);
    }
    
    @Override
    public void randomizeValues() {
        if (meta < 0) {
            setSprite(new Sprite(GrassTextures.dry_grass.getRandVariant()));
        }
        else {
            setSprite(new Sprite(GrassTextures.dry_grass.getChild(meta)));
        }
    }
    
    @Override public boolean hasVariation() { return true; }
    
    @Override
    public WorldTile copy() {
        return copyFields(this, new DryGrass());
    }
    
}
