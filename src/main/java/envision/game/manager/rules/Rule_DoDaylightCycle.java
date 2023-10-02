package envision.game.manager.rules;

public class Rule_DoDaylightCycle extends GameRule<Boolean> {

    public static final String NAME = "doDaylightCycle";
    
    public Rule_DoDaylightCycle() {
        this(true);
    }
    
    public Rule_DoDaylightCycle(boolean initialValue) {
        super(NAME, initialValue);
    }
    
}
