package envision.engine.terminal.commands.categories.engine;

import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_ReloadTextures extends TerminalCommand {
	
	public CMD_ReloadTextures() {
		setCategory("System");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "reloadtextures"; }
	@Override public EList<String> getAliases() { return EList.of("relt", "reltex"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Reloads every registered game texture."; }
	@Override public String getUsage() { return "ex: relt"; }
	
	@Override
	public Object runCommand() {
		writeln("Reloading all textures..", 0xffffaa00);
		TextureSystem.getInstance().reloadAllTextures();
		writeln("Textures Reloaded!", EColors.green);
		return null;
	}
	
}
