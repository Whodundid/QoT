package envision.game.manager.rules;

public class Rule_PlayerInfiniteMana extends GameRule<Boolean> {

    public static final String NAME = "playerInfiniteMana";
    
    public Rule_PlayerInfiniteMana() {
        this(false);
    }
    
    public Rule_PlayerInfiniteMana(boolean initialValue) {
        super(NAME, initialValue);
    }
    
}
