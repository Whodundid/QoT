package envision.debug.terminal.commands.categories.engine;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

//Author: Hunter Bragg

public class CMD_Version extends TerminalCommand {
	
	public CMD_Version() {
		setCategory("System");
		expectedArgLength = 1;
	}
	
	@Override public String getName() { return "version"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("ver", "v"); }
	@Override public String getHelpInfo(boolean runVisually) { return "displays the version of the provided argument."; }
	@Override public String getUsage() { return "ex: v"; }
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		termIn.writeln(QoT.version);
	}
	
}
