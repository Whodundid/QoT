package qot.abilities.tier1;

import envision.game.abilities.Ability;
import envision.game.abilities.AbilityTier;
import envision.game.entities.Entity;
import qot.assets.textures.ability.AbilityTextures;

/**
 * Performable ability:
 * 		Effect:
 * 			Heals self for certain amount for specified amount of mana.
 * 
 * 		Level Bonus:
 * 			Level 1: heal 20% of base health on use
 * 			Level 2: heal 33% of base health on use
 * 			Level 3: heal 60% of base health on use
 * 
 * 		Cooldown:
 * 			Level 1: 30 seconds
 * 			Level 2: 45 seconds
 * 			Level 3: 60 seconds
 * 
 * 		Cast Time:
 * 			3 seconds (all levels)
 * 				A. Can be cancelled at any point during cast time
 * 					If cancelled, no health is healed and ability goes on cooldown
 * 				B. If interrupted during cast (being successfully attacked):
 * 			 		ability to move is restored but no health is healed and ability goes on cooldown
 * 
 * 		Cast Penalty:
 * 			Defense is halved while casting and entity cannot move unless casting is cancelled
 * 
 * 		Ability Tier:
 * 			1
 * 
 * 		Level Requirements:
 * 			Level 1: Player level >= 1
 * 			Level 2: Player level >= 15
 * 			Level 3: Player level >= 25
 * 
 * @author Hunter Bragg
 */
public class Ability_SelfHeal extends Ability {

	public Ability_SelfHeal() {
		super("Self Heal", AbilityTextures.cast_heal);
		
		// assuming 60 game ticks per second
		// (I need to figure out a better way to do this)
		
		setNumTiers(3);
		
		tier(0).manaCost(5).castTime(3 * 60).cooldown(5 * 60).requiresLevel(1);
		tier(1).manaCost(15).castTime(3 * 60).cooldown(5 * 60).requiresLevel(15);
		tier(2).manaCost(25).castTime(3 * 60).cooldown(5 * 60).requiresLevel(25);
	}

	@Override
	public boolean use(Entity e, int tierIn) {
		if (!canEntityUse(e, tierIn)) return false;
		
		int level = e.getSpellbook().getAbilityLevel(this);
		AbilityTier tier = tier(level - 1);
		
		// if the tier is somehow null, return false, this is probably a broken ability though
		if (tier == null) {
			System.out.println(this + " does not have tier: '" + level + "' !");
			return false;
		}
		
		int manaCost = tier.manaCost();
		if (!e.manaCheck(manaCost)) return false;
		
		e.drainMana(manaCost);
		
		int maxHealth = e.getMaxHealth();
		int amount = switch (level) {
		case 1 -> (int) Math.ceil((double) maxHealth * 0.20);
		case 2 -> (int) Math.ceil((double) maxHealth * 0.33);
		case 3 -> (int) Math.ceil((double) maxHealth * 0.60);
		default -> maxHealth;
		};
		e.replenishHealth(amount);
		
		return true;
	}

	@Override
	public boolean canEntityUse(Entity e, int tierIn) {
		var sb = e.getSpellbook();
		
		// must know ability and must have unlocked this specific tier
		if (!sb.knowsAbility(this)) return false;
		if (sb.getAbilityLevel(this) < tierIn) return false;
		
		return true;
	}

	@Override
	public boolean canEntityUpgrade(Entity e) {
		var sb = e.getSpellbook();
		
		// must know ability
		if (!sb.knowsAbility(this)) return false;
		
		int tierLevel = sb.getAbilityLevel(this);
		
		// can't level past max tier
		if (tierLevel >= maxTiers) return false;
		
		// get the next tier
		var tier = tier(tierLevel + 1);
		
		// check level requirement
		if (tier.requiresLevel() > e.getLevel()) return false;
		//if (tier.requiresQuestCompletion())
		
		return true;
	}
	
}
