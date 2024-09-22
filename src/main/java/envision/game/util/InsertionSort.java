package envision.game.util;

import eutil.datatypes.util.EList;

public class InsertionSort {
    
    private InsertionSort() {}
    
    public static void sort(EList<? extends IDrawable> objects) {
        final int size = objects.size();
        for (int i = 1; i < size; i++) {
            var key = objects.get(i).getSortPoint();
            int j = i - 1;
            
            while (j >= 0 && (objects.get(j).getSortPoint()) > key) {
                objects.swapf(j + 1, j--);
                //objects.set(j + 1, objects.get(j));
                //j--;
            }
            
            key = objects.get(j + 1).getSortPoint();
        }
        
        //System.out.println(objects);
    }
    
    // x x x x x
    // 0 1 4 2 3
    
//    private static double get(EList<? extends IDrawable> l, int i) {
//        return l.get(i).getSortPoint();
//    }
    
}
