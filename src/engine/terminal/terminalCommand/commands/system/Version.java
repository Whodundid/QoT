package engine.terminal.terminalCommand.commands.system;

import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import main.QoT;

//Author: Hunter Bragg

public class Version extends TerminalCommand {
	
	public Version() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 1;
	}
	
	@Override public String getName() { return "version"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("ver", "v"); }
	@Override public String getHelpInfo(boolean runVisually) { return "displays the version of the provided argument."; }
	@Override public String getUsage() { return "ex: v"; }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.writeln(QoT.version);
	}
	
}
