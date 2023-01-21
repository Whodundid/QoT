package envision.util;

import eutil.datatypes.EArrayList;

public class InsertionSort {
	
	private InsertionSort() {}
	
	public static void sort(EArrayList<? extends IDrawable> objects) {
		for (int i = 1; i < objects.size(); i++) {
			var key = get(objects, i);
			int j = i - 1;
			
			while (j >= 0 && get(objects, j) > key) {
				objects.swap(j + 1, j--);
			}
			key = get(objects, j + 1);
		}
	}
	
	private static double get(EArrayList<? extends IDrawable> l, int i) {
		return l.get(i).getSortPoint();
	}
	
}
