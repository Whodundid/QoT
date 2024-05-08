package envision.game.effects;

public abstract class GenericMagicEffect extends Effect<Integer> {

    public GenericMagicEffect(String nameIn) {
        super(nameIn, "MAGIC_MODIFIER");
    }
    
    @Override
    public Integer processEvent(Object... arguments) {
        return null;
    }
    
}
