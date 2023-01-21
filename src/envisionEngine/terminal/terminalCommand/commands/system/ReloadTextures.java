package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

//Author: Hunter Bragg

public class ReloadTextures extends TerminalCommand {
	
	public ReloadTextures() {
		setCategory("System");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "reloadtextures"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("relt", "reltex"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Reloads every registered game texture."; }
	@Override public String getUsage() { return "ex: relt"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		termIn.writeln("Reloading all textures..", 0xffffaa00);
		QoT.getTextureSystem().reloadAllTextures();
		termIn.writeln("Textures Reloaded!", EColors.green);
	}
	
}
