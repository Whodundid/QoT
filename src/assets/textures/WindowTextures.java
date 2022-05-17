package assets.textures;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

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
	public static final GameTexture back;
	public static final GameTexture back_sel;
	public static final GameTexture forward;
	public static final GameTexture forward_sel;
	public static final GameTexture file;
	public static final GameTexture file_pic;
	public static final GameTexture file_txt;
	public static final GameTexture file_folder;
	public static final GameTexture file_up;
	public static final GameTexture file_up_sel;
	public static final GameTexture new_folder;
	
	private static final String base = "bin/textures/envision/window/";
	
	static {
		checkmark = new GameTexture("checkmark", base, "checkmark.png");
		close = new GameTexture("close", base, "close.png");
		close_sel = new GameTexture("close_sel", base, "close_sel.png");
		max = new GameTexture("max", base, "max.png");
		max_sel = new GameTexture("max_sel", base, "max_sel.png");
		min = new GameTexture("min", base, "min.png");
		min_sel = new GameTexture("min_sel", base, "min_sel.png");
		minimize = new GameTexture("minimize", base, "minimize.png");
		minimize_sel = new GameTexture("minimize_sel", base, "minimize_sel.png");
		plus = new GameTexture("plus", base, "plus.png");
		plus_sel = new GameTexture("plus_sel", base, "plus_sel.png");
		refresh = new GameTexture("refresh", base, "refresh.png");
		refresh_sel = new GameTexture("refresh_sel", base, "refresh_sel.png");
		settings = new GameTexture("settings", base, "settings.png");
		settings_sel = new GameTexture("settings_sel", base, "settings_sel.png");
		back = new GameTexture("back", base, "back.png");
		back_sel = new GameTexture("back_sel", base, "back_sel.png");
		forward = new GameTexture("forward", base, "forward.png");
		forward_sel = new GameTexture("forward_sel", base, "forward_sel.png");
		file = new GameTexture("file", base, "file.png");
		file_pic = new GameTexture("file_pic", base, "file_picture.png");
		file_txt = new GameTexture("file_pic", base, "file_text.png");
		file_folder = new GameTexture("file_pic", base, "folder.png");
		file_up = new GameTexture("file_pic", base, "fileup.png");
		file_up_sel = new GameTexture("file_pic", base, "fileup_sel.png");
		new_folder = new GameTexture("file_pic", base, "new_folder.png");
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
		systemIn.registerTexture(back);
		systemIn.registerTexture(back_sel);
		systemIn.registerTexture(forward);
		systemIn.registerTexture(forward_sel);
		systemIn.registerTexture(file);
		systemIn.registerTexture(file_pic);
		systemIn.registerTexture(file_txt);
		systemIn.registerTexture(file_folder);
		systemIn.registerTexture(file_up);
		systemIn.registerTexture(file_up_sel);
		systemIn.registerTexture(new_folder);
		
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
		textures.add(back);
		textures.add(back_sel);
		textures.add(forward);
		textures.add(forward_sel);
		textures.add(file);
		textures.add(file_pic);
		textures.add(file_txt);
		textures.add(file_folder);
		textures.add(file_up);
		textures.add(file_up_sel);
		textures.add(new_folder);
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
