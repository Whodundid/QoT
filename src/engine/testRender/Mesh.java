package engine.testRender;

import engine.renderEngine.textureSystem.GameTexture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
	
	private Vertex[] vertices;
	private int[] indices;
	private GameTexture texture;
	private boolean isTexture;
	
	//vertex array object
	private int vao;
	//position buffer object
	private int pbo;
	//indices buffer object
	private int ibo;
	//color buffer object
	private int cbo;
	//texture buffer object
	private int tbo;
	
	private enum Type { POS, COL, TEX; }
	
	//--------------
	// Constructors
	//--------------
	
	public Mesh(Vertex[] verticesIn, int[] indicesIn) {
		vertices = verticesIn;
		indices = indicesIn;
		isTexture = false;
	}
	
	public Mesh(Vertex[] verticesIn, int[] indicesIn, GameTexture textureIn) {
		vertices = verticesIn;
		indices = indicesIn;
		texture = textureIn;
		isTexture = true;
	}
	
	public void create() {
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		//do position buffer stuff
		pbo = storeData(0, 3, Type.POS);
		//color stuff
		cbo = storeData(1, 4, Type.COL); // 4 because of RGBA
		//texture stuff
		tbo = storeData(2, 2, Type.TEX); // only 2D so 2 instead of 3
		
		//now do indice buffer stuff
		IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
		indicesBuffer.put(indices).flip();
		
		ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		//unbind the buffer -> 0
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	private int storeData(int index, int size, Type typeIn) {
		FloatBuffer theBuffer = MemoryUtil.memAllocFloat(vertices.length * size);
		float[] theData = new float[vertices.length * size];
		for (int i = 0; i < vertices.length; i++) {
			switch (typeIn) {
			case POS:
				theData[i * 3] = vertices[i].getPosition().getX();
				theData[i * 3 + 1] = vertices[i].getPosition().getY();
				theData[i * 3 + 2] = vertices[i].getPosition().getZ();
				break;
			case COL:
				theData[i * 4] = vertices[i].getColor().getX();
				theData[i * 4 + 1] = vertices[i].getColor().getY();
				theData[i * 4 + 2] = vertices[i].getColor().getZ();
				theData[i * 4 + 3] = vertices[i].getColor().getW();
				break;
			case TEX:
				theData[i * 2] = vertices[i].getTextureCoords().getX();
				theData[i * 2 + 1] = vertices[i].getTextureCoords().getY();
				break;
			}
		}
		theBuffer.put(theData).flip();
		
		int bufferID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, theBuffer, GL15.GL_STATIC_DRAW);
		//shader stuff
		GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
		//unbind the buffer -> 0
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return bufferID;
	}
	
	public void destroy() {
		GL15.glDeleteBuffers(pbo);
		GL15.glDeleteBuffers(cbo);
		GL15.glDeleteBuffers(ibo);
		GL15.glDeleteBuffers(tbo);
		
		GL30.glDeleteVertexArrays(vao);
	}
	
	public Vertex[] getVertices() { return vertices; }
	public int[] getIndices() { return indices; }
	public GameTexture getTexture() { return texture; }
	public boolean isTexture() { return isTexture; }
	public int getVAO() { return vao; }
	public int getPBO() { return pbo; }
	public int getIBO() { return ibo; }
	public int getCBO() { return cbo; }
	public int getTBO() { return tbo; }
	
}
