package assets.textures;

import gameSystems.textureSystem.GameTexture;
import gameSystems.textureSystem.TextureSystem;
import util.EUtil;
import util.storageUtil.EArrayList;

public class WindowTextures {
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture checkmark;
	public static final GameTexture close;
	public static final GameTexture close_sel;
	public static final GameTexture max;
	public static final GameTexture max_sel;
	public static final GameTexture min;
	public static final GameTexture min_sel;
	public static final GameTexture minimize;
	public static final GameTexture minimize_sel;
	public static final GameTexture plus;
	public static final GameTexture plus_sel;
	public static final GameTexture refresh;
	public static final GameTexture refresh_sel;
	public static final GameTexture settings;
	public static final GameTexture settings_sel;
	
	static {
		checkmark = new GameTexture("checkmark", "bin/textures/envision/window/checkmark.png");
		close = new GameTexture("close", "bin/textures/envision/window/close.png");
		close_sel = new GameTexture("close_sel", "bin/textures/envision/window/close_sel.png");
		max = new GameTexture("max", "bin/textures/envision/window/max.png");
		max_sel = new GameTexture("max_sel", "bin/textures/envision/window/max_sel.png");
		min = new GameTexture("min", "bin/textures/envision/window/min.png");
		min_sel = new GameTexture("min_sel", "bin/textures/envision/window/min_sel.png");
		minimize = new GameTexture("minimize", "bin/textures/envision/window/minimize.png");
		minimize_sel = new GameTexture("minimize_sel", "bin/textures/envision/window/minimize_sel.png");
		plus = new GameTexture("plus", "bin/textures/envision/window/plus.png");
		plus_sel = new GameTexture("plus_sel", "bin/textures/envision/window/plus_sel.png");
		refresh = new GameTexture("refresh", "bin/textures/envision/window/refresh.png");
		refresh_sel = new GameTexture("refresh_sel", "bin/textures/envision/window/refresh_sel.png");
		settings = new GameTexture("settings", "bin/textures/envision/window/settings.png");
		settings_sel = new GameTexture("settings_sel", "bin/textures/envision/window/settings_sel.png");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.registerTexture(checkmark);
		systemIn.registerTexture(close);
		systemIn.registerTexture(close_sel);
		systemIn.registerTexture(max);
		systemIn.registerTexture(max_sel);
		systemIn.registerTexture(min);
		systemIn.registerTexture(min_sel);
		systemIn.registerTexture(minimize);
		systemIn.registerTexture(minimize_sel);
		systemIn.registerTexture(plus);
		systemIn.registerTexture(plus_sel);
		systemIn.registerTexture(refresh);
		systemIn.registerTexture(refresh_sel);
		systemIn.registerTexture(settings);
		systemIn.registerTexture(settings_sel);
		
		textures.add(checkmark);
		textures.add(close);
		textures.add(close_sel);
		textures.add(max);
		textures.add(max_sel);
		textures.add(min);
		textures.add(min_sel);
		textures.add(minimize);
		textures.add(minimize_sel);
		textures.add(plus);
		textures.add(plus_sel);
		textures.add(refresh);
		textures.add(refresh_sel);
		textures.add(settings);
		textures.add(settings_sel);
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
