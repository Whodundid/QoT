package envision.engine.rendering.batching;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;

import envision.Envision;
import envision.engine.rendering.Camera;
import envision.engine.rendering.GLSettings;
import envision.engine.rendering.shaders.ShaderProgram;
import envision.engine.rendering.shaders.Shaders;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import eutil.datatypes.util.EList;
import eutil.math.vectors.Vec2f;
import eutil.math.vectors.Vec4f;

public class RenderBatch {
	
	int vao, vbo, ibo;
	ShaderProgram shader;
	boolean alreadyDrawn = false;
	boolean isScissorBatch = false;
	double scissorX, scissorY, scissorWidth, scissorHeight;
	int batchLayer;
	int batchLayerIndex;
	boolean isClosed;
	
	int nextVertOffset = 0;
	int totalElements = 0;
	int[] texSlots;
	EList<GameTexture> textures;

	int maxBatchSize;
	float[] vertices;
	
	static final int VERTEX_SIZE = 10;
	static final int POS_OFFSET = 0;
	static final int COLOR_OFFSET = 3;
	static final int TEX_COORD_OFFSET = 7;
	static final int TEX_ID_OFFSET = 9;
	
	public RenderBatch() { this(100, 16, Shaders.basic); }
	public RenderBatch(int maxIn, int texSlotsIn) { this(maxIn, 16, Shaders.basic); }
	public RenderBatch(int maxIn, int texSlotsIn, ShaderProgram shaderIn) {
		maxBatchSize = maxIn;
		texSlots = new int[texSlotsIn];
		vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
		textures = EList.newList();
		shader = shaderIn;
		
		// load the texture slot array
		for (int i = 0; i < texSlotsIn; i++) {
			texSlots[i] = i;
		}
		
		initBatch();
	}
	
	private void initBatch() {
		// setup VAO
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL15.GL_DYNAMIC_DRAW);
		
		// setup IBO up front
		int size = maxBatchSize * 6;
		int[] indices = new int[size];
		int offset = 0;
		
		for (int i = 0; i < size; i += 6) {
			indices[i + 0] = 0 + offset;
			indices[i + 1] = 1 + offset;
			indices[i + 2] = 2 + offset;
			
			indices[i + 3] = 2 + offset;
			indices[i + 4] = 3 + offset;
			indices[i + 5] = 0 + offset;
			
			offset += 4;
		}
		
