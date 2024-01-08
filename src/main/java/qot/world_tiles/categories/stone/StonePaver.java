package qot.world_tiles.categories.stone;

import envision.engine.resourceLoaders.Sprite;
import envision.game.world.worldTiles.WorldTile;
import eutil.random.ERandomUtil;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class StonePaver extends WorldTile {
    
    public StonePaver() { this(-1); }
    public StonePaver(int id) {
        super(TileIDs.STONE_PAVER, id);
        
        numVariants = StoneFloorTextures.stone_paver.getChildren().size();
        
        randomizeValues();
        setMiniMapColor(0xff686868);
    }
    
    @Override
    public void randomizeValues() {
        drawFlipped = ERandomUtil.randomBool();
        
        if (meta < 0) {
            setSprite(new Sprite(StoneFloorTextures.stone_paver.getRandVariant()));
        }
        else {
            setSprite(new Sprite(StoneFloorTextures.stone_paver.getChild(meta)));
        }
    }
    
    @Override public boolean hasVariation() { return true; }
    
    @Override
    public WorldTile copy() {
        return copyFields(this, new StonePaver());
    }
    
}
