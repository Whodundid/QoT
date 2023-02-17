package qot.assets.textures.world.roofs;

import envision.engine.rendering.textureSystem.GameTexture;

public class GrayRoof extends GameTexture {
	
	private static final String roofDir = tDir + "world\\roofs\\";
	
	public GrayRoof() {
		super(roofDir, "gray_roof_0.png");
		
		addChild(roofDir, "gray_roof_1.png");
		addChild(roofDir, "gray_roof_2.png");
		addChild(roofDir, "gray_roof_2.png");
	}
	
}
