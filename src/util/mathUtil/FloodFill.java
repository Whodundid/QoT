package util.mathUtil;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class FloodFill {
	
	private static final int[] row = { -1, -1, -1, 0, 0, 1, 1, 1 };
    private static final int[] col = { -1, 0, 1, -1, 1, -1, 0, 1 };
	
	static class Pair {
		int x;
		int y;
		
		public Pair(int xIn, int yIn) {
			x = xIn;
			y = yIn;
		}
	}
	
	private static boolean good(int[][] arr, int x, int y, int w, int h, int target) {
		return (x >= 0 && x < w && y >= 0 && y < h) && (arr[x][y] == target);
	}
	
	public static void replace(int[][] arrayIn, int sX, int sY, int replacement) {
		
		int w = arrayIn.length;
		int h = arrayIn[0].length;
		
		// create a queue and enqueue starting pixel
		Queue<Pair> q = new ArrayDeque();
		q.add(new Pair(sX, sY));
		
		// get target color
		int target = arrayIn[sX][sY];
		
		// loop till queue is empty
		while (!q.isEmpty()) {
			// pop front node from queue and process it
			Pair node = q.poll();
			
			// (x, y) represents current pixel
			sX = node.x;
			sY = node.y;
			
			// replace current pixel color with that of replacement
			arrayIn[sX][sY] = replacement;
			
			// process all 8 adjacent pixels of current pixel and
			// enqueue each valid pixel
			for (int k = 0; k < row.length; k++) {
				
				// if adjacent pixel at position (x + row[k], y + col[k]) is
				// a valid pixel and have same color as that of current pixel
				if (good(arrayIn, sX, sY, w, h, target)) {
					// enqueue adjacent pixel
					q.add(new Pair(sX + row[k], sY + col[k]));
				}
				
			}
		}
		
	}
}
