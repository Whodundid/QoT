package envision.engine.rendering.textureSystem;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import envision.Envision;
import envision.engine.resourceLoaders.SpriteSheet;
import eutil.datatypes.util.EList;
import eutil.reflection.EReflectionUtil;

public class TextureSystem {

    /** The list of all registered textures within both OpenGL and Envision. */
	private EList<GameTexture> registeredTextures = EList.newList();
	
	/** Map of all registered spritesheets in Envision [String: sheetName, SpriteSheet: sheet]. */
	private Map<String, SpriteSheet> registeredSpriteSheets = new HashMap<>();
	/** An empty sprite sheet to return to prevent outright null pointers. */
	private final SpriteSheet EMPTY_SPRITE_SHEET = new SpriteSheet();
	
	/** The unique static instance of this TextureSystem. */
	private static TextureSystem instance;

	//==============
	// Constructors
	//==============

	public static TextureSystem getInstance() {
	    if (instance == null) instance = new TextureSystem();
	    return instance;
	}

	private TextureSystem() {
		// empty for now
	}

	//=========
	// Methods
	//=========

	/**
	 * Attempts to register a texture into OpenGL. Upon passing, the texture will
	 * have it's associated file properties loaded.
	 */
	public GameTexture reg(GameTexture in) {
		return registerTexture(in);
	}
	
	public SpriteSheet reg(String sheetName, SpriteSheet in) {
	    return registerSpriteSheet(sheetName, in);
	}
	
	/**
	 * Attempts to register a spritesheet into Envision's texture system.
	 */
	public GameTexture registerTexture(GameTexture textureIn) {
	    // prevent null textures!
		if (textureIn == null) {
		    throw new IllegalArgumentException("Error! Cannot register a 'NULL' texture!");
		}
		// only register textures that aren't already registered!
		if (isTextureRegistered(textureIn)) {
			throw new IllegalStateException("Texture ID already somehow registered! " + textureIn.getTextureID() + " : " + textureIn.getFilePath());
		}
		
		int minFilter = textureIn.getMinFilter();
		int magFilter = textureIn.getMagFilter();
		registerI(textureIn, minFilter, magFilter);
		
		// now register child textures (if there are any)
		if (textureIn.getTextureID() > 0) {
			textureIn.registerChildTextures(this);
		}
		else {
			Envision.error("Failed to register texture: " + textureIn.getFilePath());
		}
		
		registeredTextures.add(textureIn);
		
		return textureIn;
	}
	
	/**
     * Attempts to register a texture into OpenGL. Upon passing, the texture will
     * have it's associated file properties loaded.
     */
	public SpriteSheet registerSpriteSheet(String sheetName, SpriteSheet sheetIn) {
	    // prevent null spritesheets
	    if (sheetIn == null) {
	        throw new IllegalArgumentException("Error! Cannot register a 'NULL' spritesheet!");
	    }
	    // only register spritesheets that aren't already registered!
        if (isSpriteSheetRegistered(sheetName, sheetIn)) {
            throw new IllegalStateException("There is a SpriteSheet already registered under that name! " + sheetName);
        }
        
        // check if the underlying texture for the spritesheet is registered
        // if not, register it first
        if (!isTextureRegistered(sheetIn.getBaseTexture())) reg(sheetIn.getBaseTexture());
        // build the spritesheet
        sheetIn.initSheet();
        // finally, register the spritesheet by adding it to the internal map
        registeredSpriteSheets.put(sheetName, sheetIn);
        
        return sheetIn;
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
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIn.getTextureID());
	}
	
	/** Unbinds the given texture from the active texture slot in opengl if the texture has been registered. */
	public void unbind(GameTexture textureIn) {
		if (textureIn == null) return;
		if (!isTextureRegistered(textureIn)) {
			Envision.error("Texture unbind error: " + textureIn.getFilePath() + " has not been registered!");
			return;
		}
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	/** Returns true if the specified texture has been registered into OpenGL. */
	public boolean isTextureRegistered(GameTexture textureIn) {
		return textureIn != null && registeredTextures.contains(textureIn);
	}
	
	/** Returns true if the specified texture has been registered within Envision's TextureSystem. */
	public boolean isSpriteSheetRegistered(String sheetName, SpriteSheet sheetIn) {
	    return sheetIn != null &&
	           registeredSpriteSheets.containsKey(sheetName) &&
	           registeredSpriteSheets.get(sheetName).equals(sheetIn);
	}

	public void destroyAllTextures() {
		// unbind first
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		// destroy each
		registeredTextures.forEach(GameTexture::destroy);
	}
	
	//=========
	// Getters
	//=========
	
    public SpriteSheet getSpriteSheet(String sheetName) {
        return registeredSpriteSheets.getOrDefault(sheetName, EMPTY_SPRITE_SHEET);
    }
	
	//==================
	// Internal Methods
	//==================

	private void registerI(GameTexture textureIn, int minFilter, int magFilter) {
		String filePath = textureIn.getFilePath();
		
		// ensure that OpenGL is initialized first.
		if (!GLFW.glfwInit()) {
		    throw new IllegalStateException("Error! Cannot register texture, GLFW is not initialized!");
		}
		
		if (textureIn.getBufferedImage() != null) {
		    registerFromBufferedImage(textureIn, minFilter, magFilter);
		    return;
		}
		
		try (var stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			
			ByteBuffer data = STBImage.stbi_load(textureIn.getFilePath(), width, height, comp, 4);
			if (data == null) {
			    Envision.errorf("Failed to read the image information! {}", filePath);
			    return;
			}
			
			int w = width.get(0);
			int h = height.get(0);
			
			int texID = GL11.glGenTextures();
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
			
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
			
			//--------------------------------------------
			// Set the values onto the GameTexture object
			//--------------------------------------------
			
			EReflectionUtil.setField(textureIn, "width", w);
			EReflectionUtil.setField(textureIn, "height", h);
			EReflectionUtil.setField(textureIn, "textureID", texID);
			EReflectionUtil.setField(textureIn, "imageBytes", data);
		}
		catch (Exception e) {
			e.printStackTrace();
			Envision.errorf("Failed to read the image information! {}", filePath);
		}
	}
	
	//---------------------------------------------------------------------------------------------------------------------
    
    private void registerFromBufferedImage(GameTexture textureIn, int minFilter, int magFilter) {
        BufferedImage image = textureIn.getBufferedImage();
        try {
            int w = image.getWidth();
            int h = image.getHeight();
            int[] pixels = new int[w * h];
            image.getRGB(0, 0, w, h, pixels, 0, w);
            ByteBuffer buffer = ByteBuffer.allocateDirect(w * h * 4);
            
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    int pixel = pixels[i * w + j];
                    
                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) (pixel & 0xFF));
                    buffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
            
            buffer.flip();
            
            int texID = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
            // I Have no idea why this was not present but I am leaving it commented out to maintain consistency :)
            //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
            
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            
            //--------------------------------------------
            // Set the values onto the GameTexture object
            //--------------------------------------------
            
            EReflectionUtil.setField(textureIn, "width", w);
            EReflectionUtil.setField(textureIn, "height", h);
            EReflectionUtil.setField(textureIn, "textureID", texID);
            EReflectionUtil.setField(textureIn, "imageBytes", buffer);
        }
        catch (Exception e) {
            e.printStackTrace();
            Envision.errorf("Failed to read the image information! {}", image);
        }
	}
	
}
