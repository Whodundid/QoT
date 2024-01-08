package envision.game.entities.combat;

import envision.game.entities.Entity;
import eutil.random.ERandomUtil;

public class EntityAttack {
	
	private int numPhases;
	private EntityAttackPhase[] phases;
	
	public EntityAttack() {
		
	}
	
	public static int calculateMeleeAttackDamage(Entity ent) {
	    int base = ent.getBaseMeleeDamage();
	    int str = ent.getStrengthLevel();
	    
	    if (checkCrit(ent)) {
	        base += str;
	    }
	    
	    return base;
	}
	
	public static boolean checkCrit(Entity ent) {
	    double critChance = 0.1; // should probably be based off the entity in some way
	    double critAmount = 100 * critChance;
	    double rand = ERandomUtil.getRoll(0, 100);
	    return critAmount >= rand;
	}
	
}
