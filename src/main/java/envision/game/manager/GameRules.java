package envision.game.manager;

import java.util.HashMap;
import java.util.Map;

import envision.game.manager.rules.GameRule;
import eutil.EUtil;
import eutil.datatypes.util.EList;

public class GameRules {
    
    //============
    // Game Rules
    //============
    
    /** Controls whether or not the world transitions from day/night. */
    private boolean doDaylightCycle = true;
    /** Specifies whether or not the player character is in god mode or not. */
    private boolean godMode = false;
    /** Specified whether or not the player character can actively no clip through terrain. */
    private boolean playerNoClip = false;
    /** True if the player has infinite mana. */
    private boolean infiniteMana = false;
    /** True if the player has infinite stamina. */
    private boolean infiniteStamina = false;
    
    private final Map<String, GameRule<?>> rules = new HashMap<>();
    
    //=========
    // Methods
    //=========
    
    public void addRule(GameRule<?> rule) {
        if (rule == null) return;
        rules.put(rule.getRuleName(), rule);
    }
    
    public void removeRule(GameRule<?> rule) {
        if (rule == null) return;
        rules.remove(rule.getRuleName());
    }
    
    public int getRuleCount() {
        return rules.size();
    }
    
    public boolean containsRule(String ruleIn) {
        return rules.containsKey(ruleIn);
    }
    
    public boolean containsRule(GameRule<?> rule) {
        return rules.containsValue(rule);
    }
    
    public GameRule<?> getRule(String ruleName) {
        return rules.get(ruleName);
    }
    
    //=========
    // Getters
    //=========
    
    /**
     * Returns true if there is both an existing rule under the name in this
     * rule set and if that rule is currently enabled.
     * 
     * @param ruleName The name of the rule
     * 
     * @return True if a rule within this rule set under the given name is
     *         enabled
     */
    public boolean isRuleEnabled(String ruleName) {
        GameRule<?> rule = getRule(ruleName);
        return rule != null && rule.isEnabled();
    }
    
    /**
     * Returns the value of a rule under the given name within this rule set.
     * If there isn't a rule with that name, then the default value is returned
     * instead. Furthermore, if the given defaultValue doesn't match the given
     * datatype then the default value is still returned and an error stack
     * trace is thrown.
     * 
     * @param <T>          The underlying datatype of the backing rule
     * @param ruleName     The name of the rule
     * @param defaultValue A value to return if this rule set either doesn't
     *                     contain the given rule or if something goes wrong
     *                     while trying to retrieve the rule's value
     *                     
     * @return The value of a rule in this rule set under the given name
     */
    public <T> T getRuleValue(String ruleName, T defaultValue) {
        GameRule<?> rule = getRule(ruleName);
        if (rule == null) return defaultValue;
        try {
            GameRule<T> castedRule = (GameRule<T>) rule;
            return castedRule.getValue();
        }
        catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
    
    /**
     * Returns true if this rule set contains the given rule (by name) and it
     * has the given value. False is returned in any other case.
     * 
     * @param <T>      The underlying datatype of the backing rule
     * @param ruleName The name of the rule
     * @param value    The value of the rule to check for
     * 
     * @return True if there is a rule under the given name and its value
     *         matches the one given
     */
    public <T> boolean isRuleEnabledAndEquals(String ruleName, T value) {
        GameRule<?> rule = getRule(ruleName);
        if (rule == null) return false;
        try {
            GameRule<T> castedRule = (GameRule<T>) rule;
            T ruleValue = castedRule.getValue();
            return EUtil.isEqual(ruleValue, value);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean doDaylightCycle() { return doDaylightCycle; }
    public boolean godMode() { return godMode; }
    public boolean playerNoClip() { return playerNoClip; }
    
    public Map<String, GameRule<?>> getInternalRuleMap() { return rules; }
    
    /** Returns a copy of this list of game rules. */
    public EList<GameRule<?>> getRules() { return EList.of(rules.values()); }
    
    //=========
    // Setters
    //=========
    
    public void doDaylightCycle(boolean val) { doDaylightCycle = val; }
    public void godMode(boolean val) { godMode = val; }
    public void playerNoClip(boolean val) { playerNoClip = val; }
    
}
