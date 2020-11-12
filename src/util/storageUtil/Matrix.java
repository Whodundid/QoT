package util.storageUtil;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import util.mathUtil.NumType;

//Author: Hunter Bragg

/**
 * The {@code Matrix} class is a data type representing a matrix structure with default {@code Double} values.
 * The {@code Matrix} class provides numerous functions for performing matrix operations both locally and statically.
 * <blockquote><pre>
 *     Matrix m = new Matrix(2, 2, 1.0, 2, 3.0, 4);
 *     
 *     			|1.0 2.0|
 *     			|3.0 4.0|
 * </pre></blockquote><p>
 * @author Hunter Bragg
 */
public class Matrix {
	
	StorageBoxHolder<Integer, List<Number>> table = new StorageBoxHolder();
	private int rSize = 0, cSize = 0;
	private boolean printCommas = false;
	private NumType origType = NumType.n;
	
	//---------------------
	// Matrix Constructors
	//---------------------
	
	public Matrix() { this(0, 0, NumType.d); }
	public Matrix(StorageBox<Integer, Integer> dimIn) { this(dimIn, NumType.d); }
	public Matrix(StorageBox<Integer, Integer> dimIn, NumType typeIn) { this(dimIn.getA(), dimIn.getB(), typeIn); }
	public Matrix(int rowSizeIn, int columnSizeIn) { this(rowSizeIn, columnSizeIn, NumType.d); }
	public Matrix(int rowSizeIn, int columnSizeIn, NumType typeIn) {
		create(rowSizeIn, columnSizeIn, typeIn);
	}
	
	public Matrix(int rowSizeIn, int columnSizeIn, Number... vals) { this(rowSizeIn, columnSizeIn, NumType.d, vals); }
	public Matrix(int rowSizeIn, int columnSizeIn, NumType typeIn, Number... vals) {
		create(rowSizeIn, columnSizeIn, typeIn);
		if (vals.length == rowSizeIn * columnSizeIn) {
			int q = 0;
			for (int j = 0; j < rSize; j++) {
				for (int i = 0; i < cSize; i++) {
					setVal(j, i, vals[q]);
					q++;
				}
			}
		}
	}
	
	public Matrix(int rowSizeIn, int columnSizeIn, List<Number> vals)  { this(rowSizeIn, columnSizeIn, NumType.d, vals); }
	public Matrix(int rowSizeIn, int columnSizeIn, NumType typeIn, List<Number> vals) {
		create(rowSizeIn, columnSizeIn, typeIn);
		if (vals.size() == rowSizeIn * columnSizeIn) {
			int q = 0;
			for (int j = 0; j < rSize; j++) {
				for (int i = 0; i < cSize; i++) {
					setVal(j, i, vals.get(q));
					q++;
				}
			}
		}
	}
	
	public Matrix(Matrix matrixIn) { if (matrixIn != null) { setValues(matrixIn); } else { create(0, 0, NumType.d); } }
	
	public Matrix(Vector3D vecIn) {
		if (vecIn == null) { create(0, 0, NumType.d); }
		else {
			create(3, 1, NumType.d);
			setVal(0, 0, vecIn.x);
			setVal(1, 0, vecIn.y);
			setVal(2, 0, vecIn.z);
		}
	}
	
	//------------------
	// Object Overrides
	//------------------
	
	@Override
	public String toString() {
		String returnString = "";
		int longest = getLongest(toList());
		int long1 = getLongest(getColumn(0));
		
		for (int i = 0; i < rSize; i++) {
			returnString += "|";
			for (int j = 0; j < cSize; j++) {
				String val = String.valueOf(getVal(i, j));
				int offset = j == 0 ? long1 - val.length() : longest - val.length();
				for (int p = 0; p < offset; p++) { returnString += " "; }
				
				returnString += getVal(i, j) + (printCommas ? ", " : " ");
			}
			returnString = returnString.substring(0, returnString.length() >= (printCommas ? 2 : 1) ? returnString.length() - (printCommas ? 2 : 1) : returnString.length());
			returnString += "|\n";
		}
		
		returnString = returnString.substring(0, returnString.length() >= 1 ? returnString.length() - 1 : returnString.length());
		
		return returnString;
	}
	
