package envision.engine.rendering;

import org.joml.Matrix4f;

import eutil.math.vectors.Vec2f;

public class RenderMath {
	
	public static Matrix4f orthoMatrix(float right, float left, float top, float bottom, float far, float near) {
//		Matrix4f m = new Matrix4f();
//		
//		m.m00(2.0f / (right - left));
//		m.m01(0.0f);
//		m.m02(0.0f);
//		m.m02(0.0f);
//		
//		m.m10(0.0f);
//		m.m11(2.0f / (top - bottom));
//		m.m12(0.0f);
//		m.m13(0.0f);
//		
//		m.m20(0.0f);
//		m.m21(0.0f);
//		m.m22(-2.0f / (far - near));
//		m.m23(0.0f);
//		
//		m.m30(-(right + left) / (right - left));
//		m.m31(-(top + bottom) / (top - bottom));
//		m.m32(-(far + near) / (far - near));
//		m.m33(1.0f);
//		
//		return m;
		return new Matrix4f().ortho2D(left, right, bottom, top);
	}
	
	public static Matrix4f translate(Vec2f t) {
//		Matrix4f translate = new Matrix4f();
//		
//		translate.set( 1f ,  0f , 0f , 0f ,
//				       0f ,  1f , 0f , 0f ,
//				       0f ,  0f , 1f , 0f ,
//				      t.x , t.y , 0f , 1f );
//
//		return translate;
//		
		return new Matrix4f().translate(t.x, t.y, 0.0f);
	}
	
	public static Matrix4f scale(Vec2f s) {
//		Matrix4f scaled = new Matrix4f();
//		
//		scaled.set(s.x ,  0f , 0f , 0f ,
//				    0f , s.y , 0f , 0f ,
//				    0f ,  0f , 1f , 0f ,
//				    0f ,  0f , 0f , 1f );
//		
//		return scaled;
		return new Matrix4f().scale(s.x, s.y, 1.0f);
	}
	
	public static Matrix4f rotate(float angle, Vec2f axis) {
//		Matrix4f ret = new Matrix4f();
//		
//		float x = axis.x;
//		float y = axis.y;
//		float z = 0.0f;
//		float cos = (float) Math.cos(Math.toRadians(angle));
//		float sin = (float) Math.sin(Math.toRadians(angle));
//		float C = 1f - cos;
//		
//		ret.set( x*x*C + cos     ,   x*y*C - z*sin   ,   x*z*C + y*sin   ,   0f   ,
//				 y*x*C + z*sin   ,   y*y*C + cos     ,   y*z*C - x*sin   ,   0f   ,
//		         z*x*C - y*sin   ,   z*y*C + x*sin   ,   z*z*C + cos     ,   0f   ,
//		         0f              ,   0f              ,   0f              ,   1f   );
//		
//		return ret;
		return new Matrix4f().rotate(angle, axis.x, axis.y, 0.0f);
	}
	
	public static Matrix4f view(Vec2f pos, Vec2f rot) {
		Matrix4f ret = translate(new Vec2f(-pos.x, -pos.y));
		ret = multiply(ret, rotate(rot.y, new Vec2f(0f, 1f))); //ORDER MATTERS!
		ret = multiply(ret, rotate(rot.x, new Vec2f(1f, 0f)));
		return ret;
	}
	
	public static Matrix4f modelMatrix(Vec2f pos, Vec2f rot, Vec2f scale) {
		Matrix4f ret = translate(new Vec2f(pos.x, pos.y));
		ret = multiply(ret, rotate(rot.y, new Vec2f(0f, 1f)));
		ret = multiply(ret, rotate(rot.x, new Vec2f(1f, 0f)));
		ret = multiply(ret, scale(scale));
		return ret;
	}
	
	public static Matrix4f multiply(Matrix4f a, Matrix4f b) {
		return a.mul(b);
	}
	
}
