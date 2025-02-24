package qot.assets.textures.world.floors.stone;

import envision.engine.loader.built.game.GameTexture;

public class ClayPad extends GameTexture {
    
    private static final String stoneFloorDir = tDir + "world\\floors\\stone\\";
    
    public ClayPad() {
        super(stoneFloorDir, "clay_pad_0.png");
        
        addChild(stoneFloorDir, "clay_pad_1.png");
    }
    
}
