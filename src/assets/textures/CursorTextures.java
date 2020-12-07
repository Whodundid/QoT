package assets.textures;

import gameSystems.textureSystem.GameTexture;
import gameSystems.textureSystem.TextureSystem;
import util.EUtil;
import util.storageUtil.EArrayList;

public class CursorTextures {
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture resize_dl;
	public static final GameTexture resize_dr;
	public static final GameTexture resize_ew;
	public static final GameTexture resize_ns;
	public static final GameTexture ibeam;
	
	static {
		resize_dl = new GameTexture("resize_dl", "bin/textures/envision/cursor/resize_dl.png");
		resize_dr = new GameTexture("resize_dr", "bin/textures/envision/cursor/resize_dr.png");
		resize_ew = new GameTexture("resize_ew", "bin/textures/envision/cursor/resize_ew.png");
		resize_ns = new GameTexture("resize_ns", "bin/textures/envision/cursor/resize_ns.png");
		ibeam = new GameTexture("ibeam", "bin/textures/envision/cursor/text_pos.png");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(resize_dl);
		systemIn.registerTexture(resize_dr);
		systemIn.registerTexture(resize_ew);
		systemIn.registerTexture(resize_ns);
		systemIn.registerTexture(ibeam);
		
		textures.add(resize_dl);
		textures.add(resize_dr);
		textures.add(resize_ew);
		textures.add(resize_ns);
		textures.add(ibeam);
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
