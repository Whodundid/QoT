package envision.game.effects;

import envision.engine.loader.built.game.Effect;

public abstract class GenericSpeedEffect extends Effect {

    public GenericSpeedEffect(String name) {
        super(name, "SPEED_MODIFIER");
    }
    
    @Override
    public Object processEvent(Object... arguments) {
        return null;
    }
    
}
