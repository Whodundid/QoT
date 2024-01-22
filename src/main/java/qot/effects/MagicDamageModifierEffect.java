package qot.effects;

import envision.game.effects.GenericMagicEffect;

public class MagicDamageModifierEffect extends GenericMagicEffect {

    public MagicDamageModifierEffect(String nameIn, double value) {
        super(nameIn);
        this.setEffectValue(value);
    }
    
    
    
}
