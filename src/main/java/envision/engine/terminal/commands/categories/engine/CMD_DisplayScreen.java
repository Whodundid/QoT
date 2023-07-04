package envision.engine.terminal.commands.categories.engine;

import envision.Envision;
import envision.engine.screens.GameScreen;
import envision.engine.screens.ScreenRepository;
import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

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
	public void runCommand() {
	    expectAtLeast(1);
		
		if (firstArg().equals("null")) {
			if (Envision.getWorld() != null) Envision.loadWorld(null);
			Envision.displayScreen(null);
			return;
		}
		
		GameScreen s = ScreenRepository.getScreen(firstArg().toLowerCase());
        if (s == null) {
            error("Unrecognized screen name!");
            return;
        }
        
        if (Envision.getWorld() != null) Envision.loadWorld(null);
        Envision.displayScreen(s);
	}
	
}
