package envision.game.manager.rules;

public class Rule_PlayerNoClip extends GameRule<Boolean> {

    public static final String NAME = "playerNoClip";
    
    public Rule_PlayerNoClip() {
        this(false);
    }
    
    public Rule_PlayerNoClip(boolean initialValue) {
        super(NAME, initialValue);
    }
    
}
