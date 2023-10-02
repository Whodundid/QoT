package envision.game.manager.rules;

public class GameRule<T> {
    
    public static final GameRule<Void> DEFAULT_RULE = new GameRule<>("DEFAULT_RULE");
    
    private final String ruleName;
    private boolean isEnabled = true;
    private T value;
    private T minValue;
    private T maxValue;
    
    public GameRule(String ruleNameIn) { this(ruleNameIn, null); }
    public GameRule(String ruleNameIn, T valueIn) {
        ruleName = ruleNameIn;
        value = valueIn;
    }
    
    public boolean isEnabled() { return isEnabled; }
    public GameRule<T> setEnabled(boolean value) { isEnabled = value; return this; }
    
    public boolean isTrue() { return Boolean.TRUE.equals(value); }
    public boolean isFalse() { return Boolean.FALSE.equals(value); }
    
    public String getRuleName() { return ruleName; }
    public T getValue() { return value; }
    public T getMinValue() { return minValue; }
    public T getMaxValue() { return maxValue; }
    
    public T setValue(T valueIn) {
        value = valueIn;
        return value;
    }

    public void setMinValue(T valueIn) { minValue = valueIn; }
    public void setMaxValue(T valueIn) { maxValue = valueIn; }
    public void setRange(T minValueIn, T maxValueIn) {
        setMinValue(minValueIn);
        setMaxValue(maxValueIn);
    }
    
    public T setValue(T valueIn, T minValueIn, T maxValueIn) {
        setRange(minValueIn, maxValueIn);
        setValue(valueIn);
        
        return value;
    }
    
}
