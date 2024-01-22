package qot.effects;

import envision.game.effects.GenericDefenseEffect;

public class DamageMitigationEffect extends GenericDefenseEffect {

    public DamageMitigationEffect(String nameIn, double value) {
        super(nameIn);
        this.setEffectValue(value);
    }
    
}
