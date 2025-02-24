package qot.world_tiles.categories.nature;

import envision.engine.loader.built.game.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.sand.SandTextures;
import qot.world_tiles.TileIDs;

public class Sand extends WorldTile {
    
    public Sand() {
        super(TileIDs.SAND);
        setSprite(new Sprite(SandTextures.sand));
        tileHeight = 0.025f;
        setMiniMapColor(0xffD8B668);
    }
    
    @Override
    public WorldTile copy() {
        return copyFields(this, new Sand());
    }
    
}
