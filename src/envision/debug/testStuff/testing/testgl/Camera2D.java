package envision.debug.testStuff.testing.testgl;

import envision.debug.testStuff.testing.renderingAPI.math.Matrix4f;
import envision.engine.inputHandlers.WindowResizeListener;
import eutil.math.dimensions.EDimensionf;
import eutil.math.vectors.Vec2f;
import qot.QoT;

public class Camera2D {
	
	//========
	// Fields
	//========
	
	public Vec2f focus;
	public float zoom;
	
	//==============
	// Constructors
	//==============
	
	public Camera2D(Vec2f focusIn, float zoomIn) {
		focus = focusIn;
		zoom = zoomIn;
	}
	
	//=========
	// Methods
	//=========
	
	public Matrix4f getProjectionMatrix(EDimensionf windowDims) {
		//float left = focus.x - windowDims.width / 2f;
		float left = focus.x - 1280f;
		float right = focus.x + 1920f;
		float top = focus.y - 900f;
		float bottom = focus.y + 900f;
		float near = 0.01f;
		float far = 100.0f;
		
		Matrix4f ortho = new Matrix4f();
		ortho.m00 = 2.0f / (right - left);		ortho.m10 = 0.0f;						ortho.m20 = 0.0f;					ortho.m30 = (left + right) / (left - right);
		ortho.m01 = 0.0f;						ortho.m11 = 2.0f / (top - bottom);		ortho.m21 = 0.0f;					ortho.m31 = (top + bottom) / (bottom - top);
		ortho.m02 = 0.0f;						ortho.m12 = 0.0f;						ortho.m22 = -2.0f / (near - far);	ortho.m32 = near / (near - far);
		ortho.m03 = 0.0f;						ortho.m13 = 0.0f;						ortho.m23 = 0.0f;					ortho.m33 = 1.0f;
		
		Matrix4f scale = new Matrix4f();
		scale.m00 = zoom;		scale.m10 = 0.0f;		scale.m20 = 0.0f;		scale.m30 = 0.0f;
		scale.m01 = 0.0f;		scale.m11 = zoom;		scale.m21 = 0.0f;		scale.m31 = 0.0f;
		scale.m02 = 0.0f;		scale.m12 = 0.0f;		scale.m22 = zoom;		scale.m32 = 0.0f;
		scale.m03 = 0.0f;		scale.m13 = 0.0f;		scale.m23 = 0.0f;		scale.m33 = 1.0f;
		
		Matrix4f result = Matrix4f.mul(ortho, scale, null);
		//System.out.println(windowDims.midX + " : " + windowDims.midY);
		//System.out.println(windowDims.midX + " : " + QoT.getHeight() * 0.5f);
		return result;
	}
	
	//=========
	// Getters
	//=========
	
	public Vec2f getFocus() { return focus; }
	public float getZoom() { return zoom; }
	
	//=========
	// Setters
	//=========
	
	public void setFocus(Vec2f focusIn) { focus = focusIn; }
	public void setZoom(float zoomIn) { zoom = zoomIn; }
	
}
