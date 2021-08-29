package debug.terminal.terminalCommand.commands.system;

import debug.DebugFunctions;
import debug.terminal.terminalCommand.CommandType;
import debug.terminal.terminalCommand.TerminalCommand;
import debug.terminal.window.ETerminal;
import eutil.storage.EArrayList;
import java.util.List;
import main.QoT;

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
					List passArgs = (args.size() > 1) ? args.subList(1, args.length()) : new EArrayList();
					DebugFunctions.runDebugFunction(i, termIn, passArgs);
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
			QoT.setDebugMode(!QoT.isDebugMode());
			termIn.writeln((QoT.isDebugMode()) ? "Enabled" : "Disabled" + " debug mode.");
		}
	}
	
}
