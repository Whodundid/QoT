package engine.debug.testStuff.testRender;

import eutil.math.Vec2f;
import eutil.math.Vec3f;
import eutil.math.Vec4f;

public class Vertex {
	
	private Vec3f position;
	private Vec4f color;
	private Vec2f textureCoord;
	private boolean isTexture;
	
	public Vertex(Vec3f posIn, Vec4f colorIn) {
		position = posIn;
		color = colorIn;
		textureCoord = new Vec2f();
		isTexture = false;
	}
	
	public Vertex(Vec3f posIn, Vec2f textureCoordIn) {
		position = posIn;
		textureCoord = textureCoordIn;
		color = new Vec4f();
		isTexture = true;
	}
	
	public Vec3f getPosition() { return position; }
	public Vec4f getColor() { return color; }
	public Vec2f getTextureCoords() { return textureCoord; }
	public boolean isTexture() { return isTexture; }
	
}
