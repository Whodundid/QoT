package envision.game.entities.personality;

public class EntityNeed {
    
    //========
    // Fields
    //========
    
    /** The name of what this entity 'needs'. */
    public String needName;
    /** IE: If this need is positive, then the lack of this will incur negative effects. */
    public boolean isPositive;
    /** The point at which this need will either start to have an effect on the entity. */
    public float threshold;    
    //==============
    // Constructors
    //==============
    
    public EntityNeed(String nameIn) { this(nameIn, true, 0.5f); }
    public EntityNeed(String nameIn, boolean isPositiveIn) { this(nameIn, isPositiveIn, 0.5f); }
    public EntityNeed(String nameIn, boolean isPositiveIn, float thresholdIn) {
        needName = nameIn;
        isPositive = isPositiveIn;
        threshold = thresholdIn;
    }    
    //=========
    // Getters
    //=========
    
    public String getName() { return needName; }
    public boolean getIsPositive() { return isPositive; }
    public float getThreshold() { return threshold; }
    
}
