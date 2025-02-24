package qot.assets.textures.entity;

import envision.engine.internal.rendering.textureSystem.TextureSystem;
import envision.engine.loader.built.game.GameTexture;
import envision.engine.loader.built.game.SpriteSheet;
import qot.assets.TextureLoader;

public class EntityTextures extends TextureLoader {
    
    //====================
    // Singleton Instance
    //====================
    
    private static final EntityTextures t = new EntityTextures();
    public static EntityTextures instance() { return t; }
    
    // Hide constructor
    private EntityTextures() {}
    
    //-------------------------------
    
    private static final String textureDir = tDir + "entities\\";
    
    //==========
    // Textures
    //==========
    
    public static final GameTexture
    
    goblin = new GameTexture(textureDir, "goblin/goblin_base.png"),
    player = new GameTexture(textureDir, "player/player_base.png"),
    whodundid = new GameTexture(textureDir, "whodundid/whodundid_base.png"),
    trollboar = new GameTexture(textureDir, "tollboar/tollboar_base.png"),
    thyrah = new GameTexture(textureDir, "thyrah/thyrah_base.png"),
    
    whobro = new GameTexture(textureDir + "whodundidsbrother\\", "whodundidsbrother_base.png"),
    whobro1 = new GameTexture(textureDir + "whodundidsbrother\\", "whodundidsbrother_base2.png"),
    whobro2 = new GameTexture(textureDir + "whodundidsbrother\\", "whodundidsbrother_base3.png"),
    whobro3 = new GameTexture(textureDir + "whodundidsbrother\\", "whodundidsbrother_base4.png"),
    
    whobro_blink0 = new GameTexture(textureDir + "whodundidsbrother\\", "whobro_blink0.png"),
    whobro_blink1 = new GameTexture(textureDir + "whodundidsbrother\\", "whobro_blink1.png"),
    whobro_blink2 = new GameTexture(textureDir + "whodundidsbrother\\", "whobro_blink2.png"),
    
    fireBall_projectile = new GameTexture(textureDir + "../projectiles/", "fireball.png"),
    arrow_projectile = new GameTexture(textureDir + "../projectiles/", "arrow.png"),
    
    shopkeepWalksheetTexture = new GameTexture(textureDir, "walksheet.png"),
    amyIdleWalkTexture = new GameTexture(textureDir, "amy/amy_idle_walk.png");
    
    //===============
    // Sprite Sheets
    //===============
    
    public static final SpriteSheet
    
    walksheet = new SpriteSheet(shopkeepWalksheetTexture, 64, 64, 36, 0),
    amyIdleWalkSheet = new SpriteSheet(amyIdleWalkTexture, 32, 32, 64, 0);    
    //===========
    // Overrides
    //===========
    
    @Override
    public void onRegister(TextureSystem sys) {
        reg(sys, goblin);
        reg(sys, player);
        reg(sys, whodundid);
        reg(sys, trollboar);
        reg(sys, thyrah);
        
        reg(sys, whobro);
        reg(sys, whobro1);
        reg(sys, whobro2);
        reg(sys, whobro3);
        
        reg(sys, whobro_blink0);
        reg(sys, whobro_blink1);
        reg(sys, whobro_blink2);
        
        reg(sys, fireBall_projectile);
        reg(sys, arrow_projectile);
        
        reg(sys, shopkeepWalksheetTexture);
        reg(sys, amyIdleWalkTexture);
        
        //----------------------------
        
        reg(sys, "walksheet", walksheet);
        reg(sys, "amy_idle_walk_sheet", amyIdleWalkSheet);
    }
    
}
