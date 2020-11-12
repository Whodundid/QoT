package util.storageUtil;

import util.EUtil;

//Author: Hunter Bragg

/**
 * The {@code Vector3D} class is a data type containing three individual {@code Double} values: x, y, and z.
 * The {@code Vector3D} class provides numerous functions for performing vector math both locally and statically.
 * <blockquote><pre>
 *     Vector3D vec = new Vector3D(x, y, z);
 * </pre></blockquote><p>
 * @author Hunter Bragg
 */
public class Vector3D {

	public double x = 0, y = 0, z = 0;
	
	//---------------------
	//Vector3D Constructors
	//---------------------
	
	public Vector3D() { set(0, 0 , 0); }
	public Vector3D(Vector3D vecIn) { EUtil.nullDo(vecIn, v -> set(v.x, v.y, v.z)); }
	public Vector3D(Vector3I vecIn) { EUtil.nullDo(vecIn, v -> set(v.x, v.y, v.z)); }
	public Vector3D(double x, double y, double z) { set(x, y, z); }
	
	//----------------
	//Object Overrides
	//----------------
	
	@Override public String toString() { return "[" + x + ", " + y + ", " + z + "]"; }
	
	//----------------
	//Vector3D Methods
	//----------------
	
	/** Sets each value in this Vector3D to 0. */
	public Vector3D clear() { x = 0; y = 0; z = 0; return this; }
	
	/** Floors each value in this Vector3D. */
	public Vector3D floor() { set(Math.floor(x), Math.floor(y), Math.floor(z)); return this; }
	
	/** Ceils each value in this Vector3D. */
	public Vector3D ceil() { set(Math.ceil(x), Math.ceil(y), Math.ceil(z)); return this; }
	
	/** Returns a Vector3DInt from floored values of this Vector3D. */
	public Vector3I asVector3I() { return new Vector3I(x, y, z); }
	
	/** Compares the contents of this Vector3D to another. */
	public boolean compare(Vector3D vecIn) { return EUtil.nullDoR(vecIn, v -> compare(v.x, v.y, v.z), false); }
	
	/** Compares the contents of this Vector3D to each x, y and z value provided. */
	public boolean compare(double xIn, double yIn, double zIn) { return (x == xIn && y == yIn && z == zIn); }
	
	/** Returns true if this Vector3Ds x is equal to the given value. */
	public boolean compareX(double xIn) { return x == xIn; }
	
	/** Returns true if this Vector3Ds y is equal to the given value. */
	public boolean compareY(double yIn) { return y == yIn; }
	
	/** Returns true if this Vector3Ds z is equal to the given value. */
	public boolean compareZ(double zIn) { return z == zIn; }
	
	//--------------------
	//Vector3D Vector Math
	//--------------------
	
	public double magnitude() { return Math.sqrt(x * x + y * y + z * z); }
	public double dotProduct(Vector3D vecIn) { return Math.acos(multiplyAndAdd(this, vecIn) / magnitude(vecIn)); }
	public double multiplyAndAdd(Vector3D vecIn) { return EUtil.nullApplyR(vecIn, v -> x * v.x + y * v.y + z * v.z, Double.NaN); }
	public Vector3D scale(double val) { x *= val; y *= val; z *= val; return this;  }
	public Vector3D crossProduct(Vector3D vecIn) { return EUtil.nullApplyR(vecIn, v -> new Vector3D(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x), null); }
	public Vector3D difference(Vector3D vecIn) { return EUtil.nullApplyR(vecIn, v -> new Vector3D(x - v.x, y - v.y, z - v.z), null); }
	public Vector3D normalize() { return EUtil.nullDoR(magnitude(), l -> { x /= l; y /= l; z /= l; }, this); }

	//----------------
	//Vector3D Getters
	//----------------
	
	public double getX() { return x; }
	public double getY() { return y; }
	public double getZ() { return z; }
	
	//----------------
	//Vector3D Setters
	//----------------
	
	public Vector3D set(Vector3D vecIn) { return EUtil.nullDoR(vecIn, v -> set(v.x, v.y, v.z), this); }
	public Vector3D set(Vector3I vecIn) { return EUtil.nullDoR(vecIn, v -> set(v.x, v.y, v.z), this); }
	public Vector3D set(Number xIn, Number yIn, Number zIn) { x = xIn.doubleValue(); y = yIn.doubleValue(); z = zIn.doubleValue(); return this; }
	
	public Vector3D setX(double xIn) { x = xIn; return this; }
	public Vector3D setY(double yIn) { y = yIn; return this; }
	public Vector3D setZ(double zIn) { z = zIn; return this; }	
	
	//---------------------------
	//Vector3D Static Vector Math
	//---------------------------
	
	/** Returns the magnitdue of the given Vector3. */
	public static double magnitude(Vector3D vecIn) {
		return EUtil.nullApplyR(vecIn, v -> Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z), null);
	}
	
	/** Returns the dot product of the two given vectors. */
	public static double dotProduct(Vector3D vec1, Vector3D vec2) {
		return EUtil.nullApplyR(vec1, vec2, (a, b) -> Math.acos(multiplyAndAdd(a, b) / magnitude(b)), null);
	}
	
	/** Returns a double that is the result of each given vector3s x, y, and z values being multiplied and added together. */
	public static double multiplyAndAdd(Vector3D vec1, Vector3D vec2) {
		return EUtil.nullApplyR(vec1, vec2, (a, b) -> a.x * b.x + a.y * b.y + a.z * b.z, null);
	}
	
	/** Returns the given Vector3 with each of its x, y and z multiplied by a given double value. */
	public static Vector3D scale(Vector3D vecIn, double val) {
		return EUtil.nullDoR(vecIn, v -> v.set(v.x * val, v.y * val, v.z * val), vecIn);
	}
	
	/** Returns a new Vector3 containg the cross product of the two given Vector3 objects. */
	public static Vector3D crossProduct(Vector3D vec1, Vector3D vec2) {
		return EUtil.nullDoR(vec1, vec2, (a, b) -> new Vector3D(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x), null);
	}
	
	/** Returns a new Vector3 containing the difference of the given vec2 values from the given vec1 values. (vec1 - vec2) */
	public static Vector3D difference(Vector3D vec1, Vector3D vec2) {
		return EUtil.nullDoR(vec1, vec2, (a, b) -> new Vector3D(a.x - b.x, a.y - b.y, a.z - b.z), null);
	}
	
	/** Returns the given Vector3 with normalized values. */
	public static Vector3D normalize(Vector3D vecIn) {
		return EUtil.nullDoR(vecIn, magnitude(vecIn), (v, l) -> { v.x /= l; v.y /= l; v.z /= l; }, vecIn);
	}
}
