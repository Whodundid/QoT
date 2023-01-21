package qot.assets.textures.window;

import envisionEngine.renderEngine.textureSystem.GameTexture;
import envisionEngine.renderEngine.textureSystem.TextureSystem;
import qot.assets.TextureLoader;
import qot.settings.QoTSettings;

public class WindowTextures extends TextureLoader {
	
	//--------------------
	// Singleton Instance
	//--------------------
	
	private static final WindowTextures t = new WindowTextures();
	public static WindowTextures instance() { return t; }
	
	// Hide constructor
	private WindowTextures() {}
	
	//-------------------------------
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\window\\";
	
	//----------
	// Textures
	//----------
	
	public static final GameTexture
	
	checkmark 		= new GameTexture(textureDir, "checkmark.png"),
	close 			= new GameTexture(textureDir, "close.png"),
	close_sel		= new GameTexture(textureDir, "close_sel.png"),
	max 			= new GameTexture(textureDir, "max.png"),
	max_sel 		= new GameTexture(textureDir, "max_sel.png"),
	min 			= new GameTexture(textureDir, "min.png"),
	min_sel 		= new GameTexture(textureDir, "min_sel.png"),
	minimize 		= new GameTexture(textureDir, "minimize.png"),
	minimize_sel 	= new GameTexture(textureDir, "minimize_sel.png"),
	plus 			= new GameTexture(textureDir, "plus.png"),
	plus_sel 		= new GameTexture(textureDir, "plus_sel.png"),
	refresh 		= new GameTexture(textureDir, "refresh.png"),
	refresh_sel 	= new GameTexture(textureDir, "refresh_sel.png"),
	settings 		= new GameTexture(textureDir, "settings.png"),
	settings_sel 	= new GameTexture(textureDir, "settings_sel.png"),
	back 			= new GameTexture(textureDir, "back.png"),
	back_sel 		= new GameTexture(textureDir, "back_sel.png"),
	forward 		= new GameTexture(textureDir, "forward.png"),
	forward_sel 	= new GameTexture(textureDir, "forward_sel.png"),
	file 			= new GameTexture(textureDir, "file.png"),
	file_pic 		= new GameTexture(textureDir, "file_picture.png"),
	file_txt 		= new GameTexture(textureDir, "file_text.png"),
	file_folder 	= new GameTexture(textureDir, "folder_icon.png"),
	file_up 		= new GameTexture(textureDir, "fileup.png"),
	file_up_sel 	= new GameTexture(textureDir, "fileup_sel.png"),
	new_folder 		= new GameTexture(textureDir, "new_folder.png"),
	info 			= new GameTexture(textureDir, "info.png"),
	info_sel 		= new GameTexture(textureDir, "info_sel.png"),
	move 			= new GameTexture(textureDir, "move.png"),
	move_sel 		= new GameTexture(textureDir, "move_sel.png"),
	pin 			= new GameTexture(textureDir, "pin.png"),
	pin_open 		= new GameTexture(textureDir, "pin_open.png"),
	pin_sel 		= new GameTexture(textureDir, "pin_sel.png"),
	pin_open_sel 	= new GameTexture(textureDir, "pin_open_sel.png"),
	problem 		= new GameTexture(textureDir, "problem.png"),
	problem_sel 	= new GameTexture(textureDir, "problem_sel.png"),
	problem_open 	= new GameTexture(textureDir, "problem_open.png"),
	problem_open_sel= new GameTexture(textureDir,"problem_open_sel.png"),
	terminal 		= new GameTexture(textureDir, "terminal.png"),
	terminal_sel 	= new GameTexture(textureDir, "terminal_sel.png");
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onRegister(TextureSystem sys) {
		reg(sys, checkmark);
		reg(sys, close);
		reg(sys, close_sel);
		reg(sys, max);
		reg(sys, max_sel);
		reg(sys, min);
		reg(sys, min_sel);
		reg(sys, minimize);
		reg(sys, minimize_sel);
		reg(sys, plus);
		reg(sys, plus_sel);
		reg(sys, refresh);
		reg(sys, refresh_sel);
		reg(sys, settings);
		reg(sys, settings_sel);
		reg(sys, back);
		reg(sys, back_sel);
		reg(sys, forward);
		reg(sys, forward_sel);
		reg(sys, file);
		reg(sys, file_pic);
		reg(sys, file_txt);
		reg(sys, file_folder);
		reg(sys, file_up);
		reg(sys, file_up_sel);
		reg(sys, new_folder);
		reg(sys, info);
		reg(sys, info_sel);
		reg(sys, move);
		reg(sys, move_sel);
		reg(sys, pin);
		reg(sys, pin_open);
		reg(sys, pin_sel);
		reg(sys, pin_open_sel);
		reg(sys, problem);
		reg(sys, problem_sel);
		reg(sys, problem_open);
		reg(sys, problem_open_sel);
		reg(sys, terminal);
		reg(sys, terminal_sel);
	}
	
}
