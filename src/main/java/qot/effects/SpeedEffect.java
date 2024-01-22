package qot.effects;

import envision.game.effects.GenericSpeedEffect;

public class SpeedEffect extends GenericSpeedEffect {
    
    public SpeedEffect(String name, double value) {
        super(name);
        this.setEffectValue(value);
    }
    
}
