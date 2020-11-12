package util.resourceUtil;

public class EResourceHandler {

	/*
	public static void bindTexture(ResourceLocation textureIn) {
		if (textureIn != null && isInit()) {
			Minecraft.getMinecraft().renderEngine.bindTexture(textureIn);
		}
	}
	
	public static void bindTexture(DynamicTexture textureIn) {
		if (isInit()) {
			ResourceLocation loc = Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation("texture/", textureIn);
			if (loc != null) {
				bindTexture(loc);
			}
			else {
				System.err.println("EResourceHandler Error: DynamicTexture is null!");
			}
		}
	}
	*/
	/*
	public static void bindTexture(GameTexture textureIn) {
		if (isInit()) {
			if (textureIn != null) {
				Envision.getTextureSystem().bind(textureIn);
			}
		}
	}
	
	public static void bindTexture(DynamicTextureHandler textureIn) {
		if (isInit()) {
			if (textureIn != null) {
				//bindTexture(textureIn.getTextureLocation());
			}
		}
	}
	*/
	//check that mc's renderer is initialized
	private static boolean isInit() {
		return false;
		//return Minecraft.getMinecraft() != null && Minecraft.getMinecraft().renderEngine != null;
	}
	
}
