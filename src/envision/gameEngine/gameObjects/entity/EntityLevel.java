package envision.gameEngine.gameObjects.entity;

/**
 * A static helper class which helps to keep track of experience and
 * level ups.
 */
public class EntityLevel {
	
	private EntityLevel() {}
	
	/**
	 * Returns the amount of experience needed to advance to the next
	 * given level.
	 * <p>
	 * Note: this method calculates for the absolute minimum amount of
	 * experience needed for a specific level and assumes that it is
	 * calculating for the next immediate level up.
	 * <p>
	 * For instance, if you are currently level 10 and want to know how
	 * much experience is needed to reach level 11, then pass '11' to this
	 * method to determine the amount needed.
	 * 
	 * @param level The level to reach
	 * @return The amount of experience needed to reach the given level
	 *         from the previous level
	 */
	public static long getXPNeededForNextLevel(int level) {
		return (long) (((Math.pow(2, (double) level / 5) * 300 + level)) * 1.25) - 375;
	}
	
	/**
	 * Returns the minimum total amount of experience that would be needed
	 * in order to reach the given level.
	 * 
	 * @param level
	 * @return
	 */
	public static long getTotalXPNeeded(int level) {
		long total = 0;
		for (int i = 1; i < level + 1; i++) {
			total += getXPNeededForNextLevel(i);
		}
		return total;
	}
	
	/**
	 * Returns the minimum level that corresponds to the total amount of
	 * experience earned.
	 * 
	 * @param xp
	 * @return The level from the given experience
	 */
	public static int getLevelFromXP(long xp) {
		int level = 0;
		while (xp > 0) {
			xp -= getXPNeededForNextLevel(level++);
		}
		return level;
	}
	
	/**
	 * Takes in the current level and the total amount of experience earned and
	 * calculates whether or not there is enough XP for a level up.
	 * 
	 * @param level
	 * @param xp
	 * @return True if can level up
	 */
	public static boolean checkLevelUp(int level, long xp) {
		return xp >= getTotalXPNeeded(level + 1);
	}
	
	public static int calculateMaxHealth(int hitpointsLevel) {
		return 7 + (int) Math.ceil(hitpointsLevel * 2.50);
	}
	
	public static int calculateMaxMana(int magicLevel) {
		return 17 + (int) Math.ceil(magicLevel * 2.50);
	}
	
	public static int calculateBaseDamage(int strengthLevel) {
		int x = strengthLevel;
		return (int) (Math.ceil(x*0.516667)+Math.floor(0.04*x*Math.exp(0.0386538*x)));
	}
	
}
