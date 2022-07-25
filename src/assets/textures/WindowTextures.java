package assets.textures;

import engine.renderEngine.textureSystem.GameTexture;
import engine.renderEngine.textureSystem.TextureSystem;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import main.settings.QoTSettings;

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
	public static final GameTexture info;
	public static final GameTexture info_sel;
	public static final GameTexture move;
	public static final GameTexture move_sel;
	public static final GameTexture pin;
	public static final GameTexture pin_open;
	public static final GameTexture pin_sel;
	public static final GameTexture pin_open_sel;
	public static final GameTexture problem;
	public static final GameTexture problem_sel;
	public static final GameTexture problem_open;
	public static final GameTexture problem_open_sel;
	public static final GameTexture terminal;
	public static final GameTexture terminal_sel;
	
	private static final String textureDir = QoTSettings.getResourcesDir().toString() + "\\textures\\window\\";
	
	static {
		checkmark 		= new GameTexture("checkmark", 		textureDir, "checkmark.png");
		close 			= new GameTexture("close", 			textureDir, "close.png");
		close_sel		= new GameTexture("close_sel", 		textureDir, "close_sel.png");
		max 			= new GameTexture("max", 			textureDir, "max.png");
		max_sel 		= new GameTexture("max_sel", 		textureDir, "max_sel.png");
		min 			= new GameTexture("min", 			textureDir, "min.png");
		min_sel 		= new GameTexture("min_sel", 		textureDir, "min_sel.png");
		minimize 		= new GameTexture("minimize", 		textureDir, "minimize.png");
		minimize_sel 	= new GameTexture("minimize_sel", 	textureDir, "minimize_sel.png");
		plus 			= new GameTexture("plus", 			textureDir, "plus.png");
		plus_sel 		= new GameTexture("plus_sel", 		textureDir, "plus_sel.png");
		refresh 		= new GameTexture("refresh", 		textureDir, "refresh.png");
		refresh_sel 	= new GameTexture("refresh_sel", 	textureDir, "refresh_sel.png");
		settings 		= new GameTexture("settings", 		textureDir, "settings.png");
		settings_sel 	= new GameTexture("settings_sel", 	textureDir, "settings_sel.png");
		back 			= new GameTexture("back", 			textureDir, "back.png");
		back_sel 		= new GameTexture("back_sel", 		textureDir, "back_sel.png");
		forward 		= new GameTexture("forward", 		textureDir, "forward.png");
		forward_sel 	= new GameTexture("forward_sel", 	textureDir, "forward_sel.png");
		file 			= new GameTexture("file", 			textureDir, "file.png");
		file_pic 		= new GameTexture("file_pic", 		textureDir, "file_picture.png");
		file_txt 		= new GameTexture("file_txt", 		textureDir, "file_text.png");
		file_folder 	= new GameTexture("file_folder",	textureDir, "folder_icon.png");
		file_up 		= new GameTexture("file_up", 		textureDir, "fileup.png");
		file_up_sel 	= new GameTexture("file_up_sel",	textureDir, "fileup_sel.png");
		new_folder 		= new GameTexture("new_folder",		textureDir, "new_folder.png");
		info 			= new GameTexture("info",			textureDir, "info.png");
		info_sel 		= new GameTexture("info_sel", 		textureDir, "info_sel.png");
		move 			= new GameTexture("move",			textureDir, "move.png");
		move_sel 		= new GameTexture("move_sel", 		textureDir, "move_sel.png");
		pin 			= new GameTexture("pin",	 		textureDir, "pin.png");
		pin_open 		= new GameTexture("pin_open", 		textureDir, "pin_open.png");
		pin_sel 		= new GameTexture("pin_sel", 		textureDir, "pin_sel.png");
		pin_open_sel 	= new GameTexture("pin_open_sel",	textureDir, "pin_open_sel.png");
		problem 		= new GameTexture("problem", 		textureDir, "problem.png");
		problem_sel 	= new GameTexture("problem_sel",	textureDir, "problem_sel.png");
		problem_open 	= new GameTexture("problem_open",	textureDir, "problem_open.png");
		problem_open_sel= new GameTexture("problem_open_sel",textureDir,"problem_open_sel.png");
		terminal 		= new GameTexture("terminal", 		textureDir, "terminal.png");
		terminal_sel 	= new GameTexture("terminal_sel",	textureDir, "terminal_sel.png");
	}
	
	public static void registerTextures(TextureSystem in) {
		textures.add(in.reg(checkmark));
		textures.add(in.reg(close));
		textures.add(in.reg(close_sel));
		textures.add(in.reg(max));
		textures.add(in.reg(max_sel));
		textures.add(in.reg(min));
		textures.add(in.reg(min_sel));
		textures.add(in.reg(minimize));
		textures.add(in.reg(minimize_sel));
		textures.add(in.reg(plus));
		textures.add(in.reg(plus_sel));
		textures.add(in.reg(refresh));
		textures.add(in.reg(refresh_sel));
		textures.add(in.reg(settings));
		textures.add(in.reg(settings_sel));
		textures.add(in.reg(back));
		textures.add(in.reg(back_sel));
		textures.add(in.reg(forward));
		textures.add(in.reg(forward_sel));
		textures.add(in.reg(file));
		textures.add(in.reg(file_pic));
		textures.add(in.reg(file_txt));
		textures.add(in.reg(file_folder));
		textures.add(in.reg(file_up));
		textures.add(in.reg(file_up_sel));
		textures.add(in.reg(new_folder));
		textures.add(in.reg(info));
		textures.add(in.reg(info_sel));
		textures.add(in.reg(move));
		textures.add(in.reg(move_sel));
		textures.add(in.reg(pin));
		textures.add(in.reg(pin_open));
		textures.add(in.reg(pin_sel));
		textures.add(in.reg(pin_open_sel));
		textures.add(in.reg(problem));
		textures.add(in.reg(problem_sel));
		textures.add(in.reg(problem_open));
		textures.add(in.reg(problem_open_sel));
		textures.add(in.reg(terminal));
		textures.add(in.reg(terminal_sel));
	}
	
	public static GameTexture getTextureFromName(String nameIn) {
		return EUtil.getFirst(textures, t -> t.getName().equals(nameIn));
	}
	
}
