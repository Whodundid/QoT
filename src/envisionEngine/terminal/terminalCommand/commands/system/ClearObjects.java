package envisionEngine.terminal.terminalCommand.commands.system;

import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import envisionEngine.terminal.terminalCommand.CommandType;
import envisionEngine.terminal.terminalCommand.TerminalCommand;
import envisionEngine.terminal.window.ETerminal;
import util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class ClearObjects extends TerminalCommand {
	
	public ClearObjects() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "clearobj"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("clro"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Clears the objects from the renderer"; }
	@Override public String getUsage() { return "ex: clro"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		EArrayList<IWindowObject> objs = termIn.getTopParent().getCombinedObjects();
		if (objs.contains(termIn)) { objs.remove(termIn); }
		if (objs.isNotEmpty()) {
			termIn.writeln("Closing Renderer Objects..", 0x00ffff);
			for (IWindowObject o : objs) {
				if (o.isCloseable()) {
					if (runVisually) { termIn.writeln("Closing: " + o, 0xffff00); }
					o.close();
				}
			}
			if (runVisually) { termIn.writeln(objs.size() + " closed.", 0xffaa00); }
		}
	}
	
}
