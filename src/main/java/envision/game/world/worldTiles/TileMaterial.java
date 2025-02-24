package envision.game.world.worldTiles;

/** Tile material indicates what kind of sound is played when the tile is stepped on. */
public class TileMaterial {
    
    //========
    // Fields
    //========
    
    private String materialName;
    
    /** 0 by default -- does not emit light. */
    public float luminosity = 0.0f;
    /** This material's coefficient of friction. */
    public float friction = 1.0f;
    /** The ratio of much light this material lets through. */
    public float translucency = 0.0f;
    
    private String walkSoundEffectName;
    
    private String walkParticleEffectName;
    
    //=========
    // Getters
    //=========
    
    public String getMaterialName() { return materialName; }
    
}
