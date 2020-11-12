package util.miscUtil;

import java.util.List;
import java.util.function.Predicate;

/** A static implementation of a for loop for when you REALLY feel like doing lambda loops with an index. */
public class For {
	
	public static int i = 0;
	
	public static <E> void of(List<E> of, Runnable actionIn) { of(of, actionIn, 0, (of != null) ? of.size() : 0, 1, i -> ((Number) i).intValue() < ((of != null) ? of.size() : 0)); }
	public static <E> void of(List<E> of, Runnable actionIn, int toVal) { of(of, actionIn, 0, toVal, 1, i -> ((Number) i).intValue() < toVal); }
	public static <E> void of(List<E> of, Runnable actionIn, int toVal, int changeVal) { of(of, actionIn, 0, toVal, changeVal, i -> ((Number) i).intValue() < toVal); }
	public static <E> void of(List<E> of, Runnable actionIn, int start, int toVal, int changeVal, Predicate<? super Number> conditionIn) {
		run(of, actionIn, start, toVal, changeVal, conditionIn);
	}
	
	private static <E> void run(List<E> of, Runnable actionIn, int start, int toVal, int changeVal, Predicate<? super Number> conditionIn) {
		i = start;
		while (conditionIn.test(i)) {
			actionIn.run();
			i += changeVal;
		}
		i = 0;
	}
	
}
