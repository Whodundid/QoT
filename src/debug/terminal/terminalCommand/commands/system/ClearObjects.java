package debug.terminal.terminalCommand.commands.system;

import debug.terminal.terminalCommand.CommandType;
import debug.terminal.terminalCommand.TerminalCommand;
import debug.terminal.window.ETerminal;
import storageUtil.EArrayList;
import windowLib.windowTypes.interfaces.IWindowObject;

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
			termIn.writeln("Closing Renderer Objects..", 0xff00ffff);
			for (IWindowObject o : objs) {
				if (o.isCloseable()) {
					if (runVisually) { termIn.writeln("Closing: " + o, 0xffffff00); }
					o.close();
				}
			}
			if (runVisually) { termIn.writeln(objs.size() + " closed.", 0xffffaa00); }
		}
	}
	
}
