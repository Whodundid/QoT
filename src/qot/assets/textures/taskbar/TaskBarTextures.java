package qot.assets.textures.taskbar;

import envisionEngine.renderEngine.textureSystem.GameTexture;
import envisionEngine.renderEngine.textureSystem.TextureSystem;
import qot.assets.TextureLoader;
import qot.settings.QoTSettings;

public class TaskBarTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final TaskBarTextures t = new TaskBarTextures();
	public static TaskBarTextures instance() { return t; }
	
	// Hide constructor
	private TaskBarTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\taskbar\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	experiment 		= new GameTexture(textureDir, "experiment.png"),
	info 			= new GameTexture(textureDir, "info.png"),
	keyboard 		= new GameTexture(textureDir, "keyboard.png"),
	notification 	= new GameTexture(textureDir, "notification.png"),
	opengui 		= new GameTexture(textureDir, "opengui.png"),
	settings 		= new GameTexture(textureDir, "settings.png"),
	terminal 		= new GameTexture(textureDir, "terminal.png"),
	texteditor 		= new GameTexture(textureDir, "texteditor.png"),
	textureviewer 	= new GameTexture(textureDir, "textureviewer.png"),
	window 			= new GameTexture(textureDir, "window.png"),
	calculator		= new GameTexture(textureDir, "calculator.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, experiment);
		reg(sys, info);
		reg(sys, keyboard);
		reg(sys, notification);
		reg(sys, opengui);
		reg(sys, settings);
		reg(sys, terminal);
		reg(sys, texteditor);
		reg(sys, textureviewer);
		reg(sys, window);
		reg(sys, calculator);
	}
	
}
