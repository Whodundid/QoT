package util.resourceUtil;

import gameSystems.textureSystem.GameTexture;

public class ResourceUtil {
	
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
	public static int getImageWidth(GameTexture locIn) {
		try {
			return locIn.getWidth();
		}
		catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	
	
	/** Returns the actual height in pixels for the given RescoureLocation. */
	public static int getImageHeight(GameTexture locIn) {
		try {
			return locIn.getHeight();
		}
		catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	
}
