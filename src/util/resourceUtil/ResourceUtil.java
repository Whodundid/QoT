package util.resourceUtil;

public class ResourceUtil {

	/** Returns the actual width in pixels for the given RescoureLocation. */
	/*
	public static int getImageWidth(ResourceLocation locIn) {
		try {
			String path = EUtil.subStringToString(locIn.getResourcePath(), 0, "texture/", true);
			ITextureObject resource = Minecraft.getMinecraft().getTextureManager().getTexture(locIn);
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, resource.getGlTextureId());
			
			int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
			
			return width;
		}
		catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	
	/** Returns the actual height in pixels for the given RescoureLocation. */
	/*
	public static int getImageHeight(ResourceLocation locIn) {
		try {
			String path = EUtil.subStringToString(locIn.getResourcePath(), 0, "texture/", true);
			ITextureObject resource = Minecraft.getMinecraft().getTextureManager().getTexture(locIn);
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, resource.getGlTextureId());
			
			int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
			
			return height;
		}
		catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	*/
	
	/** Returns the actual width in pixels for the given RescoureLocation. */
	public static int getImageWidth(DynamicTextureHandler locIn) {
		try {
			//return locIn.getTextureWidth();
		}
		catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	
	/** Returns the actual height in pixels for the given RescoureLocation. */
	public static int getImageHeight(DynamicTextureHandler locIn) {
		try {
			//return locIn.getTextureHeight();
		}
		catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	
	/** Returns the actual width in pixels for the given RescoureLocation. */
	/*
	public static int getImageWidth(GameTexture locIn) {
		try {
			return locIn.getWidth();
		}
		catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	*/
	
	/** Returns the actual height in pixels for the given RescoureLocation. */
	/*
	public static int getImageHeight(GameTexture locIn) {
		try {
			return locIn.getHeight();
		}
		catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	*/
	
}
