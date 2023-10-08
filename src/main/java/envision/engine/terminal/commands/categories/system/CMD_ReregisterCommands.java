package envision.engine.terminal.commands.categories.system;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class CMD_ReregisterCommands extends TerminalCommand {
	
	public CMD_ReregisterCommands() {
		setCategory("System");
		expectedArgLength = 0;
	}
	
	@Override public String getName() { return "rebuild"; }
	@Override public EList<String> getAliases() { return EList.of("rrac", "reb", "rebuildcommands", "rebuildcmds"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Rebuilds the commands in the terminal's command handler."; }
	@Override public String getUsage() { return "ex: rrac -i"; }
	
	@Override
	public Object runCommand() {
	    expectNoArgs();
		writeln("Rebuilding command list..", 0xffffaa00);
		Envision.getTerminalHandler().reregisterAllCommands(term(), runVisually());
		return null;
	}
	
}
