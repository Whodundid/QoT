package envision.debug.terminal.commands.categories.engine;

import envision.debug.terminal.commands.ConfirmationCommand;
import envision.debug.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

public class CMD_Shutdown extends ConfirmationCommand {
	
	public CMD_Shutdown() {
		setCategory("System");
		setConfirmationString("Warning: You are about to shut down the game engine! Do you wish to continue?");
		expectedArgLength = 0;
	}
	
	@Override public String getName() { return "shutdown"; }
	@Override public String getHelpInfo(boolean runVisually) { return "Shuts the engine down"; }
	@Override public String getUsage() { return "ex: shutdown"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		basicTabComplete(termIn, args, new EArrayList<>("true", "false"));
	}
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) checkConfirm(termIn, args, runVisually);
	}

	@Override
	public void runAction(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		QoT.stopGame();
	}
	
}