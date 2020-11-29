package util.storageUtil;

import java.lang.reflect.Array;

public class Grid2D<E> {
	
	int width, height;
	E[][] data;
	Class<E> type;
	
	public Grid2D(Class<E> typeIn, int widthIn, int heightIn) {
		width = widthIn;
		height = heightIn;
		type = typeIn;
		data = (E[][]) Array.newInstance(type, width, height);
	}
	
	//---------
	// Methods
	//---------
	
	/** Clears existing value data and sets each value to null. */
	public Grid2D<E> clear() { data = (E[][]) Array.newInstance(type, width, height); return this; }
	
	//---------
	// Setters
	//---------
	
	/** Sets the value at given x and y coords of array. */
	public void set(E value, int xIn, int yIn) {
		if (inRange(this, xIn, yIn)) { data[xIn][yIn] = value; }
	}
	/** Sets the value at given x and y coords of array. Ignores range checking. */
	public void setFast(E value, int xIn, int yIn) { data[xIn][yIn] = value; }
	
	/** Sets an entire row of values in this chunk. */
	public void setRow(E[] in, int rowNum) {
		if (rowNum >= 0 && rowNum < height) { data[rowNum] = in; }
	}
	/** Sets an entire row of values in this chunk. Ignores range checking. */
	public void setRowFast(E[] in, int rowNum) { data[rowNum] = in; }
	
	/** Sets an entire column of values in this chunk. */
	public void setCol(E[] in, int colNum) {
		if (colNum >= 0 && colNum < width) { setColFast(in, colNum); }
	}
	/** Sets an entire column of values in this chunk. Ignores range checking. */
	public void setColFast(E[] in, int colNum) {
		for (int i = 0; i < height; i++) { data[i][colNum] = in[i]; }
	}
	
	public void setRegion(E[][] in, int sX, int sY, int eX, int eY) {
		if (inRange(this, sX, sY, eX, eY)) { setRegionFast(in, sX, sY, eX, eY); }
	}
	public void setRegionFast(E[][] in, int sX, int sY, int eX, int eY) {
		for (int i = sX, x = 0; i < eX; i++, x++) {
			for (int j = sY, y = 0; j < eY; j++, y++) {
				data[i][j] = in[x][y];
			}
		}
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns the value at given x and y coords of array. */
	public E get(int xIn, int yIn) {
		return (inRange(this, xIn, yIn)) ? data[xIn][yIn] : null;
	}
	/** Returns the value at given x and y coords of array. Ignores range checking. */
	public E getFast(int xIn, int yIn) { return data[xIn][yIn]; }
	
	public E[] getRow(int rowNum) {
		return (rowNum >= 0 && rowNum < height) ? data[rowNum] : null;
	}
	public E[] getRowFast(int rowNum) { return data[rowNum]; }
	
	public E[] getCol(int colNum) {
		return (colNum >= 0 && colNum < width) ? getColFast(colNum) : null;
	}
	public E[] getColFast(int colNum) {
		E[] r = (E[]) Array.newInstance(type, height);
		for (int i = 0; i < height; i++) {
			r[i] = data[i][colNum];
		}
		return r;
	}
	
	public E[][] getRegion(int sX, int sY, int eX, int eY) {
		return (inRange(this, sX, sY, eX, eY)) ? getRegionFast(sX, sY, eX, eY) : null;
	}
	public E[][] getRegionFast(int sX, int sY, int eX, int eY) {
		E[][] r = (E[][]) Array.newInstance(type, eX - sX, eY - sY);
		for (int i = sX, x = 0; i < eX; i++, x++) {
			for (int j = sY, y = 0; j < eY; j++, y++) {
				 r[x][y] = data[i][j];
			}
		}
		return (E[][]) r;
	}
	
	/** Returns the width of tiles in this grid. */
	public int getWidth() { return width; }
	/** Returns the height of tiles in this grid. */
	public int getHeight() { return height; }
	
	public E[][] getData() { return data; }
	
	//----------------
	// Static Methods
	//----------------
	
	/** Returns true if the given x and y coords are within the bounds for the grid's width and height. */
	public static boolean inRange(Grid2D g, int xIn, int yIn) {
		return (xIn >= 0 && xIn < g.width) && (yIn >= 0 && yIn < g.height);
	}
	
	public static boolean inRange(Grid2D g, int sX, int sY, int eX, int eY) {
		return (sX >= 0 && sX <= eX && eX < g.width) && (sY >= 0 && sY <= eY && eY < g.height);
	}
	
}
