package envision.game.effects;

import envision.engine.loader.built.game.Effect;

public abstract class GenericPoisonEffect extends Effect {

    public GenericPoisonEffect(String nameIn) {
        super(nameIn, "POISON_MODIFIER");
    }
    
}
