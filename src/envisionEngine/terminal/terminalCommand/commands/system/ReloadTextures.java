package envision.terminal.terminalCommand.commands.system;

import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import game.QoT;

//Author: Hunter Bragg

public class ReloadTextures extends TerminalCommand {
	
	public ReloadTextures() {
		setCategory("System");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "reloadtextures"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("relt", "reltex"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Reloads every registered game texture."; }
	@Override public String getUsage() { return "ex: relt"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.writeln("Reloading all textures..", 0xffffaa00);
		QoT.getTextureSystem().reloadAllTextures();
		termIn.writeln("Textures Reloaded!", EColors.green);
	}
	
}
