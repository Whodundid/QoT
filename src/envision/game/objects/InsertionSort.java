package envision.game.objects;

import eutil.datatypes.util.EList;

public class InsertionSort {
	
	private InsertionSort() {}
	
	public static void sort(EList objects) {
		int size = objects.size();
		for (int i = 1; i < size; i++) {
			var key = get(objects, i);
			int j = i - 1;
			
			while (j >= 0 && get(objects, j) > key) {
				objects.swap(j + 1, j--);
				//objects.set(j + 1, objects.get(j));
				//j--;
			}
			
			key = get(objects, j + 1);
		}
	}
	
	// x x x x x
	// 0 1 4 2 3
	
	private static double get(EList<? extends IDrawable> l, int i) {
		return l.get(i).getSortPoint();
	}
	
}
