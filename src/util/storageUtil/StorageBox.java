package util.storageUtil;

//Author: Hunter Bragg

public class StorageBox<A, B> {
	
	private A a;
	private B b;
	
	//constructor
	public StorageBox() { this(null, null); }
	public StorageBox(A aIn, B bIn) { a = aIn; b = bIn; }
	
	public boolean contains(Object obj) {
		if (obj == null) { return a == null || b == null; }
		return ((a != null ? a.equals(obj) : false) || (b != null ? b.equals(obj) : false));
	}
	
	public void setValues(StorageBox<A, B> boxIn) { a = (boxIn != null) ? boxIn.a : null; b = (boxIn != null) ? boxIn.b : null; }
	public void setValues(A obj, B val) { a = obj; b = val; }
	public void setA(A obj) { a = obj; }	
	public void setB(B obj) { b = obj; }
	public void clear() { a = null; b = null; }
	
	public A getA() { return a; }	
	public B getB() { return b; }
	
	public boolean containsA(Object obj) { return (obj == null ) ? a == null : obj.equals(a); }
	public boolean containsB(Object val) { return (val == null) ? b == null : val.equals(b); }
	
	public boolean compare(StorageBox<?, ?> boxIn) { return compare(boxIn.getA(), boxIn.getB()); }
	public boolean compare(Object inObj, Object inVal) { return (a.equals(inObj) && b.equals(inVal)); }
	
	public static boolean compare(StorageBox<?, ?> box1, StorageBox<?, ?> box2) { return (box1 != null && box2 != null) ? box1.compare(box2) : false; }
	
	@Override public String toString() { return "[" + a + ", " + b + "]"; }
	
}
