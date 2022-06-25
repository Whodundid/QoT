package assets.textures;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

public class TaskBarTextures {
	
	private static final EArrayList<GameTexture> textures = new EArrayList();
	
	public static final GameTexture experiment;
	public static final GameTexture info;
	public static final GameTexture keyboard;
	public static final GameTexture notification;
	public static final GameTexture opengui;
	public static final GameTexture settings;
	public static final GameTexture terminal;
	public static final GameTexture texteditor;
	public static final GameTexture textureviewer;
	public static final GameTexture window;
	
	private static final String base = "resources/textures/taskbar/";
	
	static {
		experiment 		= new GameTexture("experiment", base, "experiment.png");
		info 			= new GameTexture("info", base, "info.png");
		keyboard 		= new GameTexture("keyboard", base, "keyboard.png");
		notification 	= new GameTexture("notification", base, "notification.png");
		opengui 		= new GameTexture("opengui", base, "opengui.png");
		settings 		= new GameTexture("settings", base, "settings.png");
		terminal 		= new GameTexture("terminal", base, "terminal.png");
		texteditor 		= new GameTexture("texteditor", base, "texteditor.png");
		textureviewer 	= new GameTexture("textureviewer", base, "textureviewer.png");
		window 			= new GameTexture("window", base, "window.png");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		//systemIn.registerTexture(logo);
		//systemIn.registerTexture(noscreens);
		
		//textures.add(logo);
		//textures.add(noscreens);
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
