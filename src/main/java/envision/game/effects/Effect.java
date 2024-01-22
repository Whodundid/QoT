package envision.game.effects;

public abstract class Effect {
	
	public String name;
	public long effectDuration = -1;
	public boolean permanent;
	private String effectType;
	private double value;
	
	public Effect(String nameIn, String effectTypeIn) {
	    name = nameIn;
	    effectType = effectTypeIn;
	}
	
	public String getEffectType() { return effectType; }
	public double getEffectValue() { return value; }
	public void setEffectValue(double value) { this.value = value; }
	
}
