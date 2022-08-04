package assets.textures.cursor;

import assets.TextureLoader;
import engine.inputHandlers.CursorHelper;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;

public class CursorTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final CursorTextures t = new CursorTextures();
	public static CursorTextures instance() { return t; }
	
	// Hide constructor
	private CursorTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = tDir + "cursor\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	resize_dl = new GameTexture(textureDir, "resize_dl.png"),
	resize_dr = new GameTexture(textureDir, "resize_dr.png"),
	resize_ew = new GameTexture(textureDir, "resize_ew.png"),
	resize_ns = new GameTexture(textureDir, "resize_ns.png"),
	ibeam = new GameTexture(textureDir, "text_pos.png");
	
	//-------------------------------
	
	public static final long
	
	cursor_resize_dl = CursorHelper.createCursorFromGameTexture(resize_dl),
	cursor_resize_dr = CursorHelper.createCursorFromGameTexture(resize_dr);
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, resize_dl);
		reg(sys, resize_dr);
		reg(sys, resize_ew);
		reg(sys, resize_ns);
		reg(sys, ibeam);
	}
	
}