	private int getLongest(List<Number> list) {
		int returnVal = 0;
		
		for (Number i : list) {
			String s = String.valueOf(i);
			if (s.length() > returnVal) { returnVal = s.length(); }
		}
		
		return returnVal;
	}
	
	//---------
	// Methods
	//---------
	
	public boolean printsCommas() { return printCommas; }
	public Matrix setPrintCommas(boolean valIn) { printCommas = valIn; return this; }
	public int getNumRows() { return rSize; }
	public int getNumCols() { return cSize; }
	public StorageBox<Integer, Integer> getDimensions() { return new StorageBox<Integer, Integer>(rSize, cSize); }
	public NumType getNumberType() { return origType; }
	public Matrix draw() { return draw(""); }
	public Matrix draw(String title) { System.out.println(title + "\n" + this); return this; }
	public Matrix draw(String title, String end) { System.out.println(title + "\n" + this + "\n" + end); return this; }
	
	public Matrix save(String fileName) { return save(new File(fileName), false); }
	public Matrix save(String fileName, boolean asPrint) { return save(new File(fileName), asPrint); }
	public Matrix save(File fileIn) { return save(fileIn, false); }
	public Matrix save(File fileIn, boolean asPrint) {
		try {
			PrintWriter w = new PrintWriter(fileIn);
			if (asPrint) { w.print(this); }
			else {
				w.print(rSize + " " + cSize + (table.isNotEmpty() ? " " : ""));
				toList().forEach(n -> w.print(n + " "));
			}
			w.close();
		}
		catch (Exception e) { e.printStackTrace(); }
		return this;
	}
	
	//----------------
	// Util Functions
	//----------------
	
	private void create(int rowSizeIn, int columnSizeIn) { create(rowSizeIn, columnSizeIn, NumType.d); }
	private void create(int rowSizeIn, int columnSizeIn, NumType typeIn) {
		rSize = rowSizeIn;
		cSize = columnSizeIn;
		origType = typeIn;
		clearMatrix();
	}
	
	public Number getVal(int rowPos, int colPos) {
		try {
			return rangeCheck(rowPos, colPos) ? table.getBoxWithA(rowPos).getB().get(colPos) : -1;
		}
		catch (Exception e) { return Double.NaN; }
	}
	
	public Matrix setVal(int rowPos, int colPos, Number valIn) {
		if (rangeCheck(rowPos, colPos)) {
			table.getBoxWithA(rowPos).getB().set(colPos, valIn);
		}
		return this;
	}
	
	public Matrix setValAsType(int rowPos, int colPos, Number valIn, NumType typeIn) {
		if (rangeCheck(rowPos, colPos)) {
			switch (typeIn) {
			case b: table.getBoxWithA(rowPos).getB().set(colPos, valIn.byteValue()); break;
			case s: table.getBoxWithA(rowPos).getB().set(colPos, valIn.shortValue()); break;
			case i: table.getBoxWithA(rowPos).getB().set(colPos, valIn.intValue()); break;
			case l: table.getBoxWithA(rowPos).getB().set(colPos, valIn.longValue()); break;
			case f: table.getBoxWithA(rowPos).getB().set(colPos, valIn.floatValue()); break;
			case d: table.getBoxWithA(rowPos).getB().set(colPos, valIn.doubleValue()); break;
			default: throw new NumberFormatException();
			}
		}
		return this;
	}
	
	public List<Number> getRow(int rowNumIn) {
		return rangeCheck(rowNumIn, 0) ? table.getBoxWithA(rowNumIn).getB() : null;
	}
	
