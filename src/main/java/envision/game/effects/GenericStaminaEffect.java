package envision.game.effects;

import envision.engine.loader.built.game.Effect;

public abstract class GenericStaminaEffect extends Effect {

    public GenericStaminaEffect(String nameIn) {
        super(nameIn, "STAMINA_MODIFIER");
    }
    
    @Override
    public Integer processEvent(Object... arguments) {
        return null;
    }
    
}
