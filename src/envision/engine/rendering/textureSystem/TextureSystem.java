package envision.engine.rendering.textureSystem;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;

import envision.Envision;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.reflection.EReflectionUtil;

public class TextureSystem {

	private EList<GameTexture> registeredTextures = new EArrayList();
//	private GameTexture current = null;
	
	private static TextureSystem instance;

	//--------------
	// Constructors
	//--------------

	public static TextureSystem getInstance() {
		return instance = (instance != null) ? instance : new TextureSystem();
	}

	private TextureSystem() {
		// empty for now
	}

	//---------
	// Methods
	//---------

	/**
	 * Attempts to register a texture into OpenGL. Upon passing, the texture will
	 * have it's associated file properties loaded.
	 */
	public GameTexture reg(GameTexture in) {
		return registerTexture(in);
	}
	
	/**
	 * Attempts to register a texture into OpenGL. Upon passing, the texture will
	 * have it's associated file properties loaded.
	 */
	public GameTexture registerTexture(GameTexture textureIn) {
		if (textureIn == null) return textureIn;
		// only register textures that aren't already registered!
		if (isTextureRegistered(textureIn)) {
			throw new IllegalStateException("Texture ID already somehow registered! " + textureIn.getTextureID() + " : " + textureIn.getFilePath());
		}
		
		registerI(textureIn, textureIn.getMinFilter(), textureIn.getMagFilter());
		registeredTextures.add(textureIn);
		
		//now register child textures (if there are any)
		if (textureIn.getTextureID() > 0) {
			textureIn.registerChildTextures(this);
		}
		else {
			Envision.error("Failed to register texture: " + textureIn.getFilePath());
		}
		
		return textureIn;
	}
	
	public void reloadAllTextures() {
		Iterator<GameTexture> it = registeredTextures.iterator();
		while (it.hasNext()) {
			GameTexture t = it.next();
			GL11.glDeleteTextures(t.getTextureID());
			registerI(t, t.getMinFilter(), t.getMagFilter());
		}
	}

	/** Attempts to delete a registered texture from OpenGL. */
	public void destroyTexture(GameTexture textureIn) {
		if (textureIn == null) return;
		if (!isTextureRegistered(textureIn)) {
			Envision.error("Texture destroy error: " + textureIn.getFilePath() + " has not been registered!");
			return;
		}
		
		int id = textureIn.getTextureID();

		if (textureIn.destroy()) {
			Iterator<GameTexture> it = registeredTextures.iterator();
			
			while (it.hasNext()) {
				int curID = it.next().getTextureID();
				if (curID == id) {
					it.remove();
					break;
				}
			}
		}
	}

	/** Binds the given texture to OpenGL if the texture has been registered. */
	public void bind(GameTexture textureIn) {
		if (textureIn == null) return;
		if (!isTextureRegistered(textureIn)) {
			Envision.error("Texture bind error: " + textureIn.getFilePath() + " has not been registered!");
			return;
		}
		
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureIn.getTextureID());
	}
	
	/** Unbinds the given texture from the active texture slot in opengl if the texture has been registered. */
	public void unbind(GameTexture textureIn) {
		if (textureIn == null) return;
		if (!isTextureRegistered(textureIn)) {
			Envision.error("Texture unbind error: " + textureIn.getFilePath() + " has not been registered!");
			return;
		}
		
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
	}

	/** Returns true if the specified texture has been registered into OpenGL. */
	public boolean isTextureRegistered(GameTexture textureIn) {
		return textureIn != null && registeredTextures.contains(textureIn);
	}

	public void destroyAllTextures() {
		//unbind first
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
//		current = null;
		
		//destroy each
		registeredTextures.forEach(t -> t.destroy());
	}
	
	//---------
	// Getters
	//---------
	
//	public GameTexture getBoundTexture() {
//		return current;
//	}
	
	//------------------
	// Internal Methods
	//------------------

	private void registerI(GameTexture textureIn, int minFilter, int magFilter) {
		String filePath = textureIn.getFilePath();
		
		// ensure that OpenGL is initialized first.
		if (!GLFW.glfwInit()) return;
		
		try {
			IntBuffer width = BufferUtils.createIntBuffer(1);
			IntBuffer height = BufferUtils.createIntBuffer(1);
			IntBuffer comp = BufferUtils.createIntBuffer(1);
			
			ByteBuffer data = STBImage.stbi_load(textureIn.getFilePath(), width, height, comp, 4);
			int w = width.get(0);
			int h = height.get(0);
			
			int texID = GL46.glGenTextures();
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
			
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
			STBImage.stbi_image_free(data);
			
			//create bufferedImage object for internal use
			//ByteArrayInputStream bas = new ByteArrayInputStream(data.array());
			//BufferedImage img = ImageIO.read(bas);
			
			//--------------------------------------------
			// Set the values onto the GameTexture object
			//--------------------------------------------
			
			EReflectionUtil.setField(textureIn, "width", w);
			EReflectionUtil.setField(textureIn, "height", h);
			EReflectionUtil.setField(textureIn, "textureID", texID);
			//ReflectionHelper.setField(textureIn, "bi", img);
		}
		catch (Exception e) {
			e.printStackTrace();
			Envision.errorf("Failed to read the image information! {}", filePath);
		}
	}
	
}
