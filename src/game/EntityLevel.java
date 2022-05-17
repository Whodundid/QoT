package game;

/** A value that keeps track of experience and level ups. */
public class EntityLevel {
	
	private long xp;
	private int level;
	
	public EntityLevel() { this(0, 1); }
	public EntityLevel(long xpIn, int levelIn) {
		xp = xpIn;
		level = levelIn;
	}
	
	public static long getExpNeededForLevel(int lvl) {
		return (long) (((Math.pow(2, (double) lvl / 5) * 300 + lvl)) * 1.25) - 375;
	}
	
	public static long getTotalExpNeededForLevel(int lvl) {
		long total = 0;
		for (int i = 1; i < lvl + 1; i++) {
			total += getExpNeededForLevel(i);
		}
		return total;
	}
	
	public boolean checkCanLevel() {
		return xp >= getTotalExpNeededForLevel(level + 1);
	}
	
	public int getLevel() { return level; }
	public long getXP() { return xp; }
	
	public void addXP(long amount) { xp += amount; }
	public void levelUp() { level++; }
	
}
