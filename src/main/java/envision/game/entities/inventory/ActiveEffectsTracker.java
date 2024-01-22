package envision.game.entities.inventory;

import java.util.HashMap;
import java.util.Map;

import envision.game.effects.Effect;
import envision.game.entities.Entity;
import eutil.EUtil;
import eutil.datatypes.boxes.Box2;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;

public class ActiveEffectsTracker {
    
    //========
    // Fields
    //========
    
    private final Entity theEntity;
    
    private final BoxList<String, Integer> activeEffects = new BoxList<>();
    /** Keeps track of the time the effect was applied and the current update time. */
    private final Map<String, Long> effectUpdateTracker = new HashMap<>();
    private final Map<String, Effect> nameToEffectMap = new HashMap<>();
    
    private final EList<String> effectsToRemove = EList.newList();
    
    //==============
    // Constructors
    //==============
    
    public ActiveEffectsTracker(Entity entityIn) {
        theEntity = entityIn;
    }
    
    //=========
    // Methods
    //=========
    
    public void update(long dt) {
        effectsToRemove.clear();
        
        var effects = effectUpdateTracker.keySet();
        for (String effectName : effects) {
            Effect effect = nameToEffectMap.get(effectName);
            
            // effects with -1 duration (or < 0) have infinite durration and aren't removed by time
            if (effect.effectDuration < 0) continue;
            
            long updateTracker = effectUpdateTracker.get(effectName);
            updateTracker += dt;
            effectUpdateTracker.put(effectName, updateTracker);
            
            if (updateTracker >= effect.effectDuration) {
                effectsToRemove.add(effectName);
            }
        }
        
        for (String e : effectsToRemove) {
            removeEffect(e);
        }
    }
    
    /**
     * Adds one instance of the given effect to this tracker.
     * 
     * @param e
     */
    public void addEffect(Effect e) {
        if (e == null) return;
        var box = activeEffects.getBoxWithA(e.name);
        if (box == null) {
            box = new Box2<>(e.name, 0);
            effectUpdateTracker.put(e.name, 0L);
            nameToEffectMap.put(e.name, e);
            activeEffects.add(box);
        }
        box.setB(box.getB() + 1);
    }
    
    /**
     * Removes one instance of the given effect from this tracker.
     * 
     * @param e
     */
    public void removeEffect(Effect e) {
        if (e == null) return;
        var box = activeEffects.getBoxWithA(e.name);
        if (box == null) return;
        box.setB(box.getB() - 1);
        if (box.getB() <= 0) {
            activeEffects.remove(box);
            effectUpdateTracker.remove(e.name);
            nameToEffectMap.remove(e.name);
        }
    }
    
    public void removeEffect(String effectName) {
        if (effectName == null || effectName.isEmpty()) return;
        var box = activeEffects.getBoxWithA(effectName);
        if (box == null) return;
        box.setB(box.getB() - 1);
        if (box.getB() <= 0) {
            activeEffects.remove(box);
            effectUpdateTracker.remove(effectName);
            nameToEffectMap.remove(effectName);
        }
    }
    
    /**
     * Removes ALL instances of the given effects.
     * 
     * @param e
     */
    public void clenseEffect(Effect e) {
        if (e == null) return;
        var box = activeEffects.getBoxWithA(e.name);
        if (box == null) return;
        activeEffects.remove(box);
        effectUpdateTracker.remove(e.name);
        nameToEffectMap.remove(e.name);
    }
    
    /**
     * Removes ALL instances of the given effects.
     * 
     * @param effectName
     */
    public void clenseEffect(String effectName) {
        if (effectName == null) return;
        var box = activeEffects.getBoxWithA(effectName);
        if (box == null) return;
        activeEffects.remove(box);
        effectUpdateTracker.remove(effectName);
        nameToEffectMap.remove(effectName);
    }
    
    public boolean hasEffect(Effect e) {
        if (e == null) return false;
        return activeEffects.getBoxWithA(e.name) != null;
    }
    
    public boolean hasEffect(String effectName) {
        if (effectName == null || effectName.isEmpty()) return false;
        return activeEffects.containsA(effectName);
    }
    
    public boolean hasEffectType(String effectType) {
        if (effectType == null || effectType.isEmpty()) return false;
        for (String effectName : activeEffects.getAVals()) {
            Effect e = nameToEffectMap.get(effectName);
            if (EUtil.isEqual(effectType, e.getEffectType())) return true;
        }
        return false;
    }
    
    public EList<Effect> getAllEffects() {
        return EList.of(nameToEffectMap.values());
    }
    
    public EList<String> getAllEffectTypes() {
        EList<String> types = EList.newList();
        for (String effectName : activeEffects.getAVals()) {
            Effect effect = nameToEffectMap.get(effectName);
            String effectType = effect.getEffectType();
            types.addIfNotContains(effectType);
        }
        return types;
    }
    
    public int getEffectTotal(Effect e) {
        if (e == null) return 0;
        var box = activeEffects.getBoxWithA(e.name);
        if (box == null) return 0;
        return box.getB();
    }
    
    public EList<Effect> getAllEffectsOfType(String effectType) {
        EList<Effect> r = EList.newList();
        if (effectType == null || effectType.isEmpty()) return r;
        for (String effectName : activeEffects.getAVals()) {
            Effect effect = nameToEffectMap.get(effectName);
            if (EUtil.isEqual(effectType, effect.getEffectType())) r.add(effect);
        }
        return r;
    }
    
    public double getEffectTypeTotal(String effectType) {
        double total = 0.0;
        if (effectType == null || effectType.isEmpty()) return total;
        for (String effectName : activeEffects.getAVals()) {
            Effect effect = nameToEffectMap.get(effectName);
            if (EUtil.isEqual(effectType, effect.getEffectType())) total += effect.getEffectValue();
        }
        return total;
    }
    
}
