package engine.terminal.terminalCommand.commands.system;

import engine.screenEngine.GameScreen;
import engine.screenEngine.ScreenRepository;
import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import eutil.datatypes.EArrayList;
import main.QoT;

//Author: Hunter Bragg

public class OpenScreen extends TerminalCommand {
	
	public OpenScreen() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = -1;
	}
	
	@Override public String getName() { return "openscreen"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<>("os"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Command used for opening game screens."; }
	@Override public String getUsage() { return "ex: os MainMenuScreen"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
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
		else {
			termIn.error("Command input cannot be empty!");
			termIn.info(getUsage());
		}
	}
	
}
