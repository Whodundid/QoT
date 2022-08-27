package envision.testing.renderingAPI.math;

import envision.testing.renderingAPI.camera.Camera;
import eutil.math.Vec3f;

public class MatrixBuilder {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	public static Matrix4f transform(Vec3f translation, Vec3f rotation, Vec3f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vec3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vec3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vec3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f view(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vec3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vec3f(0, 1, 0), viewMatrix, viewMatrix);
		Vec3f cameraPos = camera.getPosition();
		Vec3f negativeCameraPos = new Vec3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}
	
	public static Matrix4f projection(float fov, float aspect, float near, float far) {
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspect);
		float x_scale = y_scale / aspect;
		float range = far - near;
		
		Matrix4f proj = new Matrix4f();
		proj.m00 = x_scale;
		proj.m11 = y_scale;
		proj.m22 = -((far + near) / range);
		proj.m23 = -1;
		proj.m32 = -((2 * near * far) / range);
		proj.m33 = 0;
		
		return proj;
	}
	
}
