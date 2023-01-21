package qot.abilities.tier1;

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
public class Ability_SelfHeal {
	
}
