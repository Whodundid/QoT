package envision.debug.terminal.commands.categories.engine;

import envision.debug.terminal.commands.TerminalCommand;
import envision.debug.terminal.window.ETerminalWindow;
import envision.engine.screens.GameScreen;
import envision.engine.screens.ScreenRepository;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import qot.QoT;

//Author: Hunter Bragg

public class CMD_DisplayScreen extends TerminalCommand {
	
	public CMD_DisplayScreen() {
		setCategory("Engine");
		expectedArgLength = -1;
	}
	
	@Override public String getName() { return "openscreen"; }
	@Override public EList<String> getAliases() { return new EArrayList<>("os"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Command used for opening game screens."; }
	@Override public String getUsage() { return "ex: os MainMenuScreen"; }
	
	@Override
	public void handleTabComplete(ETerminalWindow termIn, EList<String> args) {
		
	}
	
	@Override
	public void runCommand(ETerminalWindow termIn, EList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			termIn.error("Command input cannot be empty!");
			termIn.info(getUsage());
			return;
		}
		
		if (args.getFirst().equals("null")) {
			if (QoT.getWorld() != null) QoT.loadWorld(null);
			QoT.displayScreen(null);
		}
		else {
			GameScreen s = ScreenRepository.getScreen(args.getFirst().toLowerCase());
			if (s == null) termIn.error("Unrecognized screen name!");
			else {
				if (QoT.getWorld() != null) QoT.loadWorld(null);
				QoT.displayScreen(s);
			}
		}
	}
	
}
