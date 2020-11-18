package terminal.terminalCommand.commands.system;

import main.Game;
import terminal.terminalCommand.CommandType;
import terminal.terminalCommand.TerminalCommand;
import terminal.window.ETerminal;
import util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class DebugControl extends TerminalCommand {
	
	public DebugControl() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 1;
	}

	@Override public String getName() { return "debug"; }
	@Override public boolean showInHelp() { return false; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("deb", "dev"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles debug mode for EMC."; }
	@Override public String getUsage() { return "ex: deb init"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() >= 1) {
			termIn.error("This command does not take arguments!");
		}
		else if (args.size() == 1) {
			Game.setDebugMode(!Game.isDebugMode());
			termIn.writeln((Game.isDebugMode()) ? "Enabled" : "Disabled" + " debug mode.");
		}
	}
	
}
