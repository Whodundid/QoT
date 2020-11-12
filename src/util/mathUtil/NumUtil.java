package util.mathUtil;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import util.EUtil;
import util.storageUtil.StorageBox;

//Author: Hunter Bragg

public class NumUtil {
	
	public static int clamp(int num, int min, int max) { return (num < min) ? min : (num > max ? max : num); }
	public static long clamp(long num, long min, long max) { return (num < min) ? min : (num > max ? max : num); }
	public static float clamp(float num, float min, float max) { return (num < min) ? min : (num > max ? max : num); }
	public static double clamp(double num, double min, double max) { return (num < min) ? min : (num > max ? max : num); }
	
	/** Returns the higher of the given two numbers. */
	public static Number max(Number a, Number b) { return (a.doubleValue() > b.doubleValue()) ? a : b; }
	/** Returns the lower of the given two numbers. */
	public static Number min(Number a, Number b) { return (a.doubleValue() < b.doubleValue()) ? a : b; }
	
	/** Returns true if the given string is an integer. */
	public static boolean isInteger(String s) { return s.matches("-?\\d+"); }
	/** Returns true if the given string is an integer when using the specified base. */
	public static boolean isInteger(String s, int radix) {
		if (s.isEmpty()) { return false; }
		for (int i = 0; i < s.length(); i++) {
			if (i == 0 && s.charAt(i) == '-') {
				if (s.length() == 1) { return false; }
				continue;
			}
			if (Character.digit(s.charAt(i), radix) < 0) { return false; }
		}
		return true;
	}
	
	/** Returns the cartesian distance between two points. (the distance formula) */
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
	
	/** Returns the cartesian distance between two points within storage boxes. (the distance formula) */
	public static double getDistance(StorageBox<Double, Double> point1, StorageBox<Double, Double> point2) {
		if (point1 != null && point2 != null) {
			if (point1.getA() != null && point1.getB() != null && point2.getA() != null && point2.getB() != null) {
				double x1 = point1.getA();
				double y1 = point1.getB();
				double x2 = point2.getA();
				double y2 = point2.getB();
				
				return (int) distance(x1, y1, x2, y2);
			}
		}
		return 0;
	}
	
	/** Returns the datatype of a given number. */
	public static NumType getNumType(Class<? extends Number> c) {
		if (c == Byte.class || c == byte.class) { return NumType.b; }
		if (c == Short.class || c == short.class) { return NumType.s; }
		if (c == Integer.class || c == int.class) { return NumType.i; }
		if (c == Long.class || c == long.class) { return NumType.l; }
		if (c == Float.class || c == float.class) { return NumType.f; }
		if (c == Double.class || c == double.class) { return NumType.d; }
		return NumType.n;
	}
	
	/** Returns the sum of all numbers within the given list. (does not check for null values!) */
	public static double sumValues(List<Number> valsIn) {
		if (valsIn != null) {
			double total = 0;
			for (Number n : valsIn) { total += n.doubleValue(); }
			return total;
		}
		return Double.NaN;
	}
	
	/** Returns the sum of each number squared by the given expIn value. */
	public static double squareAndPowValues(List<Number> valsIn, double expIn) {
		if (valsIn != null) {
			double total = 0;
			for (Number n : valsIn) { total += Math.pow(n.doubleValue(), expIn); }
			return total;
		}
		return Double.NaN;
	}
	
	/** Returns the sum of each element multiplied by the value in the same index of the other list. */
	public static double sumOfProducts(List<Number> vals1, List<Number> vals2) {
		if (EUtil.notNull(vals1, vals2)) {
			if (vals1.size() == vals2.size()) {
				double total = 0;
				for (int i = 0; i < vals1.size(); i++) {
					total += (vals1.get(i).doubleValue() * vals2.get(i).doubleValue());
				}
				return total;
			}
		}
		return Double.NaN;
	}
	
	/** Returns the sum of each element squared by the value in the same index of the other list. */
	public static double sumSquareFirstProdSecond(List<Number> vals1, int expIn, List<Number> vals2) {
		if (EUtil.notNull(vals1, vals2)) {
			if (vals1.size() == vals2.size()) {
				double total = 0;
				for (int i = 0; i < vals1.size(); i++) {
					total += (Math.pow(vals1.get(i).doubleValue(), expIn) * vals2.get(i).doubleValue());
				}
				return total;
			}
		}
		return Double.NaN;
	}
	
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
	
	/** Returns a random char that can be made from any type-able ascii character. */
	public static char randomChar() { return randomChar(RandStringTypes.ANY); }
	/** Returns a random char from a specified set of RandStringTypes. */
	public static char randomChar(RandStringTypes typeIn) {
		switch (typeIn) {
		case LETTERS_ALL: return (char) ((roll(1, 0, 1)) ? getRoll(65, 90) : getRoll(97, 122)); //A-Z | a-z
		case LETTERS_UPPER: return (char) getRoll(65, 90); //A-Z
		case LETTERS_LOWER: return (char) getRoll(97, 122); //a-z
		case NUMBERS: return (char) getRoll(48, 57); //0-9
		case LETALL_NUM:
			switch (getRoll(0, 2)) {
			case 0: return (char) getRoll(48, 57); //numbers
			case 1: return (char) getRoll(65, 90); //A-Z
			case 2: return (char) getRoll(97, 122); //a-z
			}
		case LETUP_NUM:
			switch (getRoll(0, 1)) {
			case 0: return (char) getRoll(48, 57); //numbers
			case 1: return (char) getRoll(65, 90); //A-Z
			}
		case LETLOW_NUM:
			switch (getRoll(0, 1)) {
			case 0: return (char) getRoll(48, 57); //numbers
			case 1: return (char) getRoll(97, 122); //a-z
			}
		case SYMBOLS:
			switch (getRoll(0, 3)) {
			case 0: return (char) getRoll(33, 47); //!-/
			case 1: return (char) getRoll(58, 64); //:-@
			case 2: return (char) getRoll(91, 96); //[-`
			case 3: return (char) getRoll(123, 126); //{-~
			}
		case ANY:
		default: return (char) getRoll(32, 126);
		}
	}
	
	/** Returns a String that is comprised of randomized, type-able ascii characters. */
	public static String randomString() { return randomString(getRoll(5, 30), RandStringTypes.ANY); }
	/** Returns a String that is comprised of randomized, type-able ascii characters from a specified set. */
	public static String randomString(RandStringTypes typeIn) { return randomString(getRoll(5, 30), typeIn); }
	/** Returns a String of a specified length that is comprised of randomized, type-able ascii characters. */
	public static String randomString(int length) { return randomString(length, RandStringTypes.ANY); }
	/** Returns a String of a specified length that is comprised of randomized, type-able ascii characters from a specified set. */
	public static String randomString(int length, RandStringTypes typeIn) {
		String str = "";
		for (int i = 0; i < length; i++) { str += randomChar(typeIn); }
		return str;
	}
	
	/** Returns a String that holds a randomized human-esque name. (see RandomNames.class) */
	public static String randomName() { return RandomNames.get(); }
	/** Returns a String that holds a randomized word. (see RandomWords.class) */
	public static String randomWord() { return RandomWords.get(); }
	
	/** An enum that specifies types of character sets. */
	public enum RandStringTypes { ANY, LETTERS_ALL, LETTERS_UPPER, LETTERS_LOWER, NUMBERS, LETALL_NUM, LETUP_NUM, LETLOW_NUM, SYMBOLS; }
	
}
