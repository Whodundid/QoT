package envision.engine.rendering;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;
import org.lwjgl.opengl.GL46;

import envision.Envision;
import envision.engine.rendering.shaders.ShaderProgram;
import envision.engine.rendering.shaders.Shaders;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import qot.QoT;

public class OpenGLBatchManager implements IBatchManager {
	
	public static final int VERTEX_SIZE = 10;
	
	private int vao, vbo, ibo;
	private ShaderProgram shader;
	private Matrix4f projection = new Matrix4f();
	private int[] elementData;
	
//	private int batchSize;
	private ByteBuffer byteBuffer;
	private int nextVertOffset = 0;
	private int totalVerts = 0;
	private int totalElements = 0;
	private int[] texSlots;
	private EList<GameTexture> textures;
	
	private static final int posOffset = 0;
	private static final int colorOffset = 12;
	private static final int texCoordOffset = 28;
	private static final int texIdOffset = 36;
	
	private static final int[] indices = {
		0, 1, 3,
		1, 2, 3
	};
	
	//==============
	// Constructors
	//==============
	
	/**
	 * Allocates a new BatchManager with 2MB (in bytes) and 16 texture slots.
	 */
//	public OpenGLBatchManager() {
//		this(2097152, 16, Shaders.basic);
//	}
	
	/**
	 * Allocates a new BatchManager with a specified amount of internal buffer
	 * memory.
	 * 
	 * @param bufferSize The size in bytes to allocate to this manager
	 * @param texSlots   The number of texture slots to allocate within OpenGL
	 */
//	public OpenGLBatchManager(int bufferSize, int texSlots, ShaderProgram shader) {
//		this.byteBuffer = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
//		this.texSlots = new int[texSlots];
//		this.shader = shader;
//		
//		// load the texture slot array
//		for (int i = 0; i < texSlots; i++) {
//			this.texSlots[i] = i;
//		}
//		
//		textures = EList.newList();
//	}
	
	public OpenGLBatchManager() {
		this(2097152, 16, Shaders.basic);
	}
	
	public OpenGLBatchManager(int bufferSize, int texSlotsIn, ShaderProgram shaderIn) {
		byteBuffer = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
		texSlots = new int[texSlotsIn];
		shader = shaderIn;
		
		// load the texture slot array
		for (int i = 0; i < texSlotsIn; i++) {
			texSlots[i] = i;
		}
		
		textures = EList.newList();
		
		initBatch();
	}
	
