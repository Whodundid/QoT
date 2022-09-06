package envision.terminal.terminalCommand.commands.system;

import envision.terminal.terminalCommand.TerminalCommand;
import envision.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import game.QoT;

//Author: Hunter Bragg

public class ReregisterCommands extends TerminalCommand {
	
	public ReregisterCommands() {
		setCategory("System");
		numArgs = 0;
	}
	
	@Override public String getName() { return "reregisterallcommands"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("rrac", "reloadcommands", "reloadcmds"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Rebuilds the commands in the terminal's command handler."; }
	@Override public String getUsage() { return "ex: rrac -i"; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		conIn.writeln("Reregistering all commands..", 0xffffaa00);
		QoT.getTerminalHandler().reregisterAllCommands(conIn, runVisually);
	}
	
}
