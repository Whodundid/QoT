package qot.world_tiles.categories.nature;

import envision.engine.loader.built.game.Sprite;
import envision.game.world.worldTiles.WorldTile;
import qot.assets.textures.world.nature.NatureTextures;
import qot.world_tiles.TileIDs;

public class Mud extends WorldTile {
    
    public Mud() {
        super(TileIDs.MUD);
        setSprite(new Sprite(NatureTextures.mud));
        tileHeight = -0.025f;
        setMiniMapColor(0xff653617);
    }
    
    @Override
    public WorldTile copy() {
        return copyFields(this, new Mud());
    }
    
}