		int ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indices, GL46.GL_STATIC_DRAW);
		
		// setup VBO
		
		//we use 12 * Float.BYTES as our stride here so that we can fit all of the following bytes in one vertex buffer
		// position (x, y, z) 		color (r, g, b, a) 				texture coord (tx, ty)		texture ID (tid)
		// -1.5f, -0.5f, 0.0f, 		0.18f, 0.6f, 0.96f, 1.0f, 		0.0f, 0.0f,					0f
		
		int stride = VERTEX_SIZE * Float.BYTES;
		
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, stride, POS_OFFSET * Float.BYTES);
		GL20.glEnableVertexAttribArray(0);
		//12 because we are looking at the index of the first position float (x) at index (0)
		
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, stride, COLOR_OFFSET * Float.BYTES);
		GL20.glEnableVertexAttribArray(1);
		//12 because we are looking at the index of the first color float (r) at index (3)
		
		GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, stride, TEX_COORD_OFFSET * Float.BYTES);
		GL20.glEnableVertexAttribArray(2);
		//28 because we are looking at the index of the first texture float (tx) at index (7)
		
		GL20.glVertexAttribPointer(3, 1, GL11.GL_FLOAT, false, stride, TEX_ID_OFFSET * Float.BYTES);
		GL20.glEnableVertexAttribArray(3);
		//36 because we are looking at the index of the starting point of (tid) at index (9)
	}
	
	//================
	// Helper Methods
	//================
	
	public boolean hasRoom() { return !isClosed && totalElements < maxBatchSize; }
	public boolean isEmpty() { return totalElements == 0; }
	public boolean isScissorBatch() { return isScissorBatch; }
	public int size() { return totalElements; }
	public int remaining() { return maxBatchSize - totalElements; }
	
	public boolean hasRoomOnLayerIndex(int layerIndex) {
		return layerIndex == batchLayerIndex && hasRoom();
	}
	
	public void setScissorBatch(boolean val) {
		isScissorBatch = val;
	}
	
	public void setScissorBounds(double startX, double startY, double width, double height) {
		scissorX = startX;
		scissorY = startY;
		scissorWidth = width;
		scissorHeight = height;
	}
	
	public void applyScissorBoundsFromGLSettings() {
		scissorX = GLSettings.scissorX;
		scissorY = GLSettings.scissorY;
		scissorWidth = GLSettings.scissorWidth;
		scissorHeight = GLSettings.scissorHeight;
	}
	
	public boolean containsTexture(GameTexture texture) {
		for (int i = 0; i < textures.size() && i < texSlots.length; i++) {
			var tex = textures.get(i);
			if (tex == texture) return true;
		}
		return false;
	}
	
	public boolean canAddTexture(GameTexture texture) {
		return !containsTexture(texture) && textures.size() < texSlots.length;
	}
	
	/**
	 * Adds the given texture to this batch if there is room
	 * 
	 * @return The index of where this texture is now located
	 */
	int addTexture(GameTexture texture) {
		if (texture != null && textures.notContains(texture)) {
			if (textures.size() >= texSlots.length) {
				System.out.println("Tex Limit Reached");
				return -1;
			}
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
				System.out.println("Tex limit reached!");
				return -1;
			}
		}
		
		return texID;
	}
	
	public void draw() { draw(false); }
	public void draw(boolean force) {
		flush(force);
	}
	
	//==================
	// Internal Methods
	//==================
	
	
	void vert(float x, float y, float r, float g, float b, float f) {
		vert(x, y, 0.0f, r, g, b, f, 0.0f, 0.0f, 0.0f);
	}
	void vert(float x, float y, float z, float r, float g, float b, float f) {
		vert(x, y, 0.0f, r, g, b, f, 0.0f, 0.0f, 0.0f);
	}
	void vert(float x, float y, float r, float g, float b, float f, float tx, float ty, float tid) {
		vert(x, y, 0.0f, r, g, b, f, 0.0f, 0.0f, 0.0f);
	}
	void vert(float x, float y, float z, float r, float g, float b, float f, float tx, float ty, float tid) {
		int v = nextVertOffset;
		
		// position
		vertices[v + 0] = x;
		vertices[v + 1] = y;
		vertices[v + 2] = z;
		// color
		vertices[v + 3] = r;
		vertices[v + 4] = g;
		vertices[v + 5] = b;
		vertices[v + 6] = f;
		// texture coords
		vertices[v + 7] = tx;
		vertices[v + 8] = ty - 0.00001f;
		// texture id
		vertices[v + 9] = tid;
		
		nextVertOffset += VERTEX_SIZE;
	}
	
	/**
	 * Uploads the current buffer's contents to the internally managed VAO and
	 * then draws them to the screen.
	 */
	void flush(boolean force) {
		if (!force && alreadyDrawn) return;
		if (totalElements == 0) return;
		
		shader.enableAttribs();
		shader.bind();
		
//		System.out.println("Textures used: " + textures.size());
//		System.out.println(totalElements);
		
		GLSettings.enableAlpha();
		GLSettings.enableBlend();
		GLSettings.blendFunc();
		
		for (int i = 0; i < textures.size(); i++) {
			GL46.glActiveTexture(GL13.GL_TEXTURE0 + i);
			TextureSystem.getInstance().bind(textures.get(i));
		}
		
		Camera cam = Envision.getRenderEngine().getCamera();
		var player = Envision.thePlayer;
		var world = Envision.theWorld;
		boolean underground = (world != null) ? world.isUnderground() : false;
		
		Matrix4f u_projection = cam.getProjection();
		Matrix4f u_view = cam.getView();
		Vec2f playerPos = new Vec2f(); // default to (0, 0)
		
		float tileSize = (world != null) ? world.getTileWidth() : 32.0f;
		float camZoom = (float) ((world != null) ? world.getCameraZoom() : 1.0f);
		float lightDist = 100000f; // really high because baller~
		float viewDist = 1000f; // the number of tiles that the entity can see out from it
		
		if (player != null) {
			playerPos.set(Envision.getWidth() >> 1, Envision.getHeight() >> 1);
			if (Envision.isPaused()) viewDist = 1000f;
			else viewDist = 7.5f;
		}
		
		if (underground) lightDist = viewDist * tileSize * camZoom;
		
		shader.setUniform("texSamplers", texSlots);
		shader.setUniform("u_projection", u_projection);
		shader.setUniform("u_view", u_view);
		shader.setUniform("u_playerPos", playerPos);
		shader.setUniform("u_lightDist", lightDist);
		shader.setUniform("u_bezierVals", new Vec4f(0.0, 0.1, 0.5, 1.0));
		shader.setUniform("u_underground", underground == true ? 1 : 0);
		
		uploadToVBO();
		
		if (isScissorBatch) {
			GLSettings.enableScissor();
			GLSettings.setScissorBounds(scissorX, scissorY, scissorWidth, scissorHeight);
			GLSettings.scissor();
		}
		
		GLCall(() -> GL30.glBindVertexArray(vao));
		GLCall(() -> GL11.glDrawElements(GL11.GL_TRIANGLES, totalElements * 6, GL11.GL_UNSIGNED_INT, 0));
		
		if (isScissorBatch) {
			GLSettings.disableScissor();
		}
		
		for (int i = 0; i < textures.size(); i++) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
			TextureSystem.getInstance().unbind(textures.get(i));
		}
		
		GLSettings.disableAlpha();
		GLSettings.disableBlend();
		
		GL46.glBindVertexArray(0);
		
		shader.unbind();
		shader.disableAttribs();
		
		alreadyDrawn = true;
	}
	
	private void uploadToVBO() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertices);
	}
	
	void resetBatch() {
		totalElements = 0;
		nextVertOffset = 0;
		textures.clear();
		alreadyDrawn = false;
		isScissorBatch = false;
		isClosed = false;
		batchLayer = 0;
		batchLayerIndex = 0;
		
		scissorX = 0;
		scissorY = 0;
		scissorWidth = 0;
		scissorHeight = 0;
	}
	
	private static void GLCall(Runnable r) {
		Envision.getRenderEngine().getRenderingContext().call(r);
	}
	
	public void setShader(ShaderProgram shaderIn) {
		shader = shaderIn;
	}
	
	public void closeBatch() { isClosed = true; }
	public void openBatch() { isClosed = false; }
	
	public int getBatchLayer() { return batchLayer; }
	public int getBatchLayerIndex() { return batchLayerIndex; }
	public void setBatchLayer(int layerNum) { batchLayer = layerNum; }
	public void setBatchLayerIndex(int index) { batchLayerIndex = index; }
	
}