	private void initBatch() {
		// set up projection matrix
		var dims = QoT.getWindowDims();
		
		projection.identity();
		projection.ortho(0, (float) dims.width, 0, (float) dims.height, 1f, 100f);
		
		// setup VAO
//		vao = GL45.glCreateVertexArrays();
//		GL30.glBindVertexArray(vao);
//		
//		vbo = GL45.glCreateBuffers();
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, byteBuffer.capacity(), GL15.GL_DYNAMIC_DRAW);
//		
//		// setup IBO up front based on batch size
////		int elementSize = batchSize * 3;
////		int[] elementBuffer = new int[elementSize];
////		
////		for (int i = 0; i < elementSize; i++) {
////			elementBuffer[i] = indices[(i % 6)] + ((i / 6) * 4);
////		}
//		
//		int[] indices = new int[totalElements * 6];
//		int offset = 0;
//		
//		for (int i = 0; i < totalElements * 6; i += 6) {
//			indices[i + 0] = 0 + offset;
//			indices[i + 1] = 1 + offset;
//			indices[i + 2] = 2 + offset;
//			
//			indices[i + 3] = 2 + offset;
//			indices[i + 4] = 3 + offset;
//			indices[i + 5] = 0 + offset;
//			
//			offset += 4;
//		}
//		
//		int ibo = GL46.glGenBuffers();
//		GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, ibo);
//		GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indices, GL46.GL_STATIC_DRAW);
//		
//		// setup VBO
//		
//		//we use 10 * Float.BYTES as our stride here so that we can fit all of the following bytes in one vertex buffer
//		// position (x, y, z) 		color (r, g, b, a) 				texture coord (tx, ty)		texture ID (tid)
//		// -1.5f, -0.5f, 0.0f, 		0.18f, 0.6f, 0.96f, 1.0f, 		0.0f, 0.f,					0f
//		
//		int stride = 10 * Float.BYTES;
//		
//		GL45.glEnableVertexArrayAttrib(vbo, 0);
//		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, stride, 0);
//		//12 because we are looking at the index of the first position float (x) at index (0)
//		
//		GL45.glEnableVertexArrayAttrib(vbo, 1);
//		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, stride, 12);
//		//12 because we are looking at the index of the first color float (r) at index (3)
//		
//		GL45.glEnableVertexArrayAttrib(vbo, 2);
//		GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, stride, 28);
//		//28 because we are looking at the index of the first texture float (tx) at index (7)
//		
//		GL45.glEnableVertexArrayAttrib(vbo, 3);
//		GL20.glVertexAttribPointer(3, 1, GL11.GL_FLOAT, false, stride, 36);
//		//36 because we are looking at the index of the starting point of (tid) at
	}
	
	//=========
	// Methods
	//=========
	
	private void drawRect(double sx, double sy, double ex, double ey, EColors color) {
		drawRect(sx, sy, ex, ey, color.intVal);
	}
	private void drawRect(double sxIn, double syIn, double exIn, double eyIn, int colorIn) {
		if ((totalVerts + 4) > byteBuffer.capacity()) {
			flush();
		}
		
		float sx = tdx((float) sxIn);
		float sy = tdy((float) syIn);
		float ex = tdx((float) exIn);
		float ey = tdy((float) eyIn);
		
		float r = (colorIn >> 16 & 255) / 255.0F;
		float g = (colorIn >> 8 & 255) / 255.0F;
		float b = (colorIn & 255) / 255.0F;
		float f = (colorIn >> 24 & 255) / 255.0F;
		
		vert(sx, sy, r, g, b, f);
		vert(sx, ey, r, g, b, f);
		vert(ex, ey, r, g, b, f);
		vert(ex, sy, r, g, b, f);
		
		totalVerts += 4;
		totalElements++;
	}
	
	public void drawTexture(GameTexture texture, double sxIn, double syIn, double exIn, double eyIn) {
		drawTexture(texture, sxIn, syIn, exIn, eyIn, EColors.white.intVal);
	}
	public void drawTexture(GameTexture texture, double sxIn, double syIn, double exIn, double eyIn, int colorIn) {
		if ((totalVerts + 4 * VERTEX_SIZE) > byteBuffer.capacity()) {
			flush();
		}
		
		if (texture != null && textures.notContains(texture)) {
			textures.add(texture);
		}
		
		int texID = 0;
		if (texture != null) {
			for (int i = 0; i < textures.size(); i++) {
				if (textures.get(i) == texture) {
					texID = i + 1;
					break;
				}
			}
			
			if (texID == 0) {
				flush();
				for (int i = 0; i < textures.size(); i++) {
					if (textures.get(i) == texture) {
						texID = i + 1;
						break;
					}
				}
			}
		}
		
		float sx = tdx((float) sxIn);
		float sy = tdy((float) syIn);
		float ex = tdx((float) exIn);
		float ey = tdy((float) eyIn);
		
		float r = (colorIn >> 16 & 255) / 255.0F;
		float g = (colorIn >> 8 & 255) / 255.0F;
		float b = (colorIn & 255) / 255.0F;
		float f = (colorIn >> 24 & 255) / 255.0F;
		
		vert(sx, sy, 0f, r, g, b, f, 0f, 0f, (float) texID);
		vert(sx, ey, 0f, r, g, b, f, 0f, 1f, (float) texID);
		vert(ex, ey, 0f, r, g, b, f, 1f, 1f, (float) texID);
		vert(ex, sy, 0f, r, g, b, f, 1f, 0f, (float) texID);
		
		totalVerts += 4;
		totalElements++;
	}
	
	public void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color) {
		drawTexture(texture, x, y, w, h, tX, tY, tW, tH, color, false);
	}
	public void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color, boolean flip) {
		if ((totalVerts + 4 * VERTEX_SIZE) > byteBuffer.capacity()) {
			flush();
		}
		
		if (texture != null && textures.notContains(texture)) {
			textures.add(texture);
		}
		
		int texID = 0;
		if (texture != null) {
			for (int i = 0; i < textures.size(); i++) {
				if (textures.get(i) == texture) {
					texID = i + 1;
					break;
				}
			}
			
			if (texID == 0) {
				flush();
				for (int i = 0; i < textures.size(); i++) {
					if (textures.get(i) == texture) {
						texID = i + 1;
						break;
					}
				}
			}
		}
		
		float xVal = (float) (tX / (float) texture.getWidth());
		float yVal = (float) (tY / (float) texture.getHeight());
		float wVal = (float) (tW / (float) texture.getWidth());
		float hVal = (float) (tH / (float) texture.getHeight());
		
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		float f = (color >> 24 & 255) / 255.0F;
		
		//garbage duct-tape font bottom fix
		//hVal -= 0.005f;
		
		float sx = tdx((float) x);
		float sy = tdy((float) y);
		float ex = tdx((float) (x + w));
		float ey = tdy((float) (y + h));
		
		float tsx = xVal;
		float tex = xVal + wVal;
		float tsy = yVal;
		float tey = yVal + hVal;
		
		float draw_tsx = (flip) ? tex : tsx;
		float draw_tsy = (flip) ? tsy : tsy;
		float draw_tex = (flip) ? tsx : tex;
		float draw_tey = (flip) ? tey : tey;
		
		vert(sx, sy, 0f, r, g, b, f, draw_tsx, draw_tsy, (float) texID);
		vert(sx, ey, 0f, r, g, b, f, draw_tsx, draw_tey, (float) texID);
		vert(ex, ey, 0f, r, g, b, f, draw_tex, draw_tey, (float) texID);
		vert(ex, sy, 0f, r, g, b, f, draw_tex, draw_tsy, (float) texID);
		
		totalVerts += 4;
		totalElements++;
	}
	
	//================
	// Helper Methods
	//================
	
	public void draw() {
		flush();
	}
	
	/** 'to double for x' converts a given value into a corresponding double value between -1.0f and 1.0f based on window x size. */
	public static float tdx(float valIn) {
		float midX = Envision.getWidth() * 0.5f;
		return (valIn * (float) Envision.getGameScale() - midX) / midX;
	}
	
	/** 'to double for y' converts a given value into a corresponding double value between -1.0f and 1.0f based on window y size. */
	public static float tdy(float valIn) {
		float midY = Envision.getHeight() * 0.5f;
		return (midY - (valIn * (float) Envision.getGameScale())) / midY;
	}
	
	//==================
	// Internal Methods
	//==================
	
	private void vert(float x, float y, float r, float g, float b, float f) {
		vert(x, y, 0.0f, r, g, b, f, 0.0f, 0.0f, 0.0f);
	}
	private void vert(float x, float y, float z, float r, float g, float b, float f) {
		vert(x, y, z, r, g, b, f, 0.0f, 0.0f, 0.0f);
	}
	private void vert(float x, float y, float r, float g, float b, float f, float tx, float ty, float tid) {
		vert(x, y, 0.0f, r, g, b, f, tx, ty, tid);
	}
	private void vert(float x, float y, float z, float r, float g, float b, float f, float tx, float ty, float tid) {
		int v = nextVertOffset;
		
		byteBuffer.limit(v + 40);
		
		// position
		byteBuffer.putFloat(v + posOffset + 0, x);
		byteBuffer.putFloat(v + posOffset + 4, y);
		byteBuffer.putFloat(v + posOffset + 8, z);
		// color
		byteBuffer.putFloat(v + colorOffset + 0, r);
		byteBuffer.putFloat(v + colorOffset + 4, g);
		byteBuffer.putFloat(v + colorOffset + 8, b);
		byteBuffer.putFloat(v + colorOffset + 12, f);
		// texture coords
		byteBuffer.putFloat(v + texCoordOffset + 0, tx);
		byteBuffer.putFloat(v + texCoordOffset + 4, ty);
		// texture id
		byteBuffer.putFloat(v + texIdOffset + 0, tid);
		
		nextVertOffset += 40;
	}
	
	/**
	 * Uploads the current buffer's contents to the internally managed VAO and
	 * then draws them to the screen.
	 */
	private void flush() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
		
		shader.enableAttribs();
		shader.bind();
		
		drawRect(100, 100, 400, 300, EColors.turquoise);
		drawRect(160, 500, 800, 700, EColors.yellow);
		
		GLSettings.enableAlpha();
		GLSettings.enableBlend();
		GLSettings.blendFunc();
		
		for (int i = 0; i < textures.size(); i++) {
			GL46.glActiveTexture(GL13.GL_TEXTURE0 + i);
			TextureSystem.getInstance().bind(textures.get(i));
		}
		shader.setUniform("texSamplers", texSlots);
		shader.setUniform("u_projection", projection);
		
		uploadToVBO();
		
		GLCall(() -> GL46.glBindVertexArray(vao));
		GLCall(() -> GL46.glDrawElements(GL46.GL_TRIANGLES, totalElements * 6, GL46.GL_UNSIGNED_INT, 0));
		
		for (int i = 0; i < textures.size(); i++) {
			GL46.glActiveTexture(GL13.GL_TEXTURE0 + i);
			TextureSystem.getInstance().unbind(textures.get(i));
		}
		
		GLSettings.disableAlpha();
		GLSettings.disableBlend();
		
		GL46.glBindVertexArray(0);
		resetBatch();
		
		shader.unbind();
		shader.disableAttribs();
	}
	
	private void uploadToVBO() {
		GL30.glBindVertexArray(vao);

		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		
		vbo = GL45.glCreateBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, byteBuffer, GL15.GL_STATIC_DRAW);
		
		//we use 10 * Float.BYTES as our stride here so that we can fit all of the following bytes in one vertex buffer
		// position (x, y, z) 		color (r, g, b, a) 				texture coord (tx, ty)		texture ID (tid)
		// -1.5f, -0.5f, 0.0f, 		0.18f, 0.6f, 0.96f, 1.0f, 		0.0f, 0.f,					0f
		
		GL45.glEnableVertexArrayAttrib(vbo, 0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 10 * Float.BYTES, 0);
		//12 because we are looking at the index of the first position float (x) at index (0)
		
		GL45.glEnableVertexArrayAttrib(vbo, 1);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 10 * Float.BYTES, 12);
		//12 because we are looking at the index of the first color float (r) at index (3)
		
		GL45.glEnableVertexArrayAttrib(vbo, 2);
		GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 10 * Float.BYTES, 28);
		//28 because we are looking at the index of the first texture float (tx) at index (7)
		
		GL45.glEnableVertexArrayAttrib(vbo, 3);
		GL20.glVertexAttribPointer(3, 1, GL11.GL_FLOAT, false, 10 * Float.BYTES, 36);
		//36 because we are looking at the index of the starting point of (tid) at index (9)
		
		int[] indices = new int[totalElements * 6];
		int offset = 0;
		
		for (int i = 0; i < totalElements * 6; i += 6) {
			indices[i + 0] = 0 + offset;
			indices[i + 1] = 1 + offset;
			indices[i + 2] = 2 + offset;
			
			indices[i + 3] = 2 + offset;
			indices[i + 4] = 3 + offset;
			indices[i + 5] = 0 + offset;
			
			offset += 4;
		}
		
		ibo = GL45.glCreateBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GLCall(() -> GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW));
	}
	
	private void resetBatch() {
		byteBuffer.rewind();
		byteBuffer.limit(0);
		totalVerts = 0;
		totalElements = 0;
		nextVertOffset = 0;
		textures.clear();
	}
	
	private static void GLCall(Runnable r) {
		Envision.getRenderEngine().getRenderingContext().call(r);
	}
	
	public void setShader(ShaderProgram shaderIn) {
		shader = shaderIn;
	}
	
}
