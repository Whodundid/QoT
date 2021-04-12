package envisionEngine.terminal.terminalCommand.commands.system;

import debug.DebugFunctions;
import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import main.Game;
import storageUtil.EArrayList;

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
			try {
				int i = Integer.parseInt(args.get(0));
				if (i >= 0 && i < DebugFunctions.getTotal()) {
					DebugFunctions.runDebugFunction(i, termIn, args.subList(1, args.length()));
				}
				else {
					termIn.error("Value out of range (0-" + DebugFunctions.getTotal() + ")");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				error(termIn, e);
			}
		}
		else if (args.size() == 1) {
			Game.setDebugMode(!Game.isDebugMode());
			termIn.writeln((Game.isDebugMode()) ? "Enabled" : "Disabled" + " debug mode.");
		}
	}
	
}
