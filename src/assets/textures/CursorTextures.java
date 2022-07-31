package assets.textures;

import engine.inputHandlers.CursorHelper;
import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import main.settings.QoTSettings;

public class CursorTextures {
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture resize_dl;
	public static final GameTexture resize_dr;
	public static final GameTexture resize_ew;
	public static final GameTexture resize_ns;
	public static final GameTexture ibeam;
	
	public static final long cursor_resize_dl;
	public static final long cursor_resize_dr;
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\cursor\\";
	
	static {
		resize_dl = new GameTexture("resize_dl", textureDir, "resize_dl.png");
		resize_dr = new GameTexture("resize_dr", textureDir, "resize_dr.png");
		resize_ew = new GameTexture("resize_ew", textureDir, "resize_ew.png");
		resize_ns = new GameTexture("resize_ns", textureDir, "resize_ns.png");
		ibeam = new GameTexture("ibeam", textureDir, "text_pos.png");
		
		cursor_resize_dl = CursorHelper.createCursorFromGameTexture(resize_dl);
		cursor_resize_dr = CursorHelper.createCursorFromGameTexture(resize_dr);
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
