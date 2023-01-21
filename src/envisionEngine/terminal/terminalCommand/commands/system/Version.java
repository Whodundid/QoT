package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

//Author: Hunter Bragg

public class Version extends TerminalCommand {
	
	public Version() {
		setCategory("System");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "version"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("ver", "v"); }
	@Override public String getHelpInfo(boolean runVisually) { return "displays the version of the provided argument."; }
	@Override public String getUsage() { return "ex: v"; }
	
	@Override
	public void runCommand(ETerminal termIn, EList<String> args, boolean runVisually) {
		termIn.writeln(QoT.version);
	}
	
}
