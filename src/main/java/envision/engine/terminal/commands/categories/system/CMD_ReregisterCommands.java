package envision.engine.terminal.commands.categories.system;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_ReregisterCommands extends TerminalCommand {
	
	public CMD_ReregisterCommands() {
		setCategory("System");
		expectedArgLength = 0;
	}
	
	@Override public String getName() { return "rebuild"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("rrac", "reb", "rebuildcommands", "rebuildcmds"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Rebuilds the commands in the terminal's command handler."; }
	@Override public String getUsage() { return "ex: rrac -i"; }
	
	@Override
	public void runCommand(ETerminalWindow conIn, EList<String> args, boolean runVisually) {
		conIn.writeln("Rebuilding command list..", 0xffffaa00);
		Envision.getTerminalHandler().reregisterAllCommands(conIn, runVisually);
	}
	
}