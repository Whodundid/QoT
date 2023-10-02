package envision.game.manager.rules;

public class Rule_GodMode extends GameRule<Boolean> {

    public static final String NAME = "godMode";
    
    public Rule_GodMode() {
        this(false);
    }
    
    public Rule_GodMode(boolean initialValue) {
        super(NAME, initialValue);
    }
    
}
