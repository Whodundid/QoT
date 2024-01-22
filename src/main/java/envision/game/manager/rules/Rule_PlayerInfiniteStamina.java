package envision.game.manager.rules;

public class Rule_PlayerInfiniteStamina extends GameRule<Boolean> {

    public static final String NAME = "playerInfiniteStamina";
    
    public Rule_PlayerInfiniteStamina() {
        this(false);
    }
    
    public Rule_PlayerInfiniteStamina(boolean initialValue) {
        super(NAME, initialValue);
    }
    
}
