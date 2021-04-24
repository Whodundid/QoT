package renderEngine.resources;

//Author: Hunter Bragg

/** A utility class to manage Minecraft DynamicTexture objects. */
public class DynamicTextureHandler {

	/*
	
	private final TextureManager textureManager;
	private final DynamicTexture texture;
	private final BufferedImage image;
	private final ResourceLocation location;
	private final int[] textureData;

	public DynamicTextureHandler(final TextureManager manager, final BufferedImage imageIn) {
		textureManager = manager;
		texture = new DynamicTexture(imageIn);
		textureData = texture.getTextureData();
		image = imageIn;
		location = textureManager.getDynamicTextureLocation("texture/", texture);
	}

	/** Sets the image in this DynamicTexture to the new image specified.
	public void updateTextureData(final BufferedImage newImage) {
		final boolean alpha = newImage.getAlphaRaster() != null;
		
		int width = newImage.getWidth();
		int height = newImage.getHeight();
		
		int[] pixels = new int[width * height];
		newImage.getRGB(0, 0, width, height, pixels, 0, width);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = pixels[y * width + x];
				
				buffer.put((byte) (pixel & 0xff));
				buffer.put((byte) ((pixel >> 8) & 0xff));
				buffer.put((byte) ((pixel >> 16) & 0xff));
				buffer.put((byte) ((pixel >> 24) & 0xff));
			}
		}
		buffer.flip();
		
		IntBuffer intBuff = buffer.asIntBuffer();
		
		int[] newImgData = new int[intBuff.limit()];
		intBuff.get(newImgData);
		
		if (textureData.length == newImgData.length) {
			for (int i = 0; i < newImgData.length; i++) {
				textureData[i] = newImgData[i];
			}
		}
		
		texture.updateDynamicTexture();
	}

	/** Returns the MC ResourceLocation for this DynamicTexture.
	public ResourceLocation getTextureLocation() { return location; }
	/** Returns the MC DynamicTexture object this handler is managing.
	public DynamicTexture getDynamicTexture() { return texture; }
	/** Returns a Java BufferedImage of the associated image.
	public BufferedImage GBI() { return image; } //get buffered image
	/** Returns the height of the image in pixels
	public int getTextureHeight() {	return image.getHeight(); }
	/** Returns the width of the image in pixels.
	public int getTextureWidth() { return image.getWidth(); }
	
	/** Cleanly attempts to remove this registered OpenGL image resource from memory.
	public void destroy() {
		TextureManager man = Minecraft.getMinecraft().getTextureManager();
		if (man != null) {
			try {
				Class c = man.getClass();
				Field mapTextures = c.getDeclaredField(EnhancedMC.isObfus() ? "field_110585_a" : "mapTextureObjects");
				
				//open
				mapTextures.setAccessible(true);
				
				Map<ResourceLocation, ITextureObject> map = (Map<ResourceLocation, ITextureObject>) mapTextures.get(man);
				map.remove(location);
				
				//close
				mapTextures.setAccessible(false);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		
		TextureUtil.deleteTexture(texture.getGlTextureId());
		texture.deleteGlTexture();
	}
	
	*/
}