	public List<Number> getColumn(int colNumIn) {
		if (rangeCheck(0, colNumIn)) {
			EArrayList<Number> l = new EArrayList(cSize);
			for (int i = 0; i < rSize; i++) {
				StorageBox<Integer, List<Number>> box = table.getBoxWithA(i);
				if (box != null && box.getB() != null && box.getB().get(colNumIn) != null) {
					l.add(i, box.getB().get(colNumIn));
				}
				else { l.set(i, Double.NaN); }
			}
			return l;
		}
		return null;
	}
	
	public Matrix setRow(int rowNumIn, List<Number> rowIn) {
		if (rangeCheck(rowNumIn, 0)) {
			table.setBInBox(rowNumIn, rowIn);
		}
		return this;
	}
	
	public Matrix setColumn(int columnNumIn, List<Number> columnIn) {
		if (rangeCheck(0, columnNumIn)) {
			for (int i = 0; i < rSize; i++) {
				StorageBox<Integer, List<Number>> box = table.getBoxWithA(i);
				if (box != null) {
					box.getB().set(i, columnIn.get(i));
				}
			}
		}
		return this;
	}
	
	public Matrix setValues(Matrix matrixIn) {
		if (matrixIn != null) {
			rSize = matrixIn.getNumRows();
			cSize = matrixIn.getNumCols();
			origType = matrixIn.getNumberType();
			for (int i = 0; i < rSize; i++) {
				table.add(i, matrixIn.getRow(i));
			}
		}
		return this;
	}
	
	public Matrix clearMatrix() {
		table.clear();
		for (int i = 0; i < rSize; i++) {
			EArrayList<Number> row = new EArrayList();
			table.add(i, row);
			for (int j = 0; j < cSize; j++) { row.add(0.0); }
		}
		return this;
	}
	
	private boolean rangeCheck(int rowPos, int colPos) {
		return (rowPos >= 0 && rowPos < rSize) && (colPos >= 0 && colPos < cSize);
	}
	
	public Matrix asByte() { return convertTo(NumType.b); }
	public Matrix asShort() { return convertTo(NumType.s); }
	public Matrix asInt() { return convertTo(NumType.i); }
	public Matrix asLong() { return convertTo(NumType.l); }
	public Matrix asFloat() { return convertTo(NumType.f); }
	public Matrix asDouble() { return convertTo(NumType.d); }
	
	public Matrix convertTo(NumType typeIn) {
		for (int i = 0; i < rSize; i++) {
			for (int j = 0; j < cSize; j++) {
				setValAsType(i, j, getVal(i, j), typeIn);
			}
		}
		origType = typeIn;
		return this;
	}
	
	public List<Number> toList() {
		EArrayList<Number> l = new EArrayList();
		for (int i = 0; i < rSize; i++) {
			for (int j = 0; j < cSize; j++) {
				l.add(getVal(i, j));
			}
		}
		return l;
	}
	
	public double getN(int xPos, int yPos) {
		return getVal(xPos, yPos).doubleValue();
	}
	
	//----------------
	// Math Functions
	//----------------
	
	public Matrix scale(Number scaleFactor) {
		Matrix m = new Matrix(this);
		for (int i = 0; i < rSize; i++) {
			for (int j = 0; j < cSize; j++) {
				m.setValAsType(i, j, getVal(i, j).doubleValue() * scaleFactor.doubleValue(), origType);
			}
		}
		return m;
	}
	
	public Matrix add(Matrix matrixIn) {
		if (compareDimensions(this, matrixIn)) {
			Matrix m = new Matrix(this);
			for (int i = 0; i < rSize; i++) {
				for (int j = 0; j < cSize; j++) {
					m.setValAsType(i, j, getVal(i, j).doubleValue() + matrixIn.getVal(i, j).doubleValue(), origType);
				}
			}
			return m;
		}
		return null;
	}
	
