package envision.engine.terminal.commands.categories.system;

import envision.Envision;
import envision.engine.terminal.commands.ConfirmationCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

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
	public Object runCommand_i(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) checkConfirm(termIn, args, runVisually);
		return null;
	}

	@Override
	public void runAction(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		Envision.shutdown();
	}
	
}