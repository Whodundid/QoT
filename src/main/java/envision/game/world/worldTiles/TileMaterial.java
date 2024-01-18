package envision.game.world.worldTiles;

/** Tile material indicates what kind of sound is played when the tile is stepped on. */
public class TileMaterial {
	
    //========
    // Fields
    //========
    
    private String materialName;
    private String walkSoundEffectName;
    private String walkParticleEffectName;
    private float materialFriction;
    private float movementModifier;
    
    //=========
    // Getters
    //=========
	
    public String getMaterialName() { return materialName; }
    
}
