package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import main.Game;
import storageUtil.EArrayList;

//Author: Hunter Bragg

public class ClearTerminalHistory extends TerminalCommand {
	
	public ClearTerminalHistory() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}
	
	@Override public String getName() { return "clearhistory"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clearh", "clrh"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the typed terminal command history"; }
	@Override public String getUsage() { return "ex: clrh"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		Game.getTerminalHandler().clearHistory();
		termIn.writeln("Terminal history cleared..", 0xff55ff55);
	}
	
}
