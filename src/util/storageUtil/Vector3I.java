package util.storageUtil;

import util.EUtil;

//Author: Hunter Bragg

/**
 * The {@code Vector3I} class is a data type containing three individual {@code Integer} values: x, y, and z.
 * <blockquote><pre>
 *     Vector3I vec = new Vector3I(x, y, z);
 * </pre></blockquote><p>
 * @author Hunter Bragg
 */
public class Vector3I {
	
	public long x = 0, y = 0, z = 0;
	
	//---------------------
	//Vector3I Constructors
	//---------------------
	
	public Vector3I() { set(0, 0 , 0);  }
	public Vector3I(Vector3I vecIn) { set(vecIn.x, vecIn.y, vecIn.z);  }
	public Vector3I(Vector3D vecIn) { set(vecIn.x, vecIn.y, vecIn.z); }
	public Vector3I(double x, double y, double z) { set((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z)); }
	public Vector3I(long x, long y, long z) { set(x, y, z); }
	
	//----------------
	//Object Overrides
	//----------------
	
	@Override public String toString() { return "[" + x + ", " + y + ", " + z + "]"; }
	
	//----------------
	//Vector3I Methods
	//----------------
	
	public Vector3I clear() { x = 0; y = 0; z = 0; return this; }
	public Vector3D asVector3D() { return new Vector3D(x, y, z); }
	
	public boolean compare(Vector3I in) { return (in != null) ? (x == in.x && y == in.y && z == in.z) : false; }
	public boolean compare(long xIn, long yIn, long zIn) { return (x == xIn && y == yIn && z == zIn); }
	
	//--------------------
	//Vector3I Vector Math
	//--------------------
	
	public Vector3I add(Vector3I in) { return EUtil.nullDoR(in, v -> set(x + in.x, y + in.y, z + in.z), this); }
	public Vector3I subtract(Vector3I in) { return EUtil.nullDoR(in, v -> set(x - in.x, y - in.y, z - in.z), this); }
	public Vector3I multiply(Vector3I in) { return EUtil.nullDoR(in, v -> set(x * in.x, y * in.y, z * in.z), this); }
	public Vector3I divide(Vector3I in) { return EUtil.nullDoR(in, v -> set(x / in.x, y / in.y, z / in.z), this); }
	
	//----------------
	//Vector3I Getters
	//----------------
	
	public long getX() { return x; }
	public long getY() { return y; }
	public long getZ() { return z; }
	
	//----------------
	//Vector3I Setters
	//----------------
	
	public Vector3I setX(long xIn) { x = xIn; return this; }
	public Vector3I setY(long yIn) { y = yIn; return this; }
	public Vector3I setZ(long zIn) { z = zIn; return this; }
	
	public Vector3I set(Vector3I vecIn) { return EUtil.nullDoR(vecIn, v -> set(v.x, v.y, v.z), this); }
	public Vector3I set(Vector3D vecIn) { return EUtil.nullDoR(vecIn, v -> set(v.x, v.y, v.z), this); }
	public Vector3I set(Number xIn, Number yIn, Number zIn) { x = xIn.intValue(); y = yIn.intValue(); z = zIn.intValue(); return this; }
	
	//---------------------------
	//Vector3I Static Vector Math
	//---------------------------
	
	public static Vector3I add(Vector3I in1, Vector3I in2) { return EUtil.nullApplyR(in1, in2, (a, b) -> new Vector3I(a.x + b.x, a.y + b.y, a.z + b.z), null); }
	public static Vector3I subtract(Vector3I in1, Vector3I in2) { return EUtil.nullApplyR(in1, in2, (a, b) -> new Vector3I(a.x - b.x, a.y - b.y, a.z - b.z), null); }
	public static Vector3I multiply(Vector3I in1, Vector3I in2) { return EUtil.nullApplyR(in1, in2, (a, b) -> new Vector3I(a.x * b.x, a.y * b.y, a.z * b.z), null); }
	public static Vector3I divide(Vector3I in1, Vector3I in2) { return EUtil.nullApplyR(in1, in2, (a, b) -> new Vector3I(a.x / b.x, a.y / b.y, a.z / b.z), null); }
	
}
