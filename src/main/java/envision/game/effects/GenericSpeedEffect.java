package envision.game.effects;

public abstract class GenericSpeedEffect extends Effect {

    public GenericSpeedEffect(String name) {
        super(name, "SPEED_MODIFIER");
    }
    
    @Override
    public Object processEvent(Object... arguments) {
        return null;
    }
    
}
