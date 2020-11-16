package gameSystems.textureSystem;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.BufferUtils;
import util.storageUtil.EArrayList;

public class TextureSystem {

	private EArrayList<GameTexture> registeredTextures = new EArrayList();
	private GameTexture current = null;
	
	private static TextureSystem instance;

	//-------------------------
	//TextureSyste Constructors
	//-------------------------

	public static TextureSystem getInstance() {
		return instance = (instance != null) ? instance : new TextureSystem();
	}

	private TextureSystem() {
		// empty for now
	}

	//---------------------
	//TextureSystem Methods
	//---------------------

	/** Attempts to register a texture into OpenGL. Upon passing, the texture will have it's associated file properties loaded. */
	public void registerTexture(GameTexture textureIn) {
		if (textureIn != null) {
			
			// only register textures that aren't already registered!
			if (isTextureRegistered(textureIn)) {
				throw new IllegalStateException("Texture ID already somehow registered! " + textureIn.getTextureID() + " : " + textureIn.getFilePath());
			}
			
			registerI(textureIn);
			registeredTextures.add(textureIn);
			
			if (textureIn.getTextureID() > 0) {
				System.out.println("Registered Texture: " + textureIn.getTextureID() + " : " + textureIn.getFilePath());
			}
			else {
				System.out.println("Failed to register texture: " + textureIn.getFilePath());
			}
		}
	}

	/** Attempts to delete a registered texture from OpenGL. */
	public void destroyTexture(GameTexture textureIn) {
		if (textureIn != null) {
			if (isTextureRegistered(textureIn)) {
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
			else { System.err.println("Texture destroy error: " + textureIn.getFilePath() + " has not been registered!"); }
		}
	}

	/** Binds the current texture to OpenGL if the texture has been registered. */
	public void bind(GameTexture textureIn) {
		if (textureIn != null) {
			if (isTextureRegistered(textureIn)) {
				GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureIn.getTextureID());
				current = textureIn;
			}
			else { System.err.println("Texture bind error: " + textureIn.getFilePath() + " has not been registered!"); }
		}
	}

	/** Returns true if the specified texture has been registered into OpenGL. */
	public boolean isTextureRegistered(GameTexture textureIn) { return textureIn != null && registeredTextures.contains(textureIn); }

	public void destroyAllTextures() {
		//unbind first
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
		current = null;
		
		//destroy each
		registeredTextures.forEach(t -> t.destroy());
	}
	
	//---------------------
	//TextureSystem Getters
	//---------------------
	
	public GameTexture getBoundTexture() { return current; }
	
	//------------------------------
	//TextureSystem Internal Methods
	//------------------------------

	private void registerI(GameTexture textureIn) {

		String filePath = textureIn.getFilePath();
		
		// ensure that OpenGL is initialized first.
		if (GLFW.glfwInit()) {
			try {
				IntBuffer width = BufferUtils.createIntBuffer(1);
				IntBuffer height = BufferUtils.createIntBuffer(1);
				IntBuffer comp = BufferUtils.createIntBuffer(1);
				
				ByteBuffer data = STBImage.stbi_load(textureIn.getFilePath(), width, height, comp, 4);
				int w = width.get(0);
				int h = height.get(0);
				
				int texID = GL46.glGenTextures();
				
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
				
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
				STBImage.stbi_image_free(data);
				
				//--------------------------------------------
				// Set the values onto the GameTexture object
				//--------------------------------------------
				
				Class c = textureIn.getClass();
				
				Field widthField = c.getDeclaredField("width");
				Field heightField = c.getDeclaredField("height");
				Field idField = c.getDeclaredField("textureID");
				
				widthField.setAccessible(true);
				heightField.setAccessible(true);
				idField.setAccessible(true);
				
				widthField.set(textureIn, w);
				heightField.set(textureIn, h);
				idField.set(textureIn, texID);
				
				widthField.setAccessible(false);
				heightField.setAccessible(false);
				idField.setAccessible(false);
			}
			catch (Exception e) {
				e.printStackTrace();
				System.err.println("Failed to read the image information! " + filePath);
			}
		}
	}
	
}
