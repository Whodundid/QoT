package envision.game.effects;

public class OutOfStaminaEffect extends GenericStaminaEffect {
    
    public static final String EFFECT_NAME = "Out of Stamina";
    
    public OutOfStaminaEffect() {
        super(EFFECT_NAME);
        this.effectDuration = 1500;
    }
    
}
