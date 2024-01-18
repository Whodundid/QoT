package qot.world_tiles.categories.stone;

import envision.engine.registry.types.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.floors.stone.StoneFloorTextures;
import qot.world_tiles.TileIDs;

public class StonePaver extends WorldTile {
    
    public StonePaver() { this(-1); }
    public StonePaver(int id) {
        super(TileIDs.STONE_PAVER, id);
        
        numVariants = StoneFloorTextures.stone_paver.getChildren().size();
        randomizeDrawFlipped = true;
        
        randomizeValues();
        setMiniMapColor(0xff686868);
    }
    
    @Override
    public void randomizeValues() {
        super.randomizeValues();
        
        if (meta < 0) {
            setSprite(new Sprite(StoneFloorTextures.stone_paver.getRandVariant()));
        }
        else {
            setSprite(new Sprite(StoneFloorTextures.stone_paver.getChild(meta)));
        }
    }
    
    @Override
    public WorldTile copy() {
        return copyFields(this, new StonePaver());
    }
    
}
