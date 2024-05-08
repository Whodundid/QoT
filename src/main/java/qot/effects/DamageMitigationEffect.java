package qot.effects;

import envision.game.effects.GenericDefenseEffect;

public class DamageMitigationEffect extends GenericDefenseEffect {
    
    public DamageMitigationEffect(String nameIn) {
        super(nameIn);
    }
    
    public DamageMitigationEffect(String nameIn, double value) {
        super(nameIn);
        this.setEffectValue(value);
    }

    @Override
    public Integer processEvent(Object... arguments) {
        if (arguments == null || arguments.length == 0) return null;
        
        int damageIn = ((Number) (arguments[0])).intValue();
        int rDamage = (int) (damageIn * getEffectValue());
        
        return rDamage;
    }
    
}
