package Aspects;
import java.util.concurrent.ThreadLocalRandom;

public class Utility {
	
    public static int clamp(int num, int min, int max) { return (num <= min) ? min : (num >= max ? max : num); }
    public static long clamp(long num, long min, long max) { return (num <= min) ? min : (num >= max ? max : num); }
    public static float clamp(float num, float min, float max) { return (num <= min) ? min : (num >= max ? max : num); }
    public static double clamp(double num, double min, double max) { return (num <= min) ? min : (num >= max ? max : num); }
    
	/** Returns true if the check value matches the produced random number within range. */
	public static boolean roll(int check, int min, int max) {
		return check == ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	/** Returns the number from a roll within a specified range. (ints) */
	public static int getRoll(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	/** Returns the number from a roll within a specified range. (doubles) */
	public static double getRoll(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max + 1);
	}
	
}