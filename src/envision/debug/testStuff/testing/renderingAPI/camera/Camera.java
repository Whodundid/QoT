package envision.debug.testStuff.testing.renderingAPI.camera;

import envision.engine.inputHandlers.Keyboard;
import envision.engine.inputHandlers.Mouse;
import eutil.math.ENumUtil;
import eutil.math.vectors.Vec3f;

public class Camera {
	
	/** False so that old values can be initialized. */
	private boolean set = false;
	private double mX_old, mY_old;
	private double mX, mY;
	
	private Vec3f position = new Vec3f(0, 0, 0);
	private Vec3f rotation = new Vec3f(0, 0, 0);
	
	public void update() {
		if (!set) {
			mX_old = Mouse.getMx();
			mY_old = Mouse.getMy();
			set = true;
		}
		
		//float x = (float) Math.sin(Math.toRadians(rotation.y)) * Settings.moveSpeed;
		//float z = (float) Math.cos(Math.toRadians(rotation.y)) * Settings.moveSpeed;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) position.z -= 0.01f;
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) position.x -= 0.01f;
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) position.z += 0.01f;
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) position.x += 0.01f;
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) position = Vec3f.add(position, new Vec3f(0, 0.01f, 0));
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) position = Vec3f.add(position, new Vec3f(0, -0.01f, 0));
		
		
		mX = Mouse.getMx();
		mY = Mouse.getMy();
		
		float dX = (float) (mX - mX_old);
		float dY = (float) (mY - mY_old);
		
		mX_old = mX;
		mY_old = mY;
		
		//if (Game.getWindow().isCursorLocked()) {
			rotation = Vec3f.add(rotation, new Vec3f(dY, dX, 0).scale(0.1f));
			rotation = new Vec3f(ENumUtil.clamp(rotation.x, -90f, 90f), rotation.y, 0);
		//}
			
		//System.out.println(position);
	}
	
	public Vec3f getPosition() { return position; }
	public Vec3f getRotation() { return rotation; }
	
	public float getPitch() { return rotation.x; }
	public float getYaw() { return rotation.y; }
	public float getRoll() { return rotation.z; }
	
}
