package envision.game.effects;

import envision.engine.loader.built.game.Effect;

public abstract class GenericDefenseEffect extends Effect<Integer> {

    public GenericDefenseEffect(String nameIn) {
        super(nameIn, "DEFENSE_MODIFIER");
    }
    
}
