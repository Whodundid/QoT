package envision.game.entities.combat;

import envision.game.entities.Entity;

public class KnockbackCalculator {
    
    public static double calculateKnockbackForce(Entity attacker, Entity target, AttackType attackType) {
        if (attacker == null || target == null || attackType == null) return 0.0;
        
        switch (attackType) {
        case MELEE:
            return 5.0;
        case RANGED:
            return 2.0;
        case MAGIC:
            return 0.5;
        default:
            break;
        }
        
        return 0.0;
    }
    
}
