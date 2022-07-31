package assets.textures;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import main.settings.QoTSettings;

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
	public static final GameTexture calculator;
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\taskbar\\";
	
	static {
		experiment 		= new GameTexture("experiment", textureDir, "experiment.png");
		info 			= new GameTexture("info", textureDir, "info.png");
		keyboard 		= new GameTexture("keyboard", textureDir, "keyboard.png");
		notification 	= new GameTexture("notification", textureDir, "notification.png");
		opengui 		= new GameTexture("opengui", textureDir, "opengui.png");
		settings 		= new GameTexture("settings", textureDir, "settings.png");
		terminal 		= new GameTexture("terminal", textureDir, "terminal.png");
		texteditor 		= new GameTexture("texteditor", textureDir, "texteditor.png");
		textureviewer 	= new GameTexture("textureviewer", textureDir, "textureviewer.png");
		window 			= new GameTexture("window", textureDir, "window.png");
		calculator		= new GameTexture("calc", textureDir, "calculator.png");
	}
	
	public static void registerTextures(TextureSystem systemIn) {
		systemIn.reg(textures.addR(experiment));
		systemIn.reg(textures.addR(info));
		systemIn.reg(textures.addR(keyboard));
		systemIn.reg(textures.addR(notification));
		systemIn.reg(textures.addR(opengui));
		systemIn.reg(textures.addR(settings));
		systemIn.reg(textures.addR(terminal));
		systemIn.reg(textures.addR(texteditor));
		systemIn.reg(textures.addR(textureviewer));
		systemIn.reg(textures.addR(window));
		systemIn.reg(textures.addR(calculator));
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
