package qot.abilities.tier1;

/**
 * Performable ability:
 * 		Effect:
 * 			1. When activated:
 * 				stays active for 10 seconds or until the entity attacks a living, non-doodad entity that has blood.
 * 			2. Upon a successful attack:
 * 				-Inflicts attacking entity's base attack damage.
 * 				-Deals bleed damage over a set amount time.
 * 				-Ability is used up and must recharge before it can be activated again.
 * 			3. Upon an unsuccessful attack:
 * 				-Miss Penalty: the attacking entity's attack speed is reduced by 30% for 3 seconds.
 * 				-Ability is used up and must recharge before it can be activated again.
 * 
 * 		Activate Conditions:
 * 			Can only be performed on a living, non-doodad entity that has blood.
 * 
 * 		Duration:
 * 			Level 1: 3 seconds
 * 			Level 2: 5 seconds
 * 			Level 3: 7 seconds
 * 
 * 		Level Damage:
 * 			Level 1: (1 to 2) damage per second:
 * 				1 damage: 35% chance
 * 				2 damage: 65% chance
 * 			Level 2: (1 to 3) damage per second:
 * 				1 damage: 25% chance
 * 				2 damage: 60% chance
 * 				3 damage: 15% chance
 * 			Level 3: (2 to 5) damage per second:
 * 				2 damage: 20% chance
 * 				3 damage: 40% chance
 * 				4 damage: 30% chance
 * 				5 damage: 10% chance
 * 
 * 		Level Bonus:
 * 			Level 1: No bonus
 * 			Level 2: Miss penalty reduced to 15% for 3 seconds
 * 			Level 3: Miss penalty removed
 * 
 * 		Cooldown:
 * 			Level 1: 12 seconds
 * 			Level 2: 9 seconds
 * 			Level 3: 6 seconds
 * 
 * 		Ability Tier:
 * 			1
 * 
 * @author Hunter Bragg
 */
public class Ability_DeepStrikes {
	
}
