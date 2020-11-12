package util.storageUtil;

import util.EUtil;

//Author: Hunter Bragg

public class Vector3<E> {

	public E x, y, z;
	
	//--------------------
	//Vector3 Constructors
	//--------------------
	
	public Vector3(Vector3<E> vecIn) { EUtil.nullDo(vecIn, v -> set(v.x, v.y, v.z)); }
	public Vector3(E x, E y, E z) { set(x, y, z); }
	
	//----------------
	//Object Overrides
	//----------------
	
	@Override public String toString() { return "[" + x + ", " + y + ", " + z + "]"; }
	
	//---------------
	//Vector3 Methods
	//---------------
	
	public Vector3<E> clear() { x = null; y = null; z = null; return this; }
	
	public boolean compare(Vector3<E> vecIn) { return EUtil.nullDoR(vecIn, v -> compare(v.x, v.y, v.z), false); }
	public boolean compare(E xIn, E yIn, E zIn) { return (EUtil.isEqual(x, xIn) && EUtil.isEqual(y, yIn) && EUtil.isEqual(z, zIn)); }
	
	public boolean compareX(E xIn) { return EUtil.isEqual(x, xIn); }
	public boolean compareY(E yIn) { return EUtil.isEqual(y, yIn); }
	public boolean compareZ(E zIn) { return EUtil.isEqual(z, zIn); }	
	
	//---------------
	//Vector3 Getters
	//---------------
	
	public E getX() { return x; }
	public E getY() { return y; }
	public E getZ() { return z; }
	
	//---------------
	//Vector3 Setters
	//---------------
	
	public Vector3<E> set(Vector3<E> vecIn) { return EUtil.nullDoR(vecIn, v -> set(v.x, v.y, v.z), this); }
	public Vector3<E> set(E xIn, E yIn, E zIn) { x = xIn; y = yIn; z = zIn; return this; }
	
	public Vector3<E> setX(E xIn) { x = xIn; return this; }
	public Vector3<E> setY(E yIn) { y = yIn; return this; }
	public Vector3<E> setZ(E zIn) { z = zIn; return this; }
	
}
