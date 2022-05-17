package engine.terminal.terminalCommand.commands.system;

import engine.QoT;
import engine.screens.screenUtil.GameScreen;
import engine.terminal.terminalCommand.CommandType;
import engine.terminal.terminalCommand.TerminalCommand;
import engine.terminal.window.ETerminal;
import engine.util.ClassFinder;
import eutil.datatypes.EArrayList;
import eutil.reflection.EModifier;

//Author: Hunter Bragg

public class OpenScreen extends TerminalCommand {
	
	public OpenScreen() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = -1;
	}
	
	@Override public String getName() { return "openscreen"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList("os"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Command used for opening game screens."; }
	@Override public String getUsage() { return "ex: os MainMenuScreen"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) {
			if (args.getFirst().equals("null")) {
				QoT.displayScreen(null);
			}
			else {
				GameScreen s = findScreen(args.getFirst().toLowerCase());
				if (s == null) termIn.error("Unrecognized screen name!");
				else {
					System.out.println("THE SCREEN: " + s);
					QoT.displayScreen(s);
				}
			}
		}
		else {
			termIn.error("Command input cannot be empty!");
			termIn.info(getUsage());
		}
	}
	
	private GameScreen findScreen(String name) {
		String dir = "engine.screens";
		
		EArrayList<Class<GameScreen>> screenClasses = ClassFinder.findClassesOfType(dir, GameScreen.class);
		
		for (Class<GameScreen> c : screenClasses) {
			EModifier m = EModifier.of(c.getModifiers());
			if (m.isPublic() && !m.isAbstract()) {
				try {
					if (c.getConstructor() != null && c.getSimpleName().equalsIgnoreCase(name)) {
						return c.getConstructor().newInstance();
					}
				}
				catch (NoSuchMethodException e) {}
				catch (Exception e) { e.printStackTrace(); break; }
			}
		}
		
		return null;
	}
}
