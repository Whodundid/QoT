package envision.engine.loader.built.game;

import envision.engine.loader.dtos.IEngineResource;
import envision.engine.loader.dtos.game.EffectDTO;

public abstract class Effect<RETURN_TYPE> implements IEngineResource {
    
    //========
    // Fields
    //========
    
    public String name;
    public long effectDuration = -1;
    public boolean permanent;
    private String effectType;
    private double value;
    
    //==============
    // Constructors
    //==============
    
    public Effect(String nameIn, String effectTypeIn) {
        name = nameIn;
        effectType = effectTypeIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public EffectDTO toDto() {
        return null;
    }
    
    //=========
    // Methods
    //=========
    
    public boolean isProcessable() { return false; }
    public RETURN_TYPE processEvent(Object... arguments) { return null; }
    
    protected boolean checkArgs(Object... arguments) {
        return arguments != null && arguments.length > 0;
    }
    
    //=========
    // Getters
    //=========
    
    public String getEffectType() { return effectType; }
    public double getEffectValue() { return value; }
    public void setEffectValue(double value) { this.value = value; }
    
    protected byte getByte(Object... arguments) { return ((Number) arguments[0]).byteValue(); }
    protected short getShort(Object... arguments) { return ((Number) arguments[0]).shortValue(); }
    protected int getInt(Object... arguments) { return ((Number) arguments[0]).intValue(); }
    protected long getLong(Object... arguments) { return ((Number) arguments[0]).longValue(); }
    protected float getFloat(Object... arguments) { return ((Number) arguments[0]).intValue(); }
    protected double getDouble(Object... arguments) { return ((Number) arguments[0]).doubleValue(); }
    
    protected byte getByte(int index, Object... arguments) { return ((Number) arguments[index]).byteValue(); }
    protected short getShort(int index, Object... arguments) { return ((Number) arguments[index]).shortValue(); }
    protected int getInt(int index, Object... arguments) { return ((Number) arguments[index]).intValue(); }
    protected long getLong(int index, Object... arguments) { return ((Number) arguments[index]).longValue(); }
    protected float getFloat(int index, Object... arguments) { return ((Number) arguments[index]).intValue(); }
    protected double getDouble(int index, Object... arguments) { return ((Number) arguments[index]).doubleValue(); }
    
    protected <T> T getArg(int index, Object... arguments) {
        return (T) arguments[index];
    }
    
}