	public Matrix subtract(Matrix matrixIn) {
		if (compareDimensions(this, matrixIn)) {
			Matrix m = new Matrix(this);
			for (int i = 0; i < rSize; i++) {
				for (int j = 0; j < cSize; j++) {
					m.setValAsType(i, j, getVal(i, j).doubleValue() - matrixIn.getVal(i, j).doubleValue(), m.origType);
				}
			}
			return m;
		}
		return null;
	}
	
	public Matrix multiply(Matrix matrixIn) {
		if (multiplyCheck(this, matrixIn)) {
			Matrix m = new Matrix(rSize, matrixIn.getNumCols());
			for (int t = 0; t < matrixIn.getNumCols(); t++) {
				for (int i = 0; i < rSize; i++) {
					double sum = 0.0;
					for (int j = 0; j < cSize; j++) {
						sum += (getVal(i, j).doubleValue() * matrixIn.getVal(j, t).doubleValue());
					}
					m.setValAsType(i, t, sum, origType);
				}
			}
			return m;
		}
		return null;
	}
	
	public Matrix transpose() {
		Matrix m = new Matrix(this);
		List<List<Number>> columns = new EArrayList();
		for (int i = 0; i < cSize; i++) { columns.add(getColumn(i)); } //grab old columns
		for (int i = 0; i < rSize; i++) { m.setRow(i, columns.get(i)); } //set rows as old columns
		return m;
	}
	
	public Number getDet() {
		if (isSquare(this)) {
			if (getNumCols() <= 3) {
				if (getNumCols() == 1) { return getVal(0, 0); }
				else if (getNumCols() == 2) {
					return (getVal(0, 0).doubleValue() * getVal(1, 1).doubleValue() -
							getVal(0, 1).doubleValue() * getVal(1, 0).doubleValue());
				}
				else if (getNumCols() == 3) {
					return (getN(0, 0) * (getN(1, 1) * getN(2, 2) - getN(1, 2) * getN(2, 1))) -
						   (getN(0, 1) * (getN(1, 0) * getN(2, 2) - getN(1, 2) * getN(2, 0))) +
						   (getN(0, 2) * (getN(1, 0) * getN(2, 1) - getN(1, 1) * getN(2, 0)));
				}
			}
		}
		return Double.NaN;
	}
	
	//----------------
	// Static Methods
	//----------------
	
	public static Matrix add(Matrix m1, Matrix m2) { return m1 != null ? (m2 != null ? m1.add(m2) : m1) : m2 != null ? m2 : null; }
	public static Matrix subtract(Matrix m1, Matrix m2) { return m1 != null ? (m2 != null ? m1.subtract(m2) : m1) : m2 != null ? m2 : null; }
	public static Matrix multiply(Matrix m1, Matrix m2) { return m1 != null ? (m2 != null ? m1.multiply(m2) : m1) : m2 != null ? m2 : null; }
	public static Matrix transpose(Matrix matrixIn) { return matrixIn != null ? matrixIn.transpose() : null; }
	
	public static List<Number> toList(Matrix matrixIn) {
		EArrayList<Number> l = new EArrayList();
		for (int i = 0; i < matrixIn.getNumRows(); i++) {
			for (int j = 0; j < matrixIn.getNumCols(); j++) {
				l.add(matrixIn.getVal(i, j));
			}
		}
		return l;
	}
	
	public static boolean compareDimensions(Matrix m1, Matrix m2) {
		 return m1 != null && m2 != null ? m1.getDimensions().compare(m2.getDimensions()) : false;
	}
	
	public static boolean multiplyCheck(Matrix m1, Matrix m2) {
		return m1 != null && m2 != null ? (m1.getNumCols() == m2.getNumRows()) : false;
	}
	
	public static boolean isSquare(Matrix m) {
		return m != null ? (m.getNumRows() == m.getNumCols()) : false;
	}
	
	private static double getN(Matrix m, int xPos, int yPos) {
		return m.getVal(xPos, yPos).doubleValue();
	}
	
}
