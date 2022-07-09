package engine.terminal.terminalCommand.commands.system;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import main.QoT;

//Author: Hunter Bragg

public class ReloadTextures extends TerminalCommand {
	
	public ReloadTextures() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 1;
	}
	
	@Override public String getName() { return "reloadtextures"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList("relt", "reltex"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Reloads every registered game texture."; }
	@Override public String getUsage() { return "ex: relt"; }
	@Override public void handleTabComplete(ETerminal conIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.writeln("Reloading all textures..", 0xffffaa00);
		QoT.getTextureSystem().reloadAllTextures();
		termIn.writeln("Textures Reloaded!", EColors.green);
	}
	
}
