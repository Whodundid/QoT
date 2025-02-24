package qot.assets.textures;

import envision.engine.internal.rendering.textureSystem.TextureSystem;
import qot.assets.TextureLoader;
import qot.assets.textures.ability.AbilityTextures;
import qot.assets.textures.doodads.DoodadTextures;
import qot.assets.textures.effects.EffectsTextures;
import qot.assets.textures.entity.EntityTextures;
import qot.assets.textures.general.GeneralTextures;
import qot.assets.textures.item.ItemTextures;
import qot.assets.textures.world.WorldTextures;

/** Contains mappings to all of QoT's textures. */
@Deprecated
public class GameTextures extends TextureLoader {
    
    //====================
    // Singleton Instance
    //====================
    
    private static final GameTextures t = new GameTextures();
    public static GameTextures instance() { return t; }
    
    // Hide constructor
    private GameTextures() {}
    
    //-------------------------------
    
    public static AbilityTextures abilityTextures = AbilityTextures.instance();
    //public static CursorTextures cursorTextures = CursorTextures.instance();
    public static DoodadTextures doodadTextures = DoodadTextures.instance();
    public static EffectsTextures effectsTextures = EffectsTextures.instance();
    public static EntityTextures entityTextures = EntityTextures.instance();
    public static GeneralTextures generalTextures = GeneralTextures.instance();
    public static ItemTextures itemTextures = ItemTextures.instance();
    public static WorldTextures worldTextures = WorldTextures.instance();
    
    //-------------------------------    
    //===========
    // Overrides
    //===========
    
    @Override
    public void onRegister(TextureSystem sys) {
        abilityTextures.onRegister(sys);
        //cursorTextures.onRegister(sys);
        doodadTextures.onRegister(sys);
        effectsTextures.onRegister(sys);
        entityTextures.onRegister(sys);
        generalTextures.onRegister(sys);
        itemTextures.onRegister(sys);
        worldTextures.onRegister(sys);
    }
    
}
